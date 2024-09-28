package pl.bilskik.citifier.ctfcreator.kubernetes.statefulset.volume;

import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeBuilder;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceCreationException;

@Service
public class K8sVolumeCreator {

    private final static String CONFIG_MAP = "CONFIG_MAP";
    private final static String EMPTY_DIR = "EMPTY_DIR";
    private final static String INVALID_VOLUME_TYPE = "Nieprawidłowy typ wolumenu! ";

    public Volume createVolume(
            String volumeType,
            String volumeName,
            String sourceName
    ) {
        if(volumeType == null) {
            throw new K8sResourceCreationException(INVALID_VOLUME_TYPE);
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
            default -> throw new K8sResourceCreationException(INVALID_VOLUME_TYPE + volumeType);
        }
    }
}
