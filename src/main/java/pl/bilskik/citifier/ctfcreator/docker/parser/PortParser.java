package pl.bilskik.citifier.ctfcreator.docker.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.docker.entity.Port;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static pl.bilskik.citifier.ctfcreator.docker.parser.TypeConverter.convertToMapString;
import static pl.bilskik.citifier.ctfcreator.docker.parser.TypeConverter.convertToStringList;

@Service
@Slf4j
public class PortParser {

    public List<Port> parsePortList(Object o) {
        log.info("Parsing port: {}", o);
        if(o instanceof List<?>) {
            List<Port> portList = new ArrayList<>();
            List<String> list = convertToStringList((List<?>)o);
            for(var val: list) {
                portList.add(parsePort(val));
            }
            return portList;
        } else if(o instanceof Map<?,?>) {
            List<Port> portList = new ArrayList<>();
            Map<String,String> map = convertToMapString((Map<?,?>) o);
            for(var val: map.entrySet()) {
                portList.add(parsePort(val.getKey() + ":" + val.getValue()));
            }
            return portList;
        }
        return new ArrayList<>();
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
        return null;
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
        port.setConnectionType(Port.ConnectionType.fromString(connectionType));
        return port;
    }

}
