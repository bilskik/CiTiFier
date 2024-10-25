package pl.bilskik.citifier.ctfcreator.kubernetes.util;

import io.fabric8.kubernetes.api.model.EnvVar;

import java.util.*;

public class K8sFactoryUtils {

    public static Map<String, String> convertValueToBase64Format(Map<String, String> secretData) {
        Map<String, String> result = new HashMap<>();
        for(var entry : secretData.entrySet()) {
            String base64Val = Base64.getEncoder().encodeToString(entry.getValue().getBytes());
            result.put(entry.getKey(), base64Val);
        }
        return result;
    }

    public static List<EnvVar> convertFromMapToEnvVarList(Map<String, String> envMap) {
        List<EnvVar> envVarList = new ArrayList<>();
        for(var entry: envMap.entrySet()) {
            EnvVar envVar = new EnvVar(entry.getKey(), entry.getValue(), null);
            envVarList.add(envVar);
        }
        return envVarList;
    }

}
