package pl.bilskik.citifier.core.kubernetes.util;

import pl.bilskik.citifier.core.kubernetes.exception.K8sResourceCreationException;

import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class K8sEnvironmentExtractor {
    private final static String PASSWORD = "PASSWORD";

    public static Map<String, String> extractNonPasswordEnv(Map<String, String> environments) {
        return extractEnvBasedOnCondition(environments, entry -> !entry.getKey().contains(PASSWORD));
    }

    public static Map<String, String> extractPasswordEnv(Map<String, String> environments) {
        Map<String, String> env = extractEnvBasedOnCondition(environments, entry -> entry.getKey().contains(PASSWORD));
        if(env.size() > 1) {
            throw new K8sResourceCreationException("Cannot contain more than one password env");
        }
        return env;
    }

    private static Map<String, String> extractEnvBasedOnCondition(Map<String, String> environments, Predicate<Map.Entry<String, String>> condition) {
        return environments.entrySet().stream()
                .filter(condition)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public static String getFirstKey(Map<String, String> environments) {
        return environments != null ? environments.entrySet().stream().findFirst().get().getKey() : null;
    }

}
