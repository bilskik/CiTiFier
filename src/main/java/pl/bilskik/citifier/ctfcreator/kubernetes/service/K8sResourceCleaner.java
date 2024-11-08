package pl.bilskik.citifier.ctfcreator.kubernetes.service;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Service;

import static pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sConstants.*;

@Service
public class K8sResourceCleaner {

    public void deleteNamespaceWithResources(KubernetesClient client, String namespace) {
        client.namespaces().withName(namespace).delete();
    }

    public void deleteResources(KubernetesClient client, String namespace) {
        client.services().inNamespace(namespace).withLabel(APP, NODE_PORT_LABEL).delete();
        client.services().inNamespace(namespace).withLabel(APP, HEADLESS_SERVICE_LABEL).delete();
        client.apps().deployments().inNamespace(namespace).withLabel(APP, DEPLOYMENT_LABEL).delete();
        client.apps().statefulSets().inNamespace(namespace).withLabel(APP, STATEFUL_SET_LABEL).delete();
        client.secrets().inNamespace(namespace).withLabel(APP, SECRET_LABEL).delete();
        client.configMaps().inNamespace(namespace).withLabel(APP, CONFIG_MAP_LABEL).delete();
    }

}
