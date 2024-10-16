package pl.bilskik.citifier.ctfcreator.kubernetes;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import pl.bilskik.citifier.ctfcreator.docker.model.ComposeService;
import pl.bilskik.citifier.ctfcreator.kubernetes.deployment.K8sDeploymentCreator;
import pl.bilskik.citifier.ctfcreator.kubernetes.service.K8sHeadlessServiceCreator;
import pl.bilskik.citifier.ctfcreator.kubernetes.service.K8sNodePortCreator;

import java.util.Collections;
import java.util.Map;

import static pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceUtils.providePortToApplication;
import static pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceUtils.provideRandomCharacters;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class K8sAppDeployer {


    private static final String APP = "app";
    private static final String DEPLOYMENT_NAME = "deployment";
    private static final String SERVICE_NAME = "service";


    private final K8sDeploymentCreator deploymentCreator;
    private final K8sNodePortCreator nodePortCreator;
    private final K8sHeadlessServiceCreator headlessServiceCreator;

    public void deployApp(KubernetesClient client, K8sResourceContext context, ComposeService composeService) {
        String namespace = context.getNamespace();
        Map<String, String> envMap = composeService.getEnvironments();

        for(int i=0; i<context.getNumberOfApp(); i++) {
            Deployment deployment = deploymentCreator.createDeployment(
                    buildDeploymentName(i),
                    Collections.singletonMap(APP, context.getDeploymentLabel()),
                    Collections.singletonMap(APP, context.getDeploymentLabel() + i),
                    composeService.getContainerName() + i,
                    composeService.getImage(),
                    envMap
            );

            Service service = nodePortCreator.createService(
                    buildServiceName(i),
                    Collections.singletonMap(APP, context.getServiceLabel()),
                    Collections.singletonMap(APP, context.getDeploymentLabel() + i),
                    3344,
                    providePortToApplication(composeService.getPorts()) + i,
                    context.getStartExposedPort() + i
            );

            client.apps().deployments().inNamespace(namespace).resource(deployment).create();
            client.services().inNamespace(namespace).resource(service).create();
        }

    }
    private String buildDeploymentName(int i) {
        return DEPLOYMENT_NAME + "-" + provideRandomCharacters() + "-" + i;
    }

    private String buildServiceName(int i) {
        return SERVICE_NAME + "-" + provideRandomCharacters() + "-" + i;
    }
}
