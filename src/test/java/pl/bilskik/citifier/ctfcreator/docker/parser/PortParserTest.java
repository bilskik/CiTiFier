package pl.bilskik.citifier.ctfcreator.docker.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bilskik.citifier.ctfcreator.docker.entity.Port;
import pl.bilskik.citifier.ctfcreator.docker.exception.DockerComposeParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PortParserTest {

    @InjectMocks
    private PortParser portParser;

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void parsePortList(List<String> yamlPorts, String hostPort, String targetPort) {
        List<Port> portList = portParser.parsePortList(yamlPorts);

        assertNotNull(portList);
        assertEquals(portList.size(), 1);
        assertNotNull(portList.getFirst());
        assertEquals(hostPort, portList.getFirst().getHostPort());
        assertEquals(targetPort, portList.getFirst().getTargetPort());
        assertEquals(Port.ConnectionType.TCP, portList.getFirst().getConnectionType());
    }

    public static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of(new ArrayList<>(){{ add("8080:80"); }}, "8080", "80"),
                Arguments.of(new ArrayList<>(){{ add("2138:2138"); }}, "2138", "2138"),
                Arguments.of(new ArrayList<>(){{ add("8000"); }}, "8000", "8000")
        );
    }

    @Test
    public void parsePortList_WhenPortSizeIsMoreThanOne() {
        List<String> yamlPorts = new ArrayList<>(){{ add("8080"); add("8080:80"); }};

        assertThrows(DockerComposeParserException.class, () -> {
            portParser.parsePortList(yamlPorts);
        });
    }

    @Test
    public void parsePortList_WhenNoPortMapping() {
        List<String> yamlPorts = new ArrayList<>();

        assertThrows(DockerComposeParserException.class, () -> {
            portParser.parsePortList(yamlPorts);
        });
    }

    @Test
    public void parsePortList_WhenUDPConnectionType() {
        List<String> yamlPorts = new ArrayList<>(){{ add("8080:8000/udp"); }};

        assertThrows(DockerComposeParserException.class, () -> {
            portParser.parsePortList(yamlPorts);
        });
    }
}