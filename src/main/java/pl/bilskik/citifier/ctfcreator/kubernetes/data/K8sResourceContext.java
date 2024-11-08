package pl.bilskik.citifier.ctfcreator.kubernetes.data;

import lombok.*;
import pl.bilskik.citifier.ctfcreator.docker.entity.DockerCompose;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class K8sResourceContext {
    private DockerCompose dockerCompose;
    private String appName;
    private boolean isNamespaceCreated;
    private String namespace;
    private String fullRepoFilePath;
    private Integer startExposedPort;
    private Integer numberOfApp;
    private Map<Integer, String> portFlag;
}
