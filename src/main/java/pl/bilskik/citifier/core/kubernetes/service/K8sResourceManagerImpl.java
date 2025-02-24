package pl.bilskik.citifier.core.kubernetes.service;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.core.docker.entity.ComposeService;
import pl.bilskik.citifier.core.docker.entity.DockerCompose;
import pl.bilskik.citifier.core.kubernetes.config.K8sClusterConnectorBuilder;
import pl.bilskik.citifier.core.kubernetes.data.K8sResourceContext;
import pl.bilskik.citifier.core.kubernetes.exception.K8sResourceCreationException;
import pl.bilskik.citifier.core.kubernetes.factory.config.K8sNamespaceFactory;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.bilskik.citifier.core.kubernetes.data.K8sConstants.DB_SERVICE_NAME;


@Service
@RequiredArgsConstructor
@Slf4j
public class K8sResourceManagerImpl implements K8sResourceManager {

    private final K8sClusterConnectorBuilder connectorBuilder;
    private final K8sNamespaceFactory namespaceCreator;
    private final K8sAppDeployer appDeployer;
    private final K8sDbDeployer dbDeployer;
    private final K8sResourceCleaner resourceCleaner;

    @Override
    public void deployAndStart(K8sResourceContext context) {
        deploy(context);
    }

    @Override
    public void start(K8sResourceContext context) {
        deploy(context);
    }

    private void deploy(K8sResourceContext context) {
        String namespace = context.getNamespace();
        DockerCompose dockerCompose = context.getDockerCompose();
        if(dockerCompose == null) {
            log.error("Cannot deploy app! DockerCompose is null!");
            throw new K8sResourceCreationException("Docker compose cannot be null");
        }

        log.info("Start deploying app");

        KubernetesClient client = null;
        try {
            client = connectorBuilder.buildClient();
            if(!context.isNamespaceCreated()) {
                Namespace namespaceResource = namespaceCreator.create(namespace);
                client.namespaces().resource(namespaceResource).create();
            }

            String dbContainerName = retrieveDbContainerName(dockerCompose.getServices().entrySet());
            for(var entry: dockerCompose.getServices().entrySet()) {
                String serviceName = entry.getKey();
                ComposeService service = entry.getValue();

                if(serviceName.equalsIgnoreCase(DB_SERVICE_NAME)) {
                    log.info("Deploying database: {}", serviceName);
                    dbDeployer.deployDb(client, context, service);
                } else {
                    log.info("Deploying app: {}", serviceName);
                    appDeployer.deployApp(client, context, service, dbContainerName);
                }
            }
        } catch(Exception e) {
            log.info("Cannot deploy app! I am cleaning resources, Reason: {}", e.getMessage());
            if(client != null) {
                resourceCleaner.deleteNamespaceWithResources(client, namespace);
            }
            throw new K8sResourceCreationException("Nie można prawidłowo wdrożyć zadania!");
        }
    }

    private String retrieveDbContainerName(Set<Map.Entry<String, ComposeService>> entries) {
        return entries.stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(DB_SERVICE_NAME))
                .map(entry -> entry.getValue().getContainerName())
                .findFirst()
                .orElse(null);
    }

    @Override
    public void stop(String namespace) {
        log.info("Stopping all resources in namespace: {}", namespace);
        try (KubernetesClient client = connectorBuilder.buildClient()) {
            resourceCleaner.deleteResources(client, namespace);
        }
        log.info("All resources within namespace: {} stopped", namespace);
    }

    @Override
    public void delete(String namespace) {
        log.info("Start undeploying resources in namespace: {}", namespace);
        try (KubernetesClient client = connectorBuilder.buildClient()) {
            resourceCleaner.deleteNamespaceWithResources(client, namespace);
        }
        log.info("Resources in namespace: {} deleted", namespace);
    }
}
