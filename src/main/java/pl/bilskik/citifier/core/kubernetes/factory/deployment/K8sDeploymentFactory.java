package pl.bilskik.citifier.core.kubernetes.factory.deployment;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static pl.bilskik.citifier.core.kubernetes.util.K8sFactoryUtils.convertFromMapToEnvVarList;

@Service
public class K8sDeploymentFactory {

    private final static String API_VERSION = "apps/v1";
    private final static int REPLICA_NUMBER = 1;
    private final static String IF_NOT_PRESENT = "IfNotPresent";
    private final static String TCP = "TCP";

    @Value("${docker.host-ip-address}")
    private String hostIpAddress;

    @Value("${docker.docker-registry-port}")
    private String registryPort;

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
                                .withImage(buildImageLocation(containerImage))
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

    private String buildImageLocation(String image) {
        return hostIpAddress + ":" + registryPort + "/" + image;
    }

}
