package pl.bilskik.citifier.core.docker;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import pl.bilskik.citifier.core.docker.datasource.ValidDockerComposeTestCases;
import pl.bilskik.citifier.core.docker.entity.ComposeService;
import pl.bilskik.citifier.core.docker.entity.DockerCompose;
import pl.bilskik.citifier.core.docker.exception.DockerComposeParserException;
import pl.bilskik.citifier.core.docker.parser.*;

import java.util.Map;
import java.util.stream.Stream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.bilskik.citifier.core.docker.datasource.InvalidDockerComposeTestCases.*;

@SpringBootTest
@ContextConfiguration(classes = {CommandEntrypointParser.class, DockerComposeParser.class, PortParser.class, ServiceParser.class, VolumeParser.class, EnvironmentVariableParser.class})
class DockerComposeParserIntegrationTest extends ValidDockerComposeTestCases {

    @Autowired
    private DockerComposeParser parser;

    @ParameterizedTest
    @MethodSource("validDockerComposeYaml")
    public void dockerParserTest(String yamlInput, DockerCompose expected) {
        Yaml yaml = configureYaml();
        Map<String, Object> yamlData = yaml.load(yamlInput);
        DockerCompose actual = parser.parse(yamlData);

        assertNotNull(actual);
        assertEquals(expected.getVersion(), actual.getVersion());
        assertNotNull(actual.getServices());
        assertEquals(expected.getServices().size(), actual.getServices().size());

        for(var entry: actual.getServices().entrySet()) {
            String actualServiceName = entry.getKey();
            ComposeService actualServiceData = entry.getValue();
            assertNotNull(actualServiceName);
            assertNotNull(actualServiceData);

            ComposeService expectedServiceData = expected.getServices().get(actualServiceName);
            assertNotNull(expectedServiceData);
            assertEquals(expectedServiceData, actualServiceData);
        }

        assertEquals(expected.getVolumes(), actual.getVolumes());
    }

    public static Stream<Arguments> validDockerComposeYaml() {
        return Stream.of(
                Arguments.of(VALID_DOCKER_COMPOSE_1, buildDockerCompose1()),
                Arguments.of(VALID_DOCKER_COMPOSE_2, buildDockerCompose2()),
                Arguments.of(VALID_DOCKER_COMPOSE_3, buildDockerCompose3()),
                Arguments.of(VALID_DOCKER_COMPOSE_4, buildDockerCompose4())
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDockerComposeYaml")
    public void dockerParserTest_WhenInputYamlIsInvalid(String yamlInput) {
        Yaml yaml = configureYaml();
        Map<String, Object> yamlData = yaml.load(yamlInput);

        assertThrows(DockerComposeParserException.class, () -> {
            parser.parse(yamlData);
        });
    }

    public static Stream<Arguments> invalidDockerComposeYaml() {
        return Stream.of(
                Arguments.of(INVALID_DOCKER_COMPOSE_1),
                Arguments.of(INVALID_DOCKER_COMPOSE_2),
                Arguments.of(INVALID_DOCKER_COMPOSE_3),
                Arguments.of(INVALID_DOCKER_COMPOSE_4),
                Arguments.of(INVALID_DOCKER_COMPOSE_5),
                Arguments.of(INVALID_DOCKER_COMPOSE_6),
                Arguments.of(INVALID_DOCKER_COMPOSE_7),
                Arguments.of(INVALID_DOCKER_COMPOSE_8),
                Arguments.of(INVALID_DOCKER_COMPOSE_9)
            );
    }

    private Yaml configureYaml() {
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setSkipMissingProperties(true);
        Constructor constructor = new Constructor(new LoaderOptions());
        constructor.setPropertyUtils(propertyUtils);
        return new Yaml(constructor);
    }

}