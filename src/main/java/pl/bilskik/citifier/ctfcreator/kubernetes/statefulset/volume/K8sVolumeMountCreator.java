package pl.bilskik.citifier.ctfcreator.kubernetes.statefulset.volume;

import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.VolumeMountBuilder;
import org.springframework.stereotype.Service;

@Service
public class K8sVolumeMountCreator {

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
