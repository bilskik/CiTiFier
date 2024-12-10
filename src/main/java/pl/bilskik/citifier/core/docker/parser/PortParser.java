package pl.bilskik.citifier.core.docker.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.core.docker.entity.Port;
import pl.bilskik.citifier.core.docker.exception.DockerComposeParserException;

import java.util.ArrayList;
import java.util.List;

import static pl.bilskik.citifier.core.docker.parser.TypeConverter.convertToStringList;

@Service
@Slf4j
public class PortParser {

    public List<Port> parsePortList(Object o) {
        log.info("Parsing port: {}", o);
        if(o instanceof List<?>) {
            List<Port> portList = new ArrayList<>();
            List<String> list = convertToStringList((List<?>)o);
            if(list.size() != 1) {
                log.error("Each service in docker-compose can contain maximum one port mapping!");
                throw new DockerComposeParserException("Każdy z serwisów w pliku docker-compose może zawierać maksymalnie jedno mapowanie portów!");
            }
            for(var val: list) {
                portList.add(parsePort(val));
            }
            return portList;
        }

        log.error("Each service in docker-compose can contain maximum one port mapping!");
        throw new DockerComposeParserException("Każdy z serwisów w pliku docker-compose może zawierać maksymalnie jedno mapowanie portów!");
    }

    private Port parsePort(String val) {
        if(val.contains(":") && val.contains("/")) {
            String[] parts = val.split(":");
            if(parts.length == 2 && parts[1].contains("/")) {
                String targetPort = parts[1].split("/")[0];
                String connectionType = parts[1].split("/")[1];
                return createPort(parts[0], targetPort, connectionType);
            }
        } else if(val.contains(":")){
            String[] parts = val.split(":");
            if(parts.length == 2) {
                return createPort(parts[0], parts[1]);
            }
        } else {
            return createPort(val, val);
        }
        log.error("Invalid port definition: {}!", val);
        throw new DockerComposeParserException(String.format("Nieprawidłowa definicja portów: %s!", val));
    }

    private Port createPort(String hostPort, String targetPort) {
        Port port = new Port();
        port.setHostPort(hostPort);
        port.setTargetPort(targetPort);
        return port;
    }

    private Port createPort(String hostPort, String targetPort, String connectionType) {
        Port port = new Port();
        port.setHostPort(hostPort);
        port.setTargetPort(targetPort);
        Port.ConnectionType type = Port.ConnectionType.fromString(connectionType);
        if(type == Port.ConnectionType.UDP) {
            log.error("UDB connection type is currently not supported!");
            throw new DockerComposeParserException("Połączenie typu UDB jest obecnie niewspierane!");
        }
        port.setConnectionType(Port.ConnectionType.fromString(connectionType));
        return port;
    }

}
