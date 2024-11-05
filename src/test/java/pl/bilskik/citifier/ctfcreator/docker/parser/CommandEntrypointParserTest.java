package pl.bilskik.citifier.ctfcreator.docker.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bilskik.citifier.ctfcreator.docker.entity.Command;
import pl.bilskik.citifier.ctfcreator.docker.entity.Entrypoint;
import pl.bilskik.citifier.ctfcreator.docker.enumeration.CommandType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommandEntrypointParserTest {

    @InjectMocks
    private CommandEntrypointParser parser;

    @Test
    void testParseEntrypointWithString() {
        String input = "sample-command";

        Entrypoint entrypoint = parser.parseEntrypoint(input);

        assertNotNull(entrypoint);
        assertEquals(Collections.singletonList("sample-command"), entrypoint.getCommand());
        assertEquals(CommandType.SHELL, entrypoint.getType());
    }

    @Test
    void testParseEntrypointWithStringList() {
        List<String> input = Arrays.asList("command1", "command2");

        Entrypoint entrypoint = parser.parseEntrypoint(input);

        assertNotNull(entrypoint);
        assertEquals(input, entrypoint.getCommand());
        assertEquals(CommandType.EXEC, entrypoint.getType());
    }

    @Test
    void testParseEntrypointWithInvalidType() {
        Integer invalidInput = 123;

        Entrypoint entrypoint = parser.parseEntrypoint(invalidInput);

        assertNull(entrypoint);
    }

    @Test
    void testParseCommandWithString() {
        String input = "sample-command";

        Command command = parser.parseCommand(input);

        assertNotNull(command);
        assertEquals(Collections.singletonList("sample-command"), command.getCommand());
        assertEquals(CommandType.SHELL, command.getType());
    }

    @Test
    void testParseCommandWithStringList() {
        List<String> input = Arrays.asList("command1", "command2");

        Command command = parser.parseCommand(input);

        assertNotNull(command);
        assertEquals(input, command.getCommand());
        assertEquals(CommandType.EXEC, command.getType());
    }

    @Test
    void testParseCommandWithInvalidType() {
        Double invalidInput = 123.45;

        Command command = parser.parseCommand(invalidInput);

        assertNull(command);
    }

}
