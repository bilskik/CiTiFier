package pl.bilskik.citifier.core.app;

import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class K8sTestUtils {

    private static final String RUNNING = "Running";
    private static final Logger log = LoggerFactory.getLogger(K8sTestUtils.class);

    public static boolean allPodsReady(KubernetesClient client, String namespace, long timeoutMilis) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        boolean allReady = false;

        Thread.sleep(10000);

        while((System.currentTimeMillis() - startTime) < timeoutMilis) {
            PodList podList = client.pods().inNamespace(namespace).list();

            allReady = podList.getItems().stream().allMatch(pod ->
                    RUNNING.equals(pod.getStatus().getPhase()) &&
                            pod.getStatus().getContainerStatuses().stream().allMatch(cs -> Boolean.TRUE.equals(cs.getReady()))
            );

            if (allReady) {
                return true;
            }

            Thread.sleep(1000);
        }
        return false; //timeout
    }
}
