package pl.bilskik.citifier.ctfcreator.docker.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DockerCompose {
    private String version;
    private Map<String, ComposeService> services;
}
