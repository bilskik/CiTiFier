package pl.bilskik.citifier.ctfcreator.kubernetes;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;

import java.util.Map;

@org.springframework.stereotype.Service
public class K8sNodePortCreator implements K8sServiceCreator {

    private final static String API_VERSION = "v1";
    private final static String NODE_PORT = "NodePort";
    private final static String APP_PROTOCOL = "HTTP";

    @Override
    public Service createService(
            String nodePortName,
            Map<String, String> nodePortLabel,
            Map<String, String> podSelectorLabel,
            int port,
            int targetPort,
            int nodePort
    ) {
        return new ServiceBuilder()
                .withApiVersion(API_VERSION)
                .withNewMetadata()
                    .withName(nodePortName)
                    .withLabels(nodePortLabel)
                .endMetadata()
                .withNewSpec()
                    .withType(NODE_PORT)
                    .withSelector(podSelectorLabel)
                    .addNewPort()
                        .withPort(port)
                        .withTargetPort(new IntOrString(targetPort))
                        .withNodePort(nodePort)
                        .withAppProtocol(APP_PROTOCOL)
                    .endPort()
                .endSpec()
                .build();
    }
}
