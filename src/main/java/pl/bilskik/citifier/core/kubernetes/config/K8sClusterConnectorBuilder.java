package pl.bilskik.citifier.core.kubernetes.config;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class K8sClusterConnectorBuilder {
    public KubernetesClient buildClient() {
        log.info("Building cluster connector");
        return new KubernetesClientBuilder().build();
    }
}
