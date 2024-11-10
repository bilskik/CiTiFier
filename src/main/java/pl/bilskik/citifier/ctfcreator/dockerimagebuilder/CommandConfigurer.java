package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommandConfigurer {

    private static final String IMAGE = "<image>";

    @Value("${kubernetes.image-load}")
    private String imageLoadCommand;

    @Value("${docker.build}")
    private String dockerBuild;

    public String getImageLoadCommand(String imageName) {
        if(imageLoadCommand == null || imageLoadCommand.isEmpty() || !imageLoadCommand.contains(IMAGE)) {
            throw new DockerImageBuilderException("Cannot resolve image load command!");
        }
        return imageLoadCommand.replace(IMAGE, imageName);
    }

    public String getDockerBuild() {
        return dockerBuild;
    }
}
