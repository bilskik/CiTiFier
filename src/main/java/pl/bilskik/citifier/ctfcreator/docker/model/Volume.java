package pl.bilskik.citifier.ctfcreator.docker.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Volume {
    private String volumeName; //only in VOLUME type
    private VolumeType volumeType;
    private String hostPath; //only in BIND_MOUNT mode
    private String containerPath;
}
