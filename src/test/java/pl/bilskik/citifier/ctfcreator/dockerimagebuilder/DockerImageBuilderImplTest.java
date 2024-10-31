package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Testcontainers
class DockerImageBuilderImplTest extends DockerImageDataProvider {
    private static final int PORT = 2375;

    @Mock
    private DockerEnvironmentStrategy environmentStrategy;

    @Mock
    private DockerShellProperties shellProperties;

    @InjectMocks
    private DockerImageBuilderImpl dockerImageBuilder;


    @Container
    public static GenericContainer<?> container =
            new GenericContainer<>(DockerImageName.parse("docker:dind"))
                    .withPrivilegedMode(true)
                    .withExposedPorts(PORT)
                    .withCommand("dockerd", "--host=tcp://0.0.0.0:2375", "--iptables=false"); //windows related

    @BeforeAll
    static void init() {
        container.start();
    }

    @BeforeEach
    void setUp() {
        when(shellProperties.getShell()).thenReturn("powershell.exe");
        when(shellProperties.getConfig()).thenReturn("-Command");
        when(shellProperties.getCommand()).thenReturn("docker-compose build");

        String dockerHost = "tcp://" + container.getHost() + ":" + container.getMappedPort(PORT);
        Map<String, String> envMap = new HashMap<>(){{ put("DOCKER_HOST", dockerHost); }};
        when(environmentStrategy.configure()).thenReturn(envMap);
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