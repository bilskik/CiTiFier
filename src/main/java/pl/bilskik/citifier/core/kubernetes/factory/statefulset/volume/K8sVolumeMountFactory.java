package pl.bilskik.citifier.core.kubernetes.factory.statefulset.volume;

import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.VolumeMountBuilder;
import org.springframework.stereotype.Service;

@Service
public class K8sVolumeMountFactory {

    public VolumeMount createVolumeMount(
            String volumeName,
            String volumePath
    ) {
        return new VolumeMountBuilder()
                .withName(volumeName)
                .withMountPath(volumePath)
                .build();
    }
}
