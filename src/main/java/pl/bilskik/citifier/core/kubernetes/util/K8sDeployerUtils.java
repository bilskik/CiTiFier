package pl.bilskik.citifier.core.kubernetes.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pl.bilskik.citifier.core.docker.entity.Port;
import pl.bilskik.citifier.core.kubernetes.exception.K8sResourceCreationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.Integer.parseInt;
import static pl.bilskik.citifier.core.kubernetes.data.K8sConstants.CTF_FLAG_ENV_NAME;
import static pl.bilskik.citifier.core.kubernetes.data.K8sConstants.DB_ENV_NAME;

@Slf4j
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
        if(ports == null || ports.isEmpty()) {
            log.error("Provided ports are empty!");
            throw new K8sResourceCreationException("Provided ports are empty!");
        }
        Port port = ports.getFirst(); //simple case -> one port mapping
        if(isValidPort(port.getHostPort())) {
            return parseInt(port.getHostPort());
        }
        throw new K8sResourceCreationException(String.format(
                "Invalid ports provided! HostPort: %s, TargetPort: %s", port.getHostPort(), port.getTargetPort()
        ));
    }

    private static boolean isValidPort(String value) {
        boolean isNumeric = StringUtils.isNumeric(value);
        if(!isNumeric) {
            return false;
        }
        int port = parseInt(value);
        return port >= MIN_PORT_RANGE && port <= MAX_PORT_RANGE;
    }

    public static String buildDBLinkName(String baseName, int i) {
        return baseName + "-" + i;
    }

    public static Map<String, String> createEnvForSpecificDeployment(Map<String, String> envMap, String flag, int i, String containerName) {
        if(!envMap.containsKey(DB_ENV_NAME)) {
            log.error("Environment variable doesn't contain DB!");
            throw new K8sResourceCreationException("Environment variable doesn't contain DB!");
        }

        envMap = updateDbEnv(envMap, i, containerName);
        envMap = updateCtfFlagEnv(envMap, flag);

        return envMap;
    }

    private static Map<String, String> updateDbEnv(Map<String, String> envMap, int i, String containerName) {
        Map<String, String> target = new HashMap<>(envMap);
        String envValue = buildDBLinkName(containerName, i);
        target.put(DB_ENV_NAME, envValue);
        return target;
    }

    private static Map<String, String> updateCtfFlagEnv(Map<String, String> envMap, String flag) {
        Map<String, String> target = new HashMap<>(envMap);
        target.put(CTF_FLAG_ENV_NAME, flag);
        return target;
    }
}
