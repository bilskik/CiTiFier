package pl.bilskik.citifier.ctfcreator.kubernetes.service;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pl.bilskik.citifier.ctfcreator.docker.entity.ComposeService;
import pl.bilskik.citifier.ctfcreator.docker.entity.Port;
import pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sResourceContext;
import pl.bilskik.citifier.ctfcreator.kubernetes.exception.K8sResourceCreationException;
import pl.bilskik.citifier.ctfcreator.kubernetes.factory.deployment.K8sDeploymentFactory;
import pl.bilskik.citifier.ctfcreator.kubernetes.factory.service.K8sNodePortFactory;

import java.util.Collections;
import java.util.Map;

import static pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sConstants.*;
import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sDeployerUtils.buildName;
import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sDeployerUtils.createEnvForSpecificDeployment;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
public class K8sAppDeployer {

    private static final String DEPLOYMENT_NAME = "deployment";
    private static final String SERVICE_NAME = "service";

    private final K8sDeploymentFactory deploymentCreator;
    private final K8sNodePortFactory nodePortCreator;

    public void deployApp(KubernetesClient client, K8sResourceContext context, ComposeService composeService) {
        String namespace = context.getNamespace();
        Port port = composeService.getPorts().getFirst();

        for(int i=0; i<context.getNumberOfApp(); i++) {
            String flag = context.getPortFlag().get(context.getStartExposedPort() + i);
            Map<String, String> envMap = createEnvForSpecificDeployment(composeService.getEnvironments(), flag, i);

            Deployment deployment = deploymentCreator.createDeployment(
                    buildName(DEPLOYMENT_NAME, i),
                    Collections.singletonMap(APP, DEPLOYMENT_LABEL),
                    Collections.singletonMap(APP, POD_LABEL + "-" + i),
                    composeService.getContainerName() + i,
                    composeService.getImage(),
                    parsePort(port.getTargetPort()),
                    envMap
            );

            Service service = nodePortCreator.createService(
                    buildName(SERVICE_NAME, i),
                    Collections.singletonMap(APP, NODE_PORT_LABEL),
                    Collections.singletonMap(APP, POD_LABEL + "-" + i),
                    parsePort(port.getHostPort()),
                    parsePort(port.getTargetPort()),
                    context.getStartExposedPort() + i
            );

            client.apps().deployments().inNamespace(namespace).resource(deployment).create();
            client.services().inNamespace(namespace).resource(service).create();
        }

    }

    private int parsePort(String port) {
        if(StringUtils.isNumeric(port)) {
            return Integer.parseInt(port);
        }

        throw new K8sResourceCreationException(String.format("Cannot parse port! Invalid port: %s", port));
    }
}
