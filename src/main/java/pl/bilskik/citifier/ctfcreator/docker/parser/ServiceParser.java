package pl.bilskik.citifier.ctfcreator.docker.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.docker.exception.DockerComposeParserException;
import pl.bilskik.citifier.ctfcreator.docker.entity.ComposeService;
import pl.bilskik.citifier.ctfcreator.docker.entity.Volume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static pl.bilskik.citifier.ctfcreator.docker.parser.TypeConverter.convertToMapString;
import static pl.bilskik.citifier.ctfcreator.docker.parser.TypeConverter.convertToStringList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceParser {

    private final static String IMAGE = "image";
    private final static String CONTAINER_NAME = "container_name";
    private final static String ENVIRONMENT = "environment";
    private final static String PORTS = "ports";
    private final static String VOLUMES = "volumes";
    private final static String COMMAND = "command";
    private final static String ENTRYPOINT = "entrypoint";

    private final BiConsumer<String, Map<String, String>> envFunc = (val, map) -> {
        String[] parts = val.split("=");
        if(parts.length == 2) {
            map.put(parts[0], parts[1]);
        }
    };

    private final VolumeParser volumeParser;
    private final PortParser portParser;
    private final CommandEntrypointParser commandEntrypointParser;

    public  Map<String, ComposeService> parseServices(
            Map<String, Map<String, Object>> services,
            Map<String, Volume> volumes
    ) {
        if(services == null || services.isEmpty()) {
            log.info("No services found in the Docker Compose configuration");
            throw new DockerComposeParserException("Services doesn't exist in docker-compose file!");
        }

        Map<String, ComposeService> composeServices = new HashMap<>();
        services.forEach((serviceName, serviceData) -> {
            if(serviceData == null || serviceData.isEmpty()) {
                log.error("Skipping service {}: No data found!", serviceName);
                throw new DockerComposeParserException(String.format("Service (%s) is empty!", serviceName));
            }
            log.info("Parsing service: {} ", serviceName);

            ComposeService composeService = new ComposeService();
            composeService.setImage((String) serviceData.get(IMAGE));
            composeService.setContainerName((String) serviceData.get(CONTAINER_NAME));
            composeService.setEnvironments(genericMapListParser(serviceData.get(ENVIRONMENT), envFunc));
            composeService.setPorts(portParser.parsePortList(serviceData.get(PORTS)));
            composeService.setVolumes(volumeParser.parseServiceVolume(serviceData.get(VOLUMES), volumes));
            composeService.setEntrypoint(commandEntrypointParser.parseEntrypoint(serviceData.get(ENTRYPOINT)));
            composeService.setCommand(commandEntrypointParser.parseCommand(serviceData.get(COMMAND)));

            composeServices.put(serviceName, composeService);
        });

        return composeServices;
    }

    private Map<String, String> genericMapListParser(Object values, BiConsumer<String, Map<String, String>> callback) {
        if(values instanceof List<?>) {
            List<String> list = convertToStringList((List<?>)values);
            Map<String, String> outputMap = new HashMap<>();
            for(var val : list) {
                if(val != null && !val.isEmpty()) {
                    callback.accept(val, outputMap);
                }
            }
            return outputMap;
        } else if(values instanceof Map<?,?>) {
            return convertToMapString((Map<?,?>) values);
        }

        return new HashMap<>();
    }
}
