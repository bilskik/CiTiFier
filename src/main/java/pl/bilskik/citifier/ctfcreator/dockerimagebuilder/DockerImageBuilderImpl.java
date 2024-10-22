package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
@Slf4j
public class DockerImageBuilderImpl implements DockerImageBuilder {

    private final DockerEnvironmentStrategy environmentStrategy;
    private final DockerShellProperties shellProperties;

    public void build(String filepath) {
        File file = new File(filepath);
        if(!file.exists()) {
            log.info("Repository on filepath: {} doesn't exist!", filepath);
            throw new DockerImageBuilderException("Cannot find repository");
        }

        boolean result = executeDockerComposeBuild(file);

        if(!result) {
            log.info("I can't build image! Image filepath: {}", filepath);
            throw new DockerImageBuilderException("Cannot execute docker build process properly!");
        }

        log.info("Image built successfully!");
    }

    private boolean executeDockerComposeBuild(File file) {
        log.info("Start building process executing docker-compose build");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(file);

        environmentStrategy.configure(processBuilder.environment());

        processBuilder.command(shellProperties.getShell(), shellProperties.getConfig(), shellProperties.getCommand());
        processBuilder.redirectErrorStream(true);

        return startProcess(processBuilder);
    }

    private boolean startProcess(ProcessBuilder processBuilder) {
        Process process = null;
        try {
            log.info("Process started");
            process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                log.info(line);
            }

            int exitCode = process.waitFor(); //TO DO define timeout
            log.info("Process finished with code: {}", exitCode);
            destroyProcessIfExist(process);
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            log.info("Error executing docker-compose build", e);
            destroyProcessIfExist(process);
            throw new DockerImageBuilderException("Cannot execute docker build process properly!");
        }
    }

    private void destroyProcessIfExist(Process process) {
        if(process != null && process.isAlive()) {
            process.destroy();
        }
    }

}
