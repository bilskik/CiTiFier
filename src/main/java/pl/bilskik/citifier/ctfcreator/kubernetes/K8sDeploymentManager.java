package pl.bilskik.citifier.ctfcreator.kubernetes;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;

import java.util.Collections;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class K8sDeploymentManager {

    private final static String NAMESPACE = "default";
    private final K8sClusterConnectorBuilder connectorBuilder;
    private final K8sDeploymentCreator deploymentCreator;
    private final K8sServiceCreator nodePortCreator;

    public void deploy() {
        try (KubernetesClient client = connectorBuilder.buildClient()) {
            for(int i=0; i<5; i++) {
                Deployment deployment = deploymentCreator.createDeployment(
                        "deployment-" + i + "-name",
                        Collections.singletonMap("app", "deployment"),
                        Collections.singletonMap("app", "pod" + i),
                        "nginx" + i,
                        "nginx"
                );

                Service service = nodePortCreator.createService(
                        "node-" + i + "-port-name",
                        Collections.singletonMap("app", "nodePort"),
                        Collections.singletonMap("app", "pod" + i),
                        8000 + i,
                        80,
                        30001 + i
                );

                client.apps().deployments().inNamespace(NAMESPACE).resource(deployment).create();
                client.services().inNamespace(NAMESPACE).resource(service).create();
            }
        }
    }
}
