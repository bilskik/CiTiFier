package pl.bilskik.citifier.core.kubernetes.factory.statefulset.volume;

import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.VolumeMountBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class K8sVolumeMountFactory {

    public VolumeMount createVolumeMount(
            String volumeName,
            String volumePath
    ) {
        log.info("Creating volume mount, name: {}, path: {}", volumeName, volumePath);
        return new VolumeMountBuilder()
                .withName(volumeName)
                .withMountPath(volumePath)
                .build();
    }
}
