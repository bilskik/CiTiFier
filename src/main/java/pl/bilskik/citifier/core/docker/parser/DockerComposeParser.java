package pl.bilskik.citifier.core.docker.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.core.docker.exception.DockerComposeParserException;
import pl.bilskik.citifier.core.docker.entity.DockerCompose;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DockerComposeParser {

    private final static String VERSION = "version";
    private final static String SERVICES = "services";
    private final static String VOLUMES = "volumes";

    private final VolumeParser volumeParser;
    private final ServiceParser serviceParser;

    @SuppressWarnings("unchecked")
    public DockerCompose parse(Map<String, Object> data) {
        log.info("Parsing docker-compose file! Data: {}", data);
        if(data.get(SERVICES) == null) {
            log.error("Services doesn't exist in docker-compose file!");
            throw new DockerComposeParserException("Services doesn't exist in docker-compose file!");
        }

        DockerCompose compose = initializeDockerCompose((String) data.get(VERSION));
        compose.setVolumes(volumeParser.parseVolumes((Map<String, Map<String, Object>>) data.get(VOLUMES)));
        compose.setServices(serviceParser.parseServices((Map<String, Map<String, Object>>) data.get(SERVICES), compose.getVolumes()));

        return compose;
    }

    private DockerCompose initializeDockerCompose(String version) {
        DockerCompose compose = new DockerCompose();
        compose.setVersion(version);
        compose.setServices(new HashMap<>());
        compose.setVolumes(new HashMap<>());
        return compose;
    }

}
