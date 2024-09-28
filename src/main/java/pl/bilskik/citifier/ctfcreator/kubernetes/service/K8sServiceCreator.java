package pl.bilskik.citifier.ctfcreator.kubernetes.service;

import io.fabric8.kubernetes.api.model.Service;

import java.util.Map;

public interface K8sServiceCreator {
    Service createService(
        String serviceName,
        Map<String, String> serviceLabel,
        Map<String, String> servicePodSelector,
        Integer port,
        Integer targetPort,
        Integer nodePort
    );
}
