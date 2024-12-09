package pl.bilskik.citifier.core.docker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import pl.bilskik.citifier.core.docker.entity.DockerCompose;
import pl.bilskik.citifier.core.docker.exception.DockerComposeParserException;
import pl.bilskik.citifier.core.docker.parser.DockerComposeParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DockerComposeParserManagerImpl implements DockerComposeParserManager {

    private final DockerComposeParser composeParser;
    private final Yaml yaml;

    public DockerCompose parse(String filepath) {
        File file = new File(filepath);

        try(InputStream inputStream = new FileInputStream(file)) {
            Map<String, Object> dockerComposeData = yaml.load(inputStream);
            if(dockerComposeData == null || dockerComposeData.isEmpty()) {
                log.error("Docker-compose file is empty! Filepath: {}", filepath);
                throw new DockerComposeParserException("Plik docker-compose jest pusty!");
            }
            return composeParser.parse(dockerComposeData);
        } catch (IOException e) {
            log.error("Cannot open docker-compose file! Filepath: {}", filepath);
            throw new DockerComposeParserException("Nie można otworzyć pliku docker-compose!");
        }
    }

}
