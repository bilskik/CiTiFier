package pl.bilskik.citifier.core.docker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bilskik.citifier.core.docker.entity.DockerCompose;
import pl.bilskik.citifier.core.docker.entity.Volume;
import pl.bilskik.citifier.core.docker.exception.DockerComposeParserException;
import pl.bilskik.citifier.core.docker.parser.DockerComposeParser;
import pl.bilskik.citifier.core.docker.parser.ServiceParser;
import pl.bilskik.citifier.core.docker.parser.VolumeParser;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DockerComposeParserTest {

    @Mock
    private VolumeParser volumeParser;

    @Mock
    private ServiceParser serviceParser;

    @InjectMocks
    private DockerComposeParser dockerComposeParser;

    @SuppressWarnings("unchecked")
    @Test
    void parse_ShouldReturnDockerCompose_WhenDataIsValid() {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> volumes = new HashMap<>();
        Map<String, Object> services = new HashMap<>();

        data.put("version", "3.8");
        data.put("volumes", new HashMap<>(){{ put("volume-example", volumes); }});
        data.put("services", new HashMap<>(){{ put("service-example", services); }});

        DockerCompose expectedCompose = new DockerCompose();
        expectedCompose.setVersion("3.8");

        when(volumeParser.parseVolumes((Map<String, Map<String, Object>>) any(Map.class)))
                .thenReturn(new HashMap<>());
        when(serviceParser.parseServices((Map<String, Map<String, Object>>) any(Map.class),(Map<String, Volume>) any(Map.class)))
                .thenReturn(new HashMap<>());

        DockerCompose result = dockerComposeParser.parse(data);

        verify(volumeParser).parseVolumes((Map<String, Map<String, Object>>) any(Map.class));
        verify(serviceParser).parseServices((Map<String, Map<String, Object>>) any(Map.class),(Map<String, Volume>) any(Map.class));
        assertEquals(expectedCompose.getVersion(), result.getVersion());
        assertEquals(new HashMap<>(), result.getServices());
        assertEquals(new HashMap<>(), result.getVolumes());
    }

    @Test
    void parse_ShouldThrowException_WhenServicesAreMissing() {
        Map<String, Object> data = new HashMap<>();
        data.put("version", "3.8");

        assertThrows(DockerComposeParserException.class, () ->
                dockerComposeParser.parse(data)
        );
    }
}