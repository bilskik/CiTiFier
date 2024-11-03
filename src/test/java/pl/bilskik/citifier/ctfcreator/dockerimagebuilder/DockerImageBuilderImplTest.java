package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("dev")
@Testcontainers
class DockerImageBuilderImplTest extends DockerImageDataProvider {
    private static final int PORT = 2375;
    private static String OS = System.getProperty("os.name").toLowerCase();

    @MockBean
    private DockerEnvironmentStrategy environmentStrategy;

    @MockBean
    private DockerShellProperties dockerShellProperties;

    @Autowired
    private DockerImageBuilderImpl dockerImageBuilder;

    public static GenericContainer<?> container;

    @BeforeAll
    static void init() {
        if(OS.contains("win")) {
            container = new GenericContainer<>(DockerImageName.parse("docker:dind"))
                    .withPrivilegedMode(true)
                    .withExposedPorts(PORT)
                    .withCommand("dockerd", "--host=tcp://0.0.0.0:2375", "--iptables=false"); //windows related


        } else {
            container = new GenericContainer<>(DockerImageName.parse("docker:dind"))
                    .withPrivilegedMode(true)
                    .withExposedPorts(PORT)
                    .withCommand("dockerd", "--host=tcp://0.0.0.0:2375");
        }
        container.start();
    }

    @BeforeEach
    void setUp() {
        String dockerHost = String.format("tcp://%s:%d", container.getHost(), container.getMappedPort(PORT));
        Map<String, String> envMap = new HashMap<>(){{ put("DOCKER_HOST", dockerHost); }};
        when(environmentStrategy.configure()).thenReturn(envMap);

        if(OS.contains("win")) {
            when(dockerShellProperties.getShell()).thenReturn("powershell.exe");
            when(dockerShellProperties.getConfig()).thenReturn("-Command");
            when(dockerShellProperties.getCommand()).thenReturn("docker-compose build");
        } else {
            when(dockerShellProperties.getShell()).thenReturn("sh");
            when(dockerShellProperties.getConfig()).thenReturn("-c");
            when(dockerShellProperties.getCommand()).thenReturn("docker-compose build");
        }
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void build(String dockerfile, String dockerCompose) throws IOException {

        String dockerComposeFilepath = setupDirectory(dockerfile, dockerCompose);

        dockerImageBuilder.build(dockerComposeFilepath);
    }

    private static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of(DOCKERFILE_CONTENT_PYTHON, DOCKER_COMPOSE_CONTENT_PYTHON),
                Arguments.of("", DOCKER_COMPOSE_SIMPLE_FLASK_APP),
                Arguments.of("", DOCKER_COMPOSE_SIMPLE_SPRING_BOOT_APP)
        );
    }

    @Test
    public void build_WhenPathNotExist() {
        String nonExistentPath = "nonexistent/path/to-repo";
        assertThrows(DockerImageBuilderException.class, () -> dockerImageBuilder.build(nonExistentPath));
    }

    @Test
    public void build_WhenInvalidDockerCompose() throws IOException {
        String dockerComposeFilepath = setupDirectory("", INVALID_DOCKER_COMPOSE);

        assertThrows(DockerImageBuilderException.class, () -> dockerImageBuilder.build(dockerComposeFilepath));
    }

    private String setupDirectory(String dockerfile, String dockerCompose) throws IOException {
        Path tempDirectory = Files.createTempDirectory("buildtest");
        File tempDockerfile = new File(tempDirectory.toFile(), "Dockerfile");
        File tempDockerComposeFile = new File(tempDirectory.toFile(), "docker-compose.yaml");
        String dockerComposeFilepath = tempDirectory.toFile().getAbsolutePath();

        Files.write(tempDockerfile.toPath(), dockerfile.getBytes());
        Files.write(tempDockerComposeFile.toPath(), dockerCompose.getBytes());

        return dockerComposeFilepath;
    }

}