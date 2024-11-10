package pl.bilskik.citifier.ctfcreator.app;

import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClient;

public class K8sTestUtils {

    private static final String RUNNING = "Running";

    public static void waitForPodsToBeReady(KubernetesClient client, String namespace, long timeoutMilis) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        boolean allReady = false;

        while((System.currentTimeMillis() - startTime) < timeoutMilis) {
            PodList podList = client.pods().inNamespace(namespace).list();

            allReady = podList.getItems().stream().allMatch(pod ->
                    RUNNING.equals(pod.getStatus().getPhase()) &&
                            pod.getStatus().getContainerStatuses().stream().allMatch(cs -> Boolean.TRUE.equals(cs.getReady()))
            );

            if (allReady) {
                return;
            }

            Thread.sleep(1000);
        }

        throw new RuntimeException("Timeout!");
    }

}
