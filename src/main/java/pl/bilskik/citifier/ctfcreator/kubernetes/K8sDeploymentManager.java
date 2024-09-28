package pl.bilskik.citifier.ctfcreator.kubernetes;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@org.springframework.stereotype.Service
public class K8sDeploymentManager {

    private final static String DEFAULT_NAMESPACE = "default";
    private K8sClusterConnectorBuilder connectorBuilder;
    private K8sDeploymentCreator deploymentCreator;
    private K8sServiceCreator nodePortCreator;
    private K8sServiceCreator headlessCreator;
    private K8sStatefulSetCreator statefulSetCreator;
    private K8sConfigMapCreator configMapCreator;

    public K8sDeploymentManager(
            K8sClusterConnectorBuilder connectorBuilder,
            K8sDeploymentCreator deploymentCreator,
            @Qualifier("nodePortService") K8sServiceCreator nodePortCreator,
            @Qualifier("headlessService") K8sServiceCreator headlessCreator,
            K8sStatefulSetCreator statefulSetCreator,
            K8sConfigMapCreator configMapCreator
    ) {
        this.connectorBuilder = connectorBuilder;
        this.deploymentCreator = deploymentCreator;
        this.nodePortCreator = nodePortCreator;
        this.headlessCreator = headlessCreator;
        this.statefulSetCreator = statefulSetCreator;
        this.configMapCreator = configMapCreator;
    }

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

            Map<String, String> env = new HashMap<>();
            env.put("POSTGRES_USER", "user");
            env.put("POSTGRES_PASSWORD", "password");
            env.put("POSTGRES_DB", "mydb");

            ConfigMap configMap = configMapCreator.createConfigMap(
              "postgres-config-map",
                Collections.singletonMap("app", "configMap"),
                env
            );



            StatefulSet statefulSet = statefulSetCreator.createStatefulSet(
                    "postgres-statefulset",
                    Collections.singletonMap("app", "postgres-statefulset"),
                    Collections.singletonMap("app", "db"),
                    "postgres",
                    "postgres",
                    "postgres-config-map",
                    "/cache",
                    "postgres-cache"
            );


            Service headlessService = headlessCreator.createService(
                    "headless-service",
                    Collections.singletonMap("app", "headlessService"),
                    Collections.singletonMap("app", "db"),
                    9000,
                    5432,
                    null
            );

            client.configMaps().inNamespace(DEFAULT_NAMESPACE).resource(configMap).create();
            client.apps().statefulSets().inNamespace(DEFAULT_NAMESPACE).resource(statefulSet).create();
            client.services().inNamespace(DEFAULT_NAMESPACE).resource(headlessService).create();
        }
    }
}
