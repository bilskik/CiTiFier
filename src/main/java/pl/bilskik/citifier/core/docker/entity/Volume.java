package pl.bilskik.citifier.core.docker.entity;

import lombok.*;
import pl.bilskik.citifier.core.docker.enumeration.VolumeType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Volume {
    private String volumeName; //only in VOLUME type
    private VolumeType volumeType;
    private String hostPath; //only in BIND_MOUNT mode
    private String containerPath;

    public boolean isBindMount() {
        return volumeType == VolumeType.BIND_MOUNT;
    }

    public boolean isVolume() {
        return volumeType == VolumeType.VOLUME;
    }
}
