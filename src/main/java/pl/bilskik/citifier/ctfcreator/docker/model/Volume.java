package pl.bilskik.citifier.ctfcreator.docker.model;

import lombok.*;
import pl.bilskik.citifier.ctfcreator.docker.model.enumeration.VolumeType;

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
}
