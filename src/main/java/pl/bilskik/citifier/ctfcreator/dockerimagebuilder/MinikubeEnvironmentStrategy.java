package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public class MinikubeEnvironmentStrategy implements DockerEnvironmentStrategy {

    @Value("${kubernetess.docker-context.DOCKER_TLS_VERIFY}")
    private String DOCKER_TLS_VERIFY;

    @Value("${kubernetess.docker-context.DOCKER_HOST}")
    private String DOCKER_HOST;

    @Value("${kubernetess.docker-context.DOCKER_CERT_PATH}")
    private String DOCKER_CERT_PATH;

    @Value("${kubernetess.docker-context.MINIKUBE_ACTIVE_DOCKERD}")
    private String MINIKUBE_ACTIVE_DOCKERD;

    @Override
    public void configure(Map<String, String> env) {
        env.put("DOCKER_TLS_VERIFY", DOCKER_TLS_VERIFY);
        env.put("DOCKER_HOST", DOCKER_HOST);
        env.put("DOCKER_CERT_PATH", DOCKER_CERT_PATH);
        env.put("MINIKUBE_ACTIVE_DOCKERD", MINIKUBE_ACTIVE_DOCKERD);
    }
}
