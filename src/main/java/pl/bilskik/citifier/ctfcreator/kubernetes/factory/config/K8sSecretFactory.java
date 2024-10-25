package pl.bilskik.citifier.ctfcreator.kubernetes.factory.config;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sFactoryUtils.convertValueToBase64Format;

@Service
public class K8sSecretFactory {

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

}

