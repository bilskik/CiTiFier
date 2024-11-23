package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommandConfigurer {

    @Value("${docker.registry-ip-address}")
    private String registryIpAddress;

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
        String registryIpAddress = validateRegistryIpAddress();
        return dockerTag + " " + imageName + " " + registryIpAddress + "/" + imageName;
    }

    public String getImagePushToRegistryCommand(String imageName) {
        String registryIpAddress = validateRegistryIpAddress();
        return dockerPush + " " + registryIpAddress + "/" + imageName;
    }

    private String validateRegistryIpAddress() {
        if(registryIpAddress == null || registryIpAddress.isEmpty()) {
            throw new DockerImageBuilderException("Cannot resolve docker registry ip!");
        }
        return registryIpAddress;
    }

}
