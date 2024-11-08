package pl.bilskik.citifier.ctfcreator.docker.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.bilskik.citifier.ctfcreator.docker.entity.ComposeService;
import pl.bilskik.citifier.ctfcreator.docker.entity.Volume;
import pl.bilskik.citifier.ctfcreator.docker.exception.DockerComposeParserException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServiceParserTest {

    @Mock
    private VolumeParser volumeParser;

    @Mock
    private PortParser portParser;

    @Mock
    private CommandEntrypointParser commandEntrypointParser;

    @InjectMocks
    private ServiceParser serviceParser;


    @Test
    void parseServices_ShouldThrowException_WhenServicesIsNull() {
        assertThrows(DockerComposeParserException.class, () -> serviceParser.parseServices(null, new HashMap<>()));
    }

    @Test
    void parseServices_ShouldThrowException_WhenServicesIsEmpty() {
        assertThrows(DockerComposeParserException.class, () -> serviceParser.parseServices(new HashMap<>(), new HashMap<>()));
    }

    @Test
    void parseServices_ShouldThrowException_WhenServicesCountNotTwo() {
        Map<String, Map<String, Object>> services = new HashMap<>();
        services.put("app", new HashMap<>());
        assertThrows(DockerComposeParserException.class, () -> serviceParser.parseServices(services, new HashMap<>()));
    }

    @Test
    void parseServices_ShouldThrowException_WhenDbServiceMissing() {
        Map<String, Map<String, Object>> services = new HashMap<>();
        services.put("app", new HashMap<>());
        services.put("web", new HashMap<>());
        assertThrows(DockerComposeParserException.class, () -> serviceParser.parseServices(services, new HashMap<>()));
    }

    @Test
    void parseServices_ShouldThrowException_WhenServiceDataIsNullOrEmpty() {
        Map<String, Map<String, Object>> services = new HashMap<>();
        services.put("db", new HashMap<>());
        services.put("app", null);
        assertThrows(DockerComposeParserException.class, () -> serviceParser.parseServices(services, new HashMap<>()));
    }

    @Test
    void parseServices_ShouldParseValidServices() {
        Map<String, Map<String, Object>> services = new HashMap<>();
        Map<String, Object> dbService = new HashMap<>();
        dbService.put("image", "postgres");
        dbService.put("container_name", "db_container");
        services.put("db", dbService);

        Map<String, Object> appService = new HashMap<>();
        appService.put("image", "app_image");
        appService.put("container_name", "app_container");
        appService.put("environment", new ArrayList<>() {{ add("DB=db"); }});
        services.put("app", appService);

        Map<String, Volume> volumes = new HashMap<>();

        mockWhenSetup();

        Map<String, ComposeService> result = serviceParser.parseServices(services, volumes);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("db"));
        assertTrue(result.containsKey("app"));
        assertEquals("dbcontainer", result.get("db").getContainerName());
        assertEquals("appcontainer", result.get("app").getContainerName());
        assertNull(result.get("app").getEntrypoint());
        assertNull(result.get("app").getCommand());
    }

    @ParameterizedTest
    @MethodSource("provideContainerNames")
    void parseServices_ShouldSanitizeContainerName(String containerName, String expectedContainerName) {
        Map<String, Map<String, Object>> services = new HashMap<>();
        Map<String, Object> dbService = new HashMap<>();
        dbService.put("image", "postgres");
        dbService.put("container_name", containerName);
        services.put("db", dbService);

        Map<String, Object> appService = new HashMap<>();
        appService.put("image", "app_image");
        appService.put("container_name", "app_container");
        appService.put("environment", new ArrayList<>() {{ add("DB=db"); }});
        services.put("app", appService);
        Map<String, Volume> volumes = new HashMap<>();

        mockWhenSetup();

        Map<String, ComposeService> result = serviceParser.parseServices(services, volumes);

        assertEquals(expectedContainerName, result.get("db").getContainerName());
    }

    private static Stream<Arguments> provideContainerNames() {
        return Stream.of(
                Arguments.of("Container_Name", "containername"),
                Arguments.of("Container-Name", "container-name"),
                Arguments.of("-Container-Name_", "container-name"),
                Arguments.of("Container_#_Name", "containername")
        );
    }

    @Test
    void parseServices_ShouldThrowException_WhenImageIsMissing() {
        Map<String, Map<String, Object>> services = new HashMap<>();
        Map<String, Object> dbService = new HashMap<>();
        dbService.put("container_name", "db_container");
        services.put("db", dbService);

        Map<String, Object> appService = new HashMap<>();
        appService.put("image", "app_image");
        appService.put("container_name", "app_container");
        appService.put("environment", new ArrayList<>() {{ add("DB=db"); }});
        services.put("app", appService);

        mockWhenSetup();

        assertThrows(DockerComposeParserException.class, () ->
                serviceParser.parseServices(services, new HashMap<>())
        );
    }

    @Test
    void parseServices_ShouldThrowException_WhenContainerNameIsMissing() {
        Map<String, Map<String, Object>> services = new HashMap<>();
        Map<String, Object> dbService = new HashMap<>();
        dbService.put("image", "postgres");
        services.put("db", dbService);

        Map<String, Object> appService = new HashMap<>();
        appService.put("image", "app_image");
        appService.put("container_name", "app_container");
        appService.put("environment", new ArrayList<>() {{ add("DB=db"); }});
        services.put("app", appService);

        mockWhenSetup();

        assertThrows(DockerComposeParserException.class, () ->
                serviceParser.parseServices(services, new HashMap<>())
        );
    }

    @Test
    void parseServices_ShouldThrowException_WhenEnvDBIsMissing() {
        Map<String, Map<String, Object>> services = new HashMap<>();
        Map<String, Object> dbService = new HashMap<>();
        dbService.put("image", "postgres");
        dbService.put("container_name", "containerName");
        services.put("db", dbService);

        Map<String, Object> appService = new HashMap<>();
        appService.put("image", "app_image");
        appService.put("container_name", "app_container");
        services.put("app", appService);

        mockWhenSetup();

        assertThrows(DockerComposeParserException.class, () ->
                serviceParser.parseServices(services, new HashMap<>())
        );
    }

    private void mockWhenSetup() {
        when(volumeParser.parseServiceVolume(any(), any())).thenReturn(new ArrayList<>());
        when(portParser.parsePortList(any())).thenReturn(new ArrayList<>());
        when(commandEntrypointParser.parseEntrypoint(any())).thenReturn(null);
        when(commandEntrypointParser.parseCommand(any())).thenReturn(null);
    }

}
