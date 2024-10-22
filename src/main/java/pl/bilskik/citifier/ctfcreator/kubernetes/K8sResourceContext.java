package pl.bilskik.citifier.ctfcreator.kubernetes;

import lombok.*;
import pl.bilskik.citifier.ctfcreator.docker.model.DockerCompose;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class K8sResourceContext {
    private DockerCompose dockerCompose;
    private String appName;
    private String namespace;
    private String deploymentLabel;
    private String serviceLabel;
    private String fullRepoFilePath;
    private Integer startExposedPort;
    private Integer numberOfApp;
}
