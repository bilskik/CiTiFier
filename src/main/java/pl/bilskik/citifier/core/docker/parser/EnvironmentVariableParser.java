package pl.bilskik.citifier.core.docker.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.core.docker.exception.DockerComposeParserException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static pl.bilskik.citifier.core.docker.parser.TypeConverter.convertToMapString;
import static pl.bilskik.citifier.core.docker.parser.TypeConverter.convertToStringList;

@Service
@Slf4j
public class EnvironmentVariableParser {

    private final static String DB_ENV = "DB";
    private final static String CTF_FLAG_ENV = "CTF_FLAG";

    private final BiConsumer<String, Map<String, String>> callback = (val, map) -> {
        String[] parts = val.split("=");
        if(parts.length == 2) {
            map.put(parts[0], parts[1]);
        }
    };

    public Map<String, String> parseEnvironment(Object env, boolean isAppService) {
        log.info("Parsing environment variables: {}", env);
        Map<String, String> envMap = parse(env, callback);
        return validateEnv(envMap, isAppService);
    }

    private Map<String, String> parse(Object values, BiConsumer<String, Map<String, String>> callback) {
        if(values instanceof List<?>) {
            List<String> list = convertToStringList((List<?>)values);
            Map<String, String> outputMap = new HashMap<>();
            for(var val : list) {
                if(val != null && !val.isEmpty()) {
                    callback.accept(val, outputMap);
                }
            }
            return outputMap;
        } else if(values instanceof Map<?,?>) {
            return convertToMapString((Map<?,?>) values);
        }

        return new HashMap<>();
    }

    private Map<String, String> validateEnv(Map<String, String> envMap, boolean isAppService) {
        if(!isAppService) {
            return envMap;
        }

        if(!envMap.containsKey(DB_ENV)) {
            log.error("Application service must include DB in environment variables!");
            throw new DockerComposeParserException("Serwis aplikacji musi zawierać 'DB' w zmiennych środowiskowych");
        }
        if(!envMap.containsKey(CTF_FLAG_ENV)) {
            log.error("Application service must include CTF_FLAG in environment variables!");
            throw new DockerComposeParserException("Serwis aplikacji musi zawierać 'CTF_FLAG' w zmiennych środowiskowych");
        }

        return envMap;
    }
}
