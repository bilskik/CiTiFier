package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class DockerImageBuilderImpl implements DockerImageBuilder {

    private final DockerShellProperties shellProperties;
    private final ProcessRunner processRunner;
    private final CommandConfigurer commandConfigurer;

    public void build(String filepath, String imageName) {
        File file = new File(filepath);
        if(!file.exists()) {
            log.error("Repository on filepath: {} doesn't exist!", filepath);
            throw new DockerImageBuilderException("Cannot find repository");
        }

        if(imageName == null || imageName.isEmpty()) {
            log.error("Image name is invalid!");
            throw new DockerImageBuilderException("Image name is invalid!");
        }

        boolean buildResult = executeDockerComposeBuild(file);
        if(!buildResult) {
            log.error("I can't build image! Image filepath: {}", filepath);
            throw new DockerImageBuilderException("Cannot execute docker build process properly!");
        }
        log.info("Image built successfully!");

        boolean loadResult = executeImageLoad(imageName);
        if(!loadResult) {
            log.error("I can't load image!");
            throw new DockerImageBuilderException("Cannot load image into kubernetes cluster!");
        }
        log.info("Image loaded successfully!");
    }

    private boolean executeDockerComposeBuild(File file) {
        log.info("Start building process executing docker-compose build");
        String command = commandConfigurer.getDockerBuild();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(file);
        processBuilder.command(shellProperties.getShell(), shellProperties.getConfig(), command);
        processBuilder.redirectErrorStream(true);

        return processRunner.startProcess(processBuilder);
    }

    private boolean executeImageLoad(String image) {
        log.info("Start building process executing loading image to kubernetes");
        String command = commandConfigurer.getImageLoadCommand(image);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(shellProperties.getShell(), shellProperties.getConfig(), command);
        processBuilder.redirectErrorStream(true);

        return processRunner.startProcess(processBuilder);
    }

}
