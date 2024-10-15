package pl.bilskik.citifier.ctfcreator.kubernetes;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.util.StringUtils;
import pl.bilskik.citifier.ctfcreator.docker.model.ComposeService;
import pl.bilskik.citifier.ctfcreator.kubernetes.config.K8sSecretCreator;
import pl.bilskik.citifier.ctfcreator.kubernetes.service.K8sHeadlessServiceCreator;
import pl.bilskik.citifier.ctfcreator.kubernetes.statefulset.K8sStatefulSetCreator;
import pl.bilskik.citifier.ctfcreator.kubernetes.statefulset.volume.K8sVolumeCreator;
import pl.bilskik.citifier.ctfcreator.kubernetes.statefulset.volume.K8sVolumeMountCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceUtils.providePortToApplication;
import static pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceUtils.provideRandomCharacters;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class K8sDbDeployer {

    private final static String STATEFULSET_NAME = "statefulset";
    private final static String APP = "app";
    private final static String OPAQUE = "Opaque";
    private final static String HOST_PATH = "HOST_PATH";

    private final static String PASSWORD = "PASSWORD";

    private final K8sHeadlessServiceCreator headlessServiceCreator;
    private final K8sStatefulSetCreator statefulSetCreator;
    private final K8sSecretCreator secretCreator;
    private final K8sVolumeCreator volumeCreator;
    private final K8sVolumeMountCreator volumeMountCreator;

    public void deployDb(KubernetesClient client, K8sResourceContext context, ComposeService composeService) {

        Map<String, String> initialEnv = composeService.getEnvironments();

        String passwordKeyEnv = extractPasswordKey(composeService.getEnvironments());
        Map<String, String> env = extractNonPasswordEnv(initialEnv);

        boolean isSecretApplied = false;
        if(!StringUtils.isEmpty(passwordKeyEnv)) {
            Secret passwordSecret = createSecret(passwordKeyEnv, initialEnv.get(passwordKeyEnv));
            client.secrets().inNamespace(context.getNamespace()).resource(passwordSecret).create();
            isSecretApplied = true;
        }

        List<pl.bilskik.citifier.ctfcreator.docker.model.Volume> dockerVolumeList = composeService.getVolumes();

        List<Volume> volumeList = new ArrayList<>();
        List<VolumeMount> volumeMountList = new ArrayList<>();

        for(var volume : dockerVolumeList) {
            if(volume.isBindMount()) { //only bind mount supported
                Volume hostPathVolume = volumeCreator.createVolume(
                        HOST_PATH,
                        "volume-script-mount",
                        null,
                        volume.getHostPath()
                );

                VolumeMount volumeMount = volumeMountCreator.createVolumeMount(
                  "init-script",
                        volume.getContainerPath()
                );

                volumeList.add(hostPathVolume);
                volumeMountList.add(volumeMount);
            }
        }

        StatefulSet statefulSet = statefulSetCreator.createStatefulSet(
                provideStatefulSetName(),
                Collections.singletonMap(APP, "postgres-statefulset"),
                Collections.singletonMap(APP, "db"),
                composeService.getContainerName(),
                composeService.getImage(),
                env,
                "",
                isSecretApplied ? "postgres-secret" : "",
                volumeList,
                volumeMountList
        );

        Service headlessService = headlessServiceCreator.createService(
                "db",
                Collections.singletonMap("app", "headlessService"),
                Collections.singletonMap("app", "db"),
                providePortToApplication(composeService.getPorts()),
                providePortToApplication(composeService.getPorts()),
                null
        );

        client.apps().statefulSets().inNamespace(context.getNamespace()).resource(statefulSet).create();
        client.services().inNamespace(context.getNamespace()).resource(headlessService).create();
    }

    private Secret createSecret(String passwordKey, String passwordValue) {
        return secretCreator.createSecret(
                "postgres-secret",
                Collections.singletonMap("app", "secret"),
                OPAQUE,
                Collections.singletonMap(passwordKey, passwordValue)
        );
    }

    private Map<String, String> extractNonPasswordEnv(Map<String, String> environments) {
        return environments.entrySet().stream()
                .filter((entry) -> !entry.getKey().contains(PASSWORD))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    private String extractPasswordKey(Map<String, String> environments) {
        return environments.keySet().stream()
                .filter((entry) -> entry.contains(PASSWORD))
                .toList()
                .getFirst();
    }

    private String provideStatefulSetName() {
        return STATEFULSET_NAME + "-" + provideRandomCharacters();
    }

}
