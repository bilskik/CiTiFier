package pl.bilskik.citifier.ctfcreator.kubernetes.config;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class K8sSecretCreator {

    private final static String API_VERSION = "v1";

    public Secret createSecret(
            String secretName,
            Map<String, String> secretLabels,
            String secretType,
            Map<String, String> secretData
    ) {
        return new SecretBuilder()
                .withApiVersion(API_VERSION)
                .withNewMetadata()
                    .withName(secretName)
                    .withLabels(secretLabels)
                .endMetadata()
                .withType(secretType)
                .withData(convertValueToBase64Format(secretData))
                .build();
    }

    public Map<String, String> convertValueToBase64Format(Map<String, String> secretData) {
        Map<String, String> result = new HashMap<>();
        for(var entry : secretData.entrySet()) {
            String base64Val = Base64.getEncoder().encodeToString(entry.getValue().getBytes());
            result.put(entry.getKey(), base64Val);
        }
        return result;
    }
}

