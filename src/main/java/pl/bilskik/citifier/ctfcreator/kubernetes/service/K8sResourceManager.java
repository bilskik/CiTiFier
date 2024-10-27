package pl.bilskik.citifier.ctfcreator.kubernetes.service;

import pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sResourceContext;

public interface K8sResourceManager {
    void deployAndStart(K8sResourceContext context);
    void start(K8sResourceContext context);
    void stop(String namespace);
    void delete(String namespace);
}
