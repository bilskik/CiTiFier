package pl.bilskik.citifier.ctfcreator.kubernetes;

import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class K8sDeploymentCreator {

    private final static String API_VERSION = "apps/v1";
    private final static int REPLICA_NUMBER = 1;
    private final static String IF_NOT_PRESENT = "IfNotPresent";

    public Deployment createDeployment(
            String deploymentName,
            Map<String, String> deploymentLabel,
            Map<String, String> podLabel,
            String containerName,
            String containerImage,
            Map<String, String> containerEnv
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
                                .withImagePullPolicy(IF_NOT_PRESENT)
                                .withEnv(convertFromMapToEnvVarList(containerEnv))
                            .endContainer()
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();
    }

    public List<EnvVar> convertFromMapToEnvVarList(Map<String, String> envMap) {
        List<EnvVar> envVarList = new ArrayList<>();
        for(var entry: envMap.entrySet()) {
            EnvVar envVar = new EnvVar(entry.getKey(), entry.getValue(), null);
            envVarList.add(envVar);
        }
        return envVarList;
    }
}
