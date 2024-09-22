package pl.bilskik.citifier.ctfcreator.kubernetes;

import io.fabric8.kubernetes.api.model.Service;

import java.util.Map;

public interface K8sServiceCreator {
    Service createService(
        String nodePortName,
        Map<String, String> nodePortLabel,
        Map<String, String> podSelectorLabel,
        int port,
        int targetPort,
        int nodePort
    );
}
