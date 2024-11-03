package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import java.util.Map;

public interface DockerEnvironmentStrategy {
    Map<String, String> configure();
}
