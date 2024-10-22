package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import java.util.Map;

public interface DockerEnvironmentStrategy {
    void configure(Map<String, String> env);
}
