package pl.bilskik.citifier.core.docker.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.core.docker.exception.DockerComposeParserException;
import pl.bilskik.citifier.core.docker.entity.ComposeService;
import pl.bilskik.citifier.core.docker.entity.Volume;

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
            throw new DockerComposeParserException("Serwis nie istnieje w pliku docker-compose!");
        }
        if(services.size() != 2) {
            log.info("Expected exactly 2 services in Docker Compose configuration!");
            throw new DockerComposeParserException("Błąd! Spodziewano dokładnie dwóch serwisów w pliku docker-compose!");
        }
        if(!services.containsKey(DB_SERVICE_NAME)) {
            log.info("Invalid service name: One of the service name must include 'db'!");
            throw new DockerComposeParserException("Nieprawidłowa nazwa serwisu! Jeden z serwisów musi zawierać nazwę 'db'!");
        }

        Map<String, ComposeService> composeServices = new HashMap<>();
        services.forEach((serviceName, serviceData) -> {
            if(serviceName != null && serviceData == null || serviceData.isEmpty()) {
                log.error("Skipping service {}: No data found!", serviceName);
                throw new DockerComposeParserException(String.format("Serwis (%s) jest pusty!", serviceName));
            }
            log.info("Parsing service: {} ", serviceName);

            boolean isAppService = !DB_SERVICE_NAME.equals(serviceName);

            ComposeService composeService = new ComposeService();
            composeService.setImage(validateAndReturnField((String) serviceData.get(IMAGE), IMAGE));
            composeService.setContainerName(normalizeName(validateAndReturnField((String) serviceData.get(CONTAINER_NAME), CONTAINER_NAME)));
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
            log.info("Docker-compose must contain field {}", fieldName);
            throw new DockerComposeParserException(String.format("Docker-compose musi zawierać pole: %s",fieldName));
        }
        return field;
    }

    private String normalizeName(String name) {
        name = name.toLowerCase();
        name = name.replaceAll("[^a-z0-9-]", "");
        name = name.replaceAll("^-+|-+$", "");
        if (name.length() > 253) {
            name = name.substring(0, 253);
        }
        return name;
    }

}
