package pl.bilskik.citifier.ctfcreator.docker.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DockerCompose {
    private String version;
    private Map<String, Service> services;
}
