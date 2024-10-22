package pl.bilskik.citifier.ctfcreator.dockebuilder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class BuildDockerContainers {

    private final static String REPOSITORY_NOT_EXIST = "Cannot find repository";
    private final static String CANNOT_BUILD_DOCKER = "Cannot execute docker build process properly!";
    private final static String DOCKER_COMPOSE_BUILD = "docker-compose build";

    @Value("${kubernetess.docker-context.minikube-context}")
    private Boolean minikubeContext;

    @Value("${kubernetess.docker-context.DOCKER_TLS_VERIFY}")
    private String DOCKER_TLS_VERIFY;

    @Value("${kubernetess.docker-context.DOCKER_HOST}")
    private String DOCKER_HOST;

    @Value("${kubernetess.docker-context.DOCKER_CERT_PATH}")
    private String DOCKER_CERT_PATH;

    @Value("${kubernetess.docker-context.MINIKUBE_ACTIVE_DOCKERD}")
    private String MINIKUBE_ACTIVE_DOCKERD;

    @Value("${docker.build.shell.name}")
    private String shell;

    @Value("${docker.build.shell.config}")
    private String shellOptions;

    public void build(String filepath) {
        File file = new File(filepath);
        if(!file.exists()) {
            log.info("Repository on filepath: {} doesn't exist!", filepath);
            throw new BuilderDockerException(REPOSITORY_NOT_EXIST);
        }

        boolean result = executeDockerComposeBuild(file);

        if(!result) {
            log.info("Cannot build process properly!");
            throw new BuilderDockerException(CANNOT_BUILD_DOCKER);
        }

        log.info("Containers build properly!");
    }

    private boolean executeDockerComposeBuild(File file) {
        log.info("Start building process executing docker-compose build");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(file);
        if(minikubeContext != null && minikubeContext) {
            Map<String, String> env = processBuilder.environment();
            buildEnv(env);
        }
        processBuilder.command(shell, shellOptions, DOCKER_COMPOSE_BUILD);
        processBuilder.redirectErrorStream(true);

        Process process = null;
        try {
            log.info("Process started");
            process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                log.info(line);
            }

            int exitCode = process.waitFor();
            log.info("Process finished with code: {}", exitCode);
            destroyProcessIfExist(process);
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            destroyProcessIfExist(process);
            log.info("An error has occurred during process! Stack Trace: {}", e.getMessage());
            throw new BuilderDockerException(CANNOT_BUILD_DOCKER);
        }
    }

    private Map<String, String> buildEnv(Map<String, String> env) {
        env.put("DOCKER_TLS_VERIFY", DOCKER_TLS_VERIFY);
        env.put("DOCKER_HOST", DOCKER_HOST);
        env.put("DOCKER_CERT_PATH", DOCKER_CERT_PATH);
        env.put("MINIKUBE_ACTIVE_DOCKERD", MINIKUBE_ACTIVE_DOCKERD);
        return env;
    }

    private void destroyProcessIfExist(Process process) {
        if(process != null && process.isAlive()) {
            process.destroy();
        }
    }

}
