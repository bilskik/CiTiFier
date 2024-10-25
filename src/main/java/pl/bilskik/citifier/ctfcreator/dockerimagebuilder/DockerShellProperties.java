package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("docker.build")
@Getter
@Setter
public class DockerShellProperties {
    private String shell;
    private String config;
    private String command;
}
