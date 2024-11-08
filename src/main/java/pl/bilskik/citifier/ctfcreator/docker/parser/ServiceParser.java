package pl.bilskik.citifier.ctfcreator.docker.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.docker.exception.DockerComposeParserException;
import pl.bilskik.citifier.ctfcreator.docker.entity.ComposeService;
import pl.bilskik.citifier.ctfcreator.docker.entity.Volume;

import java.util.HashMap;
import java.util.Map;

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

    private final static String DB_SERVICE_NAME = "db";

    private final EnvironmentVariableParser environmentVariableParser;
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
        if(services.size() != 2) {
            log.info("Expected exactly 2 services in Docker Compose configuration!");
            throw new DockerComposeParserException("Expected exactly 2 services in docker-compose file!");
        }
        if(!services.containsKey(DB_SERVICE_NAME)) {
            log.info("Invalid service name: One of the service name must include 'db'!");
            throw new DockerComposeParserException("Invalid service name: One of the service name must include 'db'!");
        }

        Map<String, ComposeService> composeServices = new HashMap<>();
        services.forEach((serviceName, serviceData) -> {
            if(serviceName != null && serviceData == null || serviceData.isEmpty()) {
                log.error("Skipping service {}: No data found!", serviceName);
                throw new DockerComposeParserException(String.format("Service (%s) is empty!", serviceName));
            }
            log.info("Parsing service: {} ", serviceName);

            boolean isAppService = !DB_SERVICE_NAME.equals(serviceName);

            ComposeService composeService = new ComposeService();
            composeService.setImage(validateAndReturnField((String) serviceData.get(IMAGE), IMAGE));
            composeService.setContainerName(sanitize(validateAndReturnField((String) serviceData.get(CONTAINER_NAME), CONTAINER_NAME)));
            composeService.setEnvironments(environmentVariableParser.parseEnvironment(serviceData.get(ENVIRONMENT), isAppService));
            composeService.setPorts(portParser.parsePortList(serviceData.get(PORTS)));
            composeService.setVolumes(volumeParser.parseServiceVolume(serviceData.get(VOLUMES), volumes));
            composeService.setEntrypoint(commandEntrypointParser.parseEntrypoint(serviceData.get(ENTRYPOINT)));
            composeService.setCommand(commandEntrypointParser.parseCommand(serviceData.get(COMMAND)));

            composeServices.put(serviceName, composeService);
        });

        return composeServices;
    }

    private String validateAndReturnField(String field, String fieldName) {
        if(field == null || field.isEmpty()) {
            throw new DockerComposeParserException(String.format("Docker compose must contain field: %s",fieldName));
        }
        return field;
    }

    private String sanitize(String name) {
        name = name.toLowerCase();
        name = name.replaceAll("[^a-z0-9-]", "");
        name = name.replaceAll("^-+|-+$", "");
        if (name.length() > 253) {
            name = name.substring(0, 253);
        }
        return name;
    }

}
