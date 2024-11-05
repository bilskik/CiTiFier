package pl.bilskik.citifier.ctfcreator.docker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yaml.snakeyaml.Yaml;
import pl.bilskik.citifier.ctfcreator.docker.entity.DockerCompose;
import pl.bilskik.citifier.ctfcreator.docker.exception.DockerComposeParserException;
import pl.bilskik.citifier.ctfcreator.docker.parser.DockerComposeParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DockerComposeParserManagerImplTest {

    @Mock
    private DockerComposeParser composeParser;

    @Mock
    private Yaml yaml;

    @InjectMocks
    private DockerComposeParserManagerImpl parserManager;

    @Test
    void parse_ShouldReturnParsedDockerCompose_WhenFileIsValid() throws IOException {
        String filepath = providePath();
        Map<String, Object> dockerComposeData = new HashMap<>();
        DockerCompose expectedCompose = new DockerCompose();
        dockerComposeData.put("version", "3.8");

        when(yaml.load(any(InputStream.class))).thenReturn(dockerComposeData);
        when(composeParser.parse(dockerComposeData)).thenReturn(expectedCompose);

        DockerCompose result = parserManager.parse(filepath);

        verify(yaml).load(any(InputStream.class));
        verify(composeParser).parse(dockerComposeData);
        assertEquals(expectedCompose, result);
    }

    @Test
    void parse_ShouldThrowException_WhenFileIsEmpty() throws IOException {
        String filepath = providePath();
        when(yaml.load(any(InputStream.class))).thenReturn(null);

        assertThrows(DockerComposeParserException.class, () ->
                parserManager.parse(filepath)
        );

        verify(yaml).load(any(InputStream.class));
        verify(composeParser, never()).parse(any());
    }

    @Test
    void parse_ShouldThrowException_WhenFileCannotBeOpened() {
        String filepath = "nonexistent-file.yml";

        assertThrows(DockerComposeParserException.class, () ->
                parserManager.parse(filepath)
        );
    }

    private String providePath() throws IOException {
        File tempDockerComposeFile = File.createTempFile("test-docker-compose", ".yml");
        return tempDockerComposeFile.getAbsolutePath();
    }
}
