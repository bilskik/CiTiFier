package pl.bilskik.citifier.ctfcreator.docker.parser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeConverter {

    public static List<String> convertToStringList(List<?> list) {
        return list.stream()
                .filter(String.class::isInstance)
                .map((elem) -> (String)elem)
                .collect(Collectors.toList());
    }

    public static Map<String, String> convertToMapString(Map<?, ?> map) {
        return map.entrySet().stream()
                .filter((entry) -> entry.getKey() instanceof String && entry.getValue() instanceof String)
                .collect(Collectors.toMap(
                        entry -> (String) entry.getKey(),
                        entry -> (String) entry.getValue()
                ));
    }

}
