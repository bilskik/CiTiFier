package pl.bilskik.citifier.ctfcreator.kubernetes.factory.deployment;

import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sFactoryUtils.convertFromMapToEnvVarList;

@Service
public class K8sDeploymentFactory {

    private final static String API_VERSION = "apps/v1";
    private final static int REPLICA_NUMBER = 1;
    private final static String IF_NOT_PRESENT = "IfNotPresent";
    private final static String TCP = "TCP";

    public Deployment createDeployment(
            String deploymentName,
            Map<String, String> deploymentLabel,
            Map<String, String> podLabel,
            String containerName,
            String containerImage,
            Integer containerPort,
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
                                .addNewPort()
                                    .withContainerPort(containerPort)
                                    .withProtocol(TCP)
                                .endPort()
                                .withImagePullPolicy(IF_NOT_PRESENT)
                                .withEnv(convertFromMapToEnvVarList(containerEnv))
                            .endContainer()
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();
    }

}
