package pl.bilskik.citifier.ctfcreator.kubernetes;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class K8sDeploymentCreator {

    private final static String API_VERSION = "apps/v1";
    private final static int REPLICA_NUMBER = 1;

    public Deployment createDeployment(
            String deploymentName,
            Map<String, String> deploymentLabel,
            Map<String, String> podLabel,
            String containerName,
            String containerImage
    ) {
        return new DeploymentBuilder()
                .withApiVersion(API_VERSION)
                .withNewMetadata()
                    .withName(deploymentName)
                    .withLabels(deploymentLabel)
                .endMetadata()
                .withNewSpec()
                    .withReplicas(REPLICA_NUMBER)
                    .withNewSelector()
                        .addToMatchLabels(podLabel)
                    .endSelector()
                    .withNewTemplate()
                        .withNewMetadata()
                            .addToLabels(podLabel)
                        .endMetadata()
                        .withNewSpec()
                            .withContainers()
                                .addNewContainer()
                                .withName(containerName)
                                .withImage(containerImage)
                            .endContainer()
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();
    }
}
