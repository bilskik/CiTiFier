package pl.bilskik.citifier.ctfcreator.kubernetes.factory.statefulset;


import io.fabric8.kubernetes.api.model.SecretEnvSourceBuilder;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sFactoryUtils.convertFromMapToEnvVarList;

@Service
public class K8sStatefulSetFactory {

    private final static String API_VERSION = "apps/v1";
    private final static int REPLICA_NUMBER = 1;

    public StatefulSet createStatefulSet(
            String statefulSetName,
            Map<String, String> statefulSetLabel,
            Map<String,String> podLabel,
            String containerName,
            String containerImage,
            Map<String, String> envMap,
            String configMapName,
            String secretName,
            List<Volume> volumes,
            List<VolumeMount> volumeMounts
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
                                .withEnv(convertFromMapToEnvVarList(envMap))
//                                .addNewEnvFrom()
//                                    .withConfigMapRef(new ConfigMapEnvSourceBuilder().withName(configMapName).build())
//                                .endEnvFrom()
                                .addNewEnvFrom()
                                    .withSecretRef(new SecretEnvSourceBuilder().withName(secretName).build())
                                .endEnvFrom()
                                .withVolumeMounts(volumeMounts)
                            .endContainer()
                            .withVolumes(volumes)
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();
    }

}
