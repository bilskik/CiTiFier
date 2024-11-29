package pl.bilskik.citifier.core.kubernetes.factory.service;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;

import java.util.Map;

@org.springframework.stereotype.Service("headlessService")
public class K8sHeadlessServiceFactory implements K8sServiceFactory {

    private final static String API_VERSION = "v1";
    private final static String NONE = "None";
    private final static String APP_PROTOCOL = "HTTP";

    @Override
    public Service createService(
            String serviceName,
            Map<String, String> serviceLabel,
            Map<String, String> servicePodSelector,
            Integer port,
            Integer targetPort,
            Integer nodePort
    ) {
        return new ServiceBuilder()
                .withApiVersion(API_VERSION)
                .withNewMetadata()
                    .withName(serviceName)
                    .withLabels(serviceLabel)
                .endMetadata()
                .withNewSpec()
                    .withClusterIP(NONE)
                    .withSelector(servicePodSelector)
                    .addNewPort()
                        .withPort(port)
                        .withTargetPort(new IntOrString(targetPort))
                        .withAppProtocol(APP_PROTOCOL)
                    .endPort()
                .endSpec()
                .build();
    }
}
