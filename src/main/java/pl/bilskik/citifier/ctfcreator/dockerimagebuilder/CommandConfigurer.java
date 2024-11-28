package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommandConfigurer {

    @Value("${docker.docker-registry-port}")
    private String registryPort;

    @Value("${docker.build}")
    private String dockerBuild;

    @Value("${docker.push}")
    private String dockerPush;

    @Value("${docker.tag}")
    private String dockerTag;

    public String getDockerBuild() {
        return dockerBuild;
    }

    public String getImageTagCommand(String imageName) {
        return dockerTag + " " + imageName + " " + "localhost:" + registryPort + "/" + imageName;
    }

    public String getImagePushToRegistryCommand(String imageName) {
        return dockerPush + " " + "localhost:5000" + "/" + imageName;
    }

}
