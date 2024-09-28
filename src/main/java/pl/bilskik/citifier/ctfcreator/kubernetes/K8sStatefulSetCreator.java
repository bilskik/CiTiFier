package pl.bilskik.citifier.ctfcreator.kubernetes;


import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class K8sStatefulSetCreator {

    private final static String API_VERSION = "apps/v1";
    private final static int REPLICA_NUMBER = 2;

    public StatefulSet createStatefulSet(
            String statefulSetName,
            Map<String, String> statefulSetLabel,
            Map<String,String> podLabel,
            String containerName,
            String containerImage,
            String configMapName,
            String secretName,
            String volumePath,
            String volumeName
    ) {
        return new StatefulSetBuilder()
                .withApiVersion(API_VERSION)
                .withNewMetadata()
                    .withName(statefulSetName)
                    .withLabels(statefulSetLabel)
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
                                .addNewEnvFrom()
                                    .withConfigMapRef(new ConfigMapEnvSourceBuilder().withName(configMapName).build())
                                .endEnvFrom()
                                .addNewEnvFrom()
                                    .withSecretRef(new SecretEnvSourceBuilder().withName(secretName).build())
                                .endEnvFrom()
                                .addNewVolumeMount()
                                    .withMountPath(volumePath)
                                    .withName(volumeName)
                                .endVolumeMount()
                            .endContainer()
                            .addNewVolume()
                                .withName(volumeName)
                                .withNewEmptyDir()
                                .endEmptyDir()
                            .endVolume()
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();
    }
}
