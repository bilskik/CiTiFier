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

        boolean buildResult = executeDockerComposeBuild(file);

        if(!buildResult) {
            log.error("I can't build image! Image filepath: {}", filepath);
            throw new DockerImageBuilderException("Cannot execute docker build process properly!");
        }
        log.info("Image built successfully!");

        boolean tagResult = tagImageToPointLocalRegistry(imageName);
        if(!tagResult) {
            log.error("I can't tag image! Image filepath: {}", filepath);
            throw new DockerImageBuilderException("Cannot tag image!");
        }
        log.info("Image tagged successfully!");

        boolean pushImageResult = pushImageToRegistry(imageName);
        if(!pushImageResult) {
            log.error("I can't push image to local registry! Push filepath: {}", filepath);
            throw new DockerImageBuilderException("Cannot push image to local registry!");
        }
        log.info("Image tagged successfully!");
    }

    private boolean executeDockerComposeBuild(File file) {
        String command = commandConfigurer.getDockerBuild();
        log.info("Starting Docker Compose build process. Command: {}", command);

        ProcessBuilder processBuilder = createProcessBuilder(
                command,
                file
        );

        return processRunner.startProcess(processBuilder);
    }

    private boolean tagImageToPointLocalRegistry(String image) {
        log.info("Tagging image for local registry. Image: {}", image);

        String command = commandConfigurer.getImageTagCommand(image);
        ProcessBuilder processBuilder = createProcessBuilder(command, null);

        return processRunner.startProcess(processBuilder);
    }

    private boolean pushImageToRegistry(String image) {
        log.info("Pushing image to local registry. Image: {}", image);

        String command = commandConfigurer.getImagePushToRegistryCommand(image);
        ProcessBuilder processBuilder = createProcessBuilder(command, null);

        return processRunner.startProcess(processBuilder);
    }

    private ProcessBuilder createProcessBuilder(String command, File directory) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(shellProperties.getShell(), shellProperties.getConfig(), command);

        if (directory != null) {
            processBuilder.directory(directory);
        }

        processBuilder.redirectErrorStream(true);
        return processBuilder;
    }

}
