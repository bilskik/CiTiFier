package pl.bilskik.citifier.core.kubernetes.factory.config;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static pl.bilskik.citifier.core.kubernetes.util.K8sFactoryUtils.convertValueToBase64Format;

@Service
@Slf4j
public class K8sSecretFactory {

    private final static String API_VERSION = "v1";

    public Secret createSecret(
            String secretName,
            Map<String, String> secretLabels,
            String secretType,
            Map<String, String> secretData
    ) {
        log.info("Creating secret, secretName: {}, secretLabels: {}", secretName, secretLabels);
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

