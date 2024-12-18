package pl.bilskik.citifier.core.docker.entity;

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
    private Map<String, Volume> volumes;
}
