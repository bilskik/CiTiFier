package pl.bilskik.citifier.ctfcreator.app;

import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.PodCondition;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.PodStatus;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sConstants.POD_LABEL;

public class K8sTestUtils {

    private static final String RUNNING = "Running";
    private static final Logger log = LoggerFactory.getLogger(K8sTestUtils.class);

    public static boolean allPodsReady(KubernetesClient client, String namespace, long timeoutMilis) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        boolean allReady = false;

        Thread.sleep(10000);

        while((System.currentTimeMillis() - startTime) < timeoutMilis) {
            PodList podList = client.pods().inNamespace(namespace).list();

            log.info("CHECK!");
            String podLabel0 = POD_LABEL + "-" + 0;
            String podLabel1 = POD_LABEL + "-" + 1;

//            client.pods().resources().forEach(p -> {
//                log.info(p.getLog());
//            });
            podList.getItems().forEach(pod -> {
                String podName = pod.getMetadata().getName();
                String phase = pod.getStatus().getPhase();
                log.info("Pod: {} | Phase: {}", podName, phase);

                if (!"Running".equalsIgnoreCase(phase)) {
                    log.info("Reason: {}", pod.getStatus().getReason());
                    log.info("Message: {}", pod.getStatus().getMessage());
                }

                PodStatus status = pod.getStatus();
                if (status.getConditions() != null) {
                    for (PodCondition condition : status.getConditions()) {
                        log.info("Condition Type: {}", condition.getType());
                        log.info("Status: {}", condition.getStatus());
                        log.info("Reason: {}", condition.getReason());
                        log.info("Message: {}", condition.getMessage());
                    }
                }

                List<Event> events = client.v1().events().inNamespace(namespace).list().getItems();
                for (Event event : events) {
                    if (event.getInvolvedObject().getName().equals(podName)) {
                        log.info("Event Reason: {}", event.getReason());
                        log.info("Event Message: {}", event.getMessage());
                    }
                }
            });

//            client.pods().inNamespace(namespace).withLabel(podLabel0).resources().forEach(p -> {
//                log.info(p.getLog());
//            });
//            client.pods().inNamespace(namespace).withLabel(podLabel1).resources().forEach(p -> {
//                log.info(p.getLog());
//            });


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
