package pl.bilskik.citifier.ctfcreator.kubernetes.config;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import org.springframework.stereotype.Service;

@Service
public class K8sNamespaceCreator {

    private static final String API_VERSION = "v1";

    public Namespace create(String namespace) {
        return new NamespaceBuilder()
                .withApiVersion(API_VERSION)
                .withNewMetadata()
                    .withName(namespace)
                .endMetadata()
                .build();
    }
}
