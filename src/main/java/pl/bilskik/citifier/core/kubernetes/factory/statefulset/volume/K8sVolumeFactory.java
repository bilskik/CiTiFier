package pl.bilskik.citifier.core.kubernetes.factory.statefulset.volume;

import io.fabric8.kubernetes.api.model.HostPathVolumeSource;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.core.kubernetes.exception.K8sResourceCreationException;

@Service
@Slf4j
public class K8sVolumeFactory {

    private final static String CONFIG_MAP = "CONFIG_MAP";
    private final static String EMPTY_DIR = "EMPTY_DIR";
    private final static String HOST_PATH = "HOST_PATH";
    private final static String HOST_PATH_TYPE_DIRECTORY = "Directory";

    public Volume createVolume(
            String volumeType,
            String volumeName,
            String sourceName,
            String hostPath
    ) {
        log.info("Creating volume, volumeType: {}, volumeName: {}", volumeType, volumeName);
        if(volumeType == null) {
            throw new K8sResourceCreationException("Invalid volume type!");
        }
        switch(volumeType.toUpperCase()) {
            case CONFIG_MAP -> {
                return new VolumeBuilder()
                        .withName(volumeName)
                        .withNewConfigMap()
                            .withName(sourceName)
                        .endConfigMap()
                        .build();
            }
            case EMPTY_DIR -> {
                return new VolumeBuilder()
                        .withName(volumeName)
                        .withNewEmptyDir()
                        .endEmptyDir()
                        .build();
            }
            case HOST_PATH -> {
                if(hostPath == null) {
                    throw new K8sResourceCreationException("HostPath cannot be null!");
                }
                return new VolumeBuilder()
                        .withName(volumeName)
                        .withHostPath(new HostPathVolumeSource(hostPath, HOST_PATH_TYPE_DIRECTORY))
                        .build();
            }
            default -> throw new K8sResourceCreationException(String.format("Invalid volume type: %s", volumeType));
        }
    }
}
