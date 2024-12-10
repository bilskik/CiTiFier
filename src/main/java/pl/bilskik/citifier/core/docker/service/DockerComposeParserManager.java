package pl.bilskik.citifier.core.docker.service;

import pl.bilskik.citifier.core.docker.entity.DockerCompose;

public interface DockerComposeParserManager {
    DockerCompose parse(String filepath);
}
