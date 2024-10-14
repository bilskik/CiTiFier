package pl.bilskik.citifier.ctfcreator.kubernetes;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Qualifier;
import pl.bilskik.citifier.ctfcreator.docker.model.ComposeService;
import pl.bilskik.citifier.ctfcreator.docker.model.DockerCompose;
import pl.bilskik.citifier.ctfcreator.kubernetes.deployment.K8sDeploymentCreator;
import pl.bilskik.citifier.ctfcreator.kubernetes.service.K8sServiceCreator;
import pl.bilskik.citifier.ctfcreator.kubernetes.statefulset.K8sStatefulSetManager;

import java.util.Collections;
import java.util.Map;


@org.springframework.stereotype.Service
public class K8sDeploymentManager {

    private final static String DEFAULT_NAMESPACE = "default";
    private K8sClusterConnectorBuilder connectorBuilder;
    private K8sDeploymentCreator deploymentCreator;
    private K8sServiceCreator nodePortCreator;
    private K8sServiceCreator headlessCreator;
    private K8sStatefulSetManager statefulSetManager;

    public K8sDeploymentManager(
            K8sClusterConnectorBuilder connectorBuilder,
            K8sDeploymentCreator deploymentCreator,
            @Qualifier("nodePortService") K8sServiceCreator nodePortCreator,
            @Qualifier("headlessService") K8sServiceCreator headlessCreator,
            K8sStatefulSetManager statefulSetManager
    ) {
        this.connectorBuilder = connectorBuilder;
        this.deploymentCreator = deploymentCreator;
        this.nodePortCreator = nodePortCreator;
        this.headlessCreator = headlessCreator;
        this.statefulSetManager = statefulSetManager;
    }

    public void deploy(K8sDeploymentContext context) {
        DockerCompose dockerCompose = context.getDockerCompose();
        if(dockerCompose == null) {
            throw new K8sResourceCreationException("Docker compose cannot be null");
        }

        try (KubernetesClient client = connectorBuilder.buildClient()) {
            for(var entry: dockerCompose.getServices().entrySet()) {
                String serviceName = entry.getKey();
                ComposeService service = entry.getValue();

                if(serviceName.equalsIgnoreCase("db")) {
                    ;
                } else {

                }

            }
        }
    }

    private void deployDeployment(KubernetesClient client, ComposeService composeService, K8sDeploymentContext context) {
        Map<String, String> envMap = composeService.getEnvironments();

        for(int i=0; i<context.getDeploymentAmount(); i++) {
            Deployment deployment = deploymentCreator.createDeployment(
                    composeService.getContainerName() + "-" + i,
                    Collections.singletonMap("app", "deployment"),
                    Collections.singletonMap("app", "pod" + i),
                    composeService.getContainerName() + i,
                    composeService.getImage(),
                    envMap
            );

            Service service = nodePortCreator.createService(
                    "node-" + i + "-port-name",
                    Collections.singletonMap("app", "nodePort"),
                    Collections.singletonMap("app", "pod" + i),
                    8000 + i,
                    3443,
                    30001 + i
            );

            client.apps().deployments().inNamespace(DEFAULT_NAMESPACE).resource(deployment).create();
            client.services().inNamespace(DEFAULT_NAMESPACE).resource(service).create();
        }

//        Service headlessService = headlessCreator.createService(
//                "db",
//                Collections.singletonMap("app", "headlessService"),
//                Collections.singletonMap("app", "db"),
//                composeService.getPorts(),
//                5432,
//                null
//        );
//
//        client.services().inNamespace(DEFAULT_NAMESPACE).resource(headlessService).create();
    }

//
//    public void deploy() {
//        try (KubernetesClient client = connectorBuilder.buildClient()) {
//            statefulSetManager.deployStatefulSet(client);
//            for(int i=0; i<2; i++) {
//                Map<String, String> envMap = new HashMap<>();
//                envMap.put("SPRING_PROFILES_ACTIVE", "prod");
//
//                Deployment deployment = deploymentCreator.createDeployment(
//                        "deployment-" + i + "-name",
//                        Collections.singletonMap("app", "deployment"),
//                        Collections.singletonMap("app", "pod" + i),
//                        "note-app" + i,
//                        "secure-notes-app",
//                        envMap
//                );
//
//                Service service = nodePortCreator.createService(
//                        "node-" + i + "-port-name",
//                        Collections.singletonMap("app", "nodePort"),
//                        Collections.singletonMap("app", "pod" + i),
//                        8000 + i,
//                        3443,
//                        30001 + i
//                );
//
//                client.apps().deployments().inNamespace(DEFAULT_NAMESPACE).resource(deployment).create();
//                client.services().inNamespace(DEFAULT_NAMESPACE).resource(service).create();
//            }
//
//            Service headlessService = headlessCreator.createService(
//                    "db",
//                    Collections.singletonMap("app", "headlessService"),
//                    Collections.singletonMap("app", "db"),
//                    5432,
//                    5432,
//                    null
//            );
//
//            client.services().inNamespace(DEFAULT_NAMESPACE).resource(headlessService).create();
//        }
//    }
}
