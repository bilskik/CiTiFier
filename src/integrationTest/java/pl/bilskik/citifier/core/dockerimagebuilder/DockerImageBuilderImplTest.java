package pl.bilskik.citifier.core.dockerimagebuilder;

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
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.k3s.K3sContainer;
import org.testcontainers.utility.DockerImageName;
import pl.bilskik.citifier.core.config.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.bilskik.citifier.core.config.ShellPropertiesConfiguration.configureShellProperties;

@SpringBootTest
@ContextConfiguration(classes = { DockerImageBuilderImpl.class, ProcessRunner.class, CommandConfigurer.class })
@ActiveProfiles("dev")
class DockerImageBuilderImplTest extends DockerImageDataProvider {

    private static OperatingSystem currentOS;

    private static K3sContainer container;

    @MockBean
    private DockerShellProperties dockerShellProperties;

    @Autowired
    private DockerImageBuilderImpl dockerImageBuilder;

    @BeforeAll
    static void init() {
        currentOS = OperatingSystem.getCurrentOS();
        if(currentOS == OperatingSystem.UNKNOWN) {
            throw new IllegalStateException("Cannot determine current operating system!");
        }
        container = new K3sContainer(DockerImageName.parse("rancher/k3s:v1.21.3-k3s1"))
                .withCommand("server");
        container.start();
    }

    @AfterAll
    static void tearDown() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        configureShellProperties(dockerShellProperties, currentOS);
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void build(String dockerfile, String dockerCompose, String image) throws IOException {
        String dockerComposeFilepath = setupDirectory(dockerfile, dockerCompose);

        dockerImageBuilder.build(dockerComposeFilepath, image);
    }

    private static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of(DOCKERFILE_CONTENT_PYTHON, DOCKER_COMPOSE_CONTENT_PYTHON, IMAGE_NAME_PYTHON)
        );
    }

    @Test
    public void build_WhenPathNotExist() {
        String nonExistentPath = "nonexistent/path/to-repo";
        String image = "image";

        assertThrows(DockerImageBuilderException.class, () -> dockerImageBuilder.build(nonExistentPath, image));
    }

    @Test
    public void build_WhenInvalidDockerCompose() throws IOException{
        String dockerComposeFilepath = setupDirectory("", INVALID_DOCKER_COMPOSE);
        String image = "image";

        assertThrows(DockerImageBuilderException.class, () -> dockerImageBuilder.build(dockerComposeFilepath, image));
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