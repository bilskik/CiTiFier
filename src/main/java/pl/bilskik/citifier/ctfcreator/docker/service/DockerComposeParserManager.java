package pl.bilskik.citifier.ctfcreator.docker.service;

import pl.bilskik.citifier.ctfcreator.docker.entity.DockerCompose;

public interface DockerComposeParserManager {
    DockerCompose parse(String filepath);
}
