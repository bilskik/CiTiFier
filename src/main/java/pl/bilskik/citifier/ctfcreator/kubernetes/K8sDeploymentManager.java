package pl.bilskik.citifier.ctfcreator.kubernetes;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;

import java.util.Collections;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class K8sDeploymentManager {

    private final static String DEFAULT_NAMESPACE = "default";
    private final K8sClusterConnectorBuilder connectorBuilder;
    private final K8sDeploymentCreator deploymentCreator;
    private final K8sServiceCreator nodePortCreator;
    private final K8sStatefulSetCreator statefulSetCreator;

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

                client.apps().deployments().inNamespace(DEFAULT_NAMESPACE).resource(deployment).create();
                client.services().inNamespace(DEFAULT_NAMESPACE).resource(service).create();
            }

            StatefulSet statefulSet = statefulSetCreator.createStatefulSet(
                    "statefulset-name",
                    Collections.singletonMap("app", "statefulset"),
                    Collections.singletonMap("app", "db"),
                    "nginx",
                    "nginx",
                    "/cache",
                    "nginx-cache"
            );

            client.apps().statefulSets().inNamespace(DEFAULT_NAMESPACE).resource(statefulSet).create();
        }
    }
}
