package pl.bilskik.citifier.ctfcreator.kubernetes.service;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pl.bilskik.citifier.ctfcreator.docker.entity.ComposeService;
import pl.bilskik.citifier.ctfcreator.docker.entity.Port;
import pl.bilskik.citifier.ctfcreator.kubernetes.context.K8sResourceContext;
import pl.bilskik.citifier.ctfcreator.kubernetes.factory.deployment.K8sDeploymentFactory;
import pl.bilskik.citifier.ctfcreator.kubernetes.factory.service.K8sNodePortFactory;
import pl.bilskik.citifier.ctfcreator.kubernetes.exception.K8sResourceCreationException;

import java.util.Collections;
import java.util.Map;

import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sDeployerUtils.provideRandomCharacters;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
public class K8sAppDeployer {

    private static final String APP = "app";
    private static final String DEPLOYMENT_NAME = "deployment";
    private static final String SERVICE_NAME = "service";

    private static final int INTERNAL_SERVICE_PORT = 3998;

    private final K8sDeploymentFactory deploymentCreator;
    private final K8sNodePortFactory nodePortCreator;

    public void deployApp(KubernetesClient client, K8sResourceContext context, ComposeService composeService) {
        String namespace = context.getNamespace();
        Map<String, String> envMap = composeService.getEnvironments();
        Port port = composeService.getPorts().getFirst();

        for(int i=0; i<context.getNumberOfApp(); i++) {
            Deployment deployment = deploymentCreator.createDeployment(
                    buildDeploymentName(i),
                    Collections.singletonMap(APP, context.getDeploymentLabel()),
                    Collections.singletonMap(APP, context.getDeploymentLabel() + i),
                    composeService.getContainerName() + i,
                    composeService.getImage(),
                    parsePort(port.getTargetPort()),
                    envMap
            );

            Service service = nodePortCreator.createService(
                    buildServiceName(i),
                    Collections.singletonMap(APP, context.getServiceLabel()),
                    Collections.singletonMap(APP, context.getDeploymentLabel() + i),
                    parsePort(port.getHostPort()) + i,
                    INTERNAL_SERVICE_PORT,
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

    private int parsePort(String port) {
        if(StringUtils.isNumeric(port)) {
            return Integer.parseInt(port);
        }

        throw new K8sResourceCreationException(String.format("Cannot parse port! Invalid port: %s", port));
    }
}
