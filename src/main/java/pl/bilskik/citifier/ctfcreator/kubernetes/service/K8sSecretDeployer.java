package pl.bilskik.citifier.ctfcreator.kubernetes.service;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sResourceContext;
import pl.bilskik.citifier.ctfcreator.kubernetes.factory.config.K8sSecretFactory;

import java.util.Collections;
import java.util.Map;

import static pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sConstants.APP;
import static pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sConstants.SECRET_LABEL;
import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sEnvironmentExtractor.getFirstKey;

@Service
@RequiredArgsConstructor
public class K8sSecretDeployer {
    private final static String SECRET = "secret";
    private final static String OPAQUE = "Opaque";

    private final K8sSecretFactory secretFactory;

    public boolean applySecret(KubernetesClient client, K8sResourceContext context, Map<String, String> passwordEnv) {
        if(passwordEnv.isEmpty()) {
            return false;
        }

        String passwordKey = getFirstKey(passwordEnv);
        Secret passwordSecret = createSecret(passwordKey, passwordEnv.get(passwordKey));
        client.secrets().inNamespace(context.getNamespace()).resource(passwordSecret).create();

        return true;
    }

    private Secret createSecret(String passwordKey, String passwordValue) {
        return secretFactory.createSecret(
                SECRET,
                Collections.singletonMap(APP, SECRET_LABEL),
                OPAQUE,
                Collections.singletonMap(passwordKey, passwordValue)
        );
    }

}
