package pl.bilskik.citifier.core.kubernetes.config;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.springframework.stereotype.Service;

@Service
public class K8sClusterConnectorBuilder {
    public KubernetesClient buildClient() {
        return new KubernetesClientBuilder().build();
    }
}
