package pl.bilskik.citifier.ctfcreator.kubernetes.util;

import org.apache.commons.lang3.StringUtils;
import pl.bilskik.citifier.ctfcreator.docker.entity.Port;
import pl.bilskik.citifier.ctfcreator.kubernetes.exception.K8sResourceCreationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sConstants.CTF_FLAG_ENV_NAME;
import static pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sConstants.DB_ENV_NAME;

public class K8sDeployerUtils {

    private static final Integer MAX_LENGTH = 20;
    private static final Integer MIN_PORT_RANGE = 1024;
    private static final Integer MAX_PORT_RANGE = 65536;

    public static String buildName(String constName) {
        return constName + "-" + provideRandomCharacters();
    }
    public static String buildName(String constName, int i) {
        return constName + "-" + provideRandomCharacters() + "-" + i;
    }

    public static String provideRandomCharacters() {
        return UUID.randomUUID().toString().replace("-", "").substring(MAX_LENGTH);
    }

    public static Integer providePortToApplication(List<Port> ports) {
        if(ports == null) {
            return null; //throw exception
        }
        Port port = ports.getFirst(); //simple case -> one port mapping
        if(port != null && isValidPort(port.getHostPort())) {
            return Integer.parseInt(port.getHostPort());
        }
        return null; //throw exception
    }

    private static boolean isValidPort(String value) {
        return StringUtils.isNumeric(value);
    }

    public static Map<String, String> createEnvForSpecificDeployment(Map<String, String> envMap, String flag, int i) {
        if(!envMap.containsKey(DB_ENV_NAME)) {
            throw new K8sResourceCreationException("Environment variable doesn't contain DB!");
        }

        envMap = updateDbEnv(envMap, i);
        envMap = updateCtfFlagEnv(envMap, flag);

        return envMap;
    }

    private static Map<String, String> updateDbEnv(Map<String, String> envMap, int i) {
        Map<String, String> target = new HashMap<>(envMap);
        String envValue = envMap.get(DB_ENV_NAME);
        envValue = envValue + "-" + i;
        target.put(DB_ENV_NAME, envValue);
        return target;
    }

    private static Map<String, String> updateCtfFlagEnv(Map<String, String> envMap, String flag) {
        Map<String, String> target = new HashMap<>(envMap);
        target.put(CTF_FLAG_ENV_NAME, flag);
        return target;
    }
}
