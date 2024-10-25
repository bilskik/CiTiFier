package pl.bilskik.citifier.ctfcreator.docker.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.docker.entity.Command;
import pl.bilskik.citifier.ctfcreator.docker.entity.Entrypoint;
import pl.bilskik.citifier.ctfcreator.docker.enumeration.CommandType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static pl.bilskik.citifier.ctfcreator.docker.parser.TypeConverter.convertToStringList;

@Service
@Slf4j
public class CommandEntrypointParser {

    public Entrypoint parseEntrypoint(Object o) {
        log.info("Parsing entrypoint: {}", o);
        return parseCommandOrEntrypoint(o, Entrypoint::new);
    }

    public Command parseCommand(Object o) {
        log.info("Parsing command: {}", o);
        return parseCommandOrEntrypoint(o, Command::new);
    }

    private <T> T parseCommandOrEntrypoint(Object o, BiFunction<List<String>, CommandType, T> constructor) {
        if(o instanceof String) {
            return constructor.apply(new ArrayList<>(){{ add((String)o);}}, CommandType.SHELL);
        } else if(o instanceof List<?>) {
            List<String> list = convertToStringList((List<?>)o);
            return constructor.apply(list, CommandType.EXEC);
        }
        return null;
    }

}
