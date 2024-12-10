package pl.bilskik.citifier.core.kubernetes.factory.config;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class K8sNamespaceFactory {

    private static final String API_VERSION = "v1";

    public Namespace create(String namespace) {
        log.info("Creating namespace, namespace: {}", namespace);
        return new NamespaceBuilder()
                .withApiVersion(API_VERSION)
                .withNewMetadata()
                    .withName(namespace)
                .endMetadata()
                .build();
    }
}
