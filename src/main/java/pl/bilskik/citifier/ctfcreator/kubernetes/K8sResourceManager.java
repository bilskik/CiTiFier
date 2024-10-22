package pl.bilskik.citifier.ctfcreator.kubernetes;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.bilskik.citifier.ctfcreator.docker.model.ComposeService;
import pl.bilskik.citifier.ctfcreator.docker.model.DockerCompose;
import pl.bilskik.citifier.ctfcreator.kubernetes.config.K8sNamespaceCreator;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
public class K8sResourceManager {

    private final K8sClusterConnectorBuilder connectorBuilder;
    private final K8sNamespaceCreator namespaceCreator;
    private final K8sAppDeployer appDeployer;
    private final K8sDbDeployer dbDeployer;

    public void deploy(K8sResourceContext context) {
        DockerCompose dockerCompose = context.getDockerCompose();
        if(dockerCompose == null) {
            log.info("DockerCompose object is null!");
            throw new K8sResourceCreationException("Docker compose cannot be null");
        }

        log.info("Start deploying app...");

        try (KubernetesClient client = connectorBuilder.buildClient()) {
            Namespace namespace = namespaceCreator.create(context.getNamespace());
            client.namespaces().resource(namespace).create();

            for(var entry: dockerCompose.getServices().entrySet()) {
                String serviceName = entry.getKey();
                ComposeService service = entry.getValue();

                if(serviceName.equalsIgnoreCase("postgres")) {
                    log.info("Deploying database: {}", serviceName);
                    dbDeployer.deployDb(client, context, service);
                } else {
                    log.info("Deploying app: {}", serviceName);
                    appDeployer.deployApp(client, context, service);
                }

            }
        }
    }
}
