package pl.bilskik.citifier.ctfcreator.kubernetes;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class K8sClusterConnectorBuilder {

    @Value("${kubernetess.cluster-config-path}")
    private String configPath;

    public KubernetesClient buildClient() {
        return new KubernetesClientBuilder().build();
    }
}
