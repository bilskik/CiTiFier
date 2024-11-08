package pl.bilskik.citifier.ctfcreator.docker.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bilskik.citifier.ctfcreator.docker.exception.DockerComposeParserException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EnvironmentVariableParserTest {

    private EnvironmentVariableParser parser;

    @BeforeEach
    void setUp() {
        parser = new EnvironmentVariableParser();
    }

    @Test
    void testParseEnvironmentWithListInput() {
        List<String> envList = Arrays.asList("DB=my_database", "CTF_FLAG=my_flag", "EXTRA_VAR=extra");

        Map<String, String> result = parser.parseEnvironment(envList, true);

        assertEquals(3, result.size(), "Expected 3 environment variables.");
        assertEquals("my_database", result.get("DB"));
        assertEquals("my_flag", result.get("CTF_FLAG"));
        assertEquals("extra", result.get("EXTRA_VAR"));
    }

    @Test
    void testParseEnvironmentWithMapInput() {
        Map<String, String> envMap = new HashMap<>();
        envMap.put("DB", "my_database");
        envMap.put("CTF_FLAG", "my_flag");
        envMap.put("EXTRA_VAR", "extra");

        Map<String, String> result = parser.parseEnvironment(envMap, true);

        assertEquals(3, result.size(), "Expected 3 environment variables.");
        assertEquals("my_database", result.get("DB"));
        assertEquals("my_flag", result.get("CTF_FLAG"));
        assertEquals("extra", result.get("EXTRA_VAR"));
    }

    @Test
    void testParseEnvironmentWithMissingDBThrowsException() {
        List<String> envList = Collections.singletonList("CTF_FLAG=my_flag");

        assertThrows(DockerComposeParserException.class, () ->
                parser.parseEnvironment(envList, true)
        );
    }

    @Test
    void testParseEnvironmentWithMissingCTF_FLAGThrowsException() {
        List<String> envList = Collections.singletonList("DB=my_database");

        assertThrows(DockerComposeParserException.class, () ->
                parser.parseEnvironment(envList, true)
        );
    }

    @Test
    void testParseEnvironmentWithNonAppServiceDoesNotThrowException() {
        List<String> envList = Collections.singletonList("EXTRA_VAR=extra");

        Map<String, String> result = parser.parseEnvironment(envList, false);

        assertEquals(1, result.size());
        assertEquals("extra", result.get("EXTRA_VAR"));
    }

    @Test
    void testParseEmptyListInput() {
        List<String> envList = Collections.emptyList();

        Map<String, String> result = parser.parseEnvironment(envList, false);

        assertTrue(result.isEmpty());
    }

    @Test
    void testParseEmptyMapInput() {
        Map<String, String> envMap = Collections.emptyMap();

        Map<String, String> result = parser.parseEnvironment(envMap, false);

        assertTrue(result.isEmpty());
    }
}
