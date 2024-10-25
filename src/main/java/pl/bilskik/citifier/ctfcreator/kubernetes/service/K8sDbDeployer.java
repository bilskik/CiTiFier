package pl.bilskik.citifier.ctfcreator.kubernetes.service;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import pl.bilskik.citifier.ctfcreator.docker.entity.ComposeService;
import pl.bilskik.citifier.ctfcreator.kubernetes.context.K8sResourceContext;
import pl.bilskik.citifier.ctfcreator.kubernetes.factory.service.K8sHeadlessServiceFactory;
import pl.bilskik.citifier.ctfcreator.kubernetes.factory.statefulset.K8sStatefulSetFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sDeployerUtils.providePortToApplication;
import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sDeployerUtils.provideRandomCharacters;
import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sEnvironmentExtractor.extractNonPasswordEnv;
import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sEnvironmentExtractor.extractPasswordEnv;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class K8sDbDeployer {

    private final static String STATEFULSET_NAME = "statefulset";
    private final static String APP = "app";

    private final K8sHeadlessServiceFactory headlessServiceFactory;
    private final K8sStatefulSetFactory statefulSetFactory;

    private final K8sSecretDeployer secretDeployer;
    private final K8sVolumeDeployer volumeDeployer;

    public void deployDb(
            KubernetesClient client,
            K8sResourceContext context,
            ComposeService composeService
    ) {
        Map<String, String> passwordEnv = extractPasswordEnv(composeService.getEnvironments());
        Map<String, String> env = extractNonPasswordEnv(composeService.getEnvironments());

        boolean isSecretApplied = secretDeployer.applySecret(client, context, passwordEnv);

        List<Volume> volumeList = volumeDeployer.createVolumes(context, composeService.getVolumes());
        List<VolumeMount> volumeMountList = volumeDeployer.createVolumeMounts(composeService.getVolumes());

        StatefulSet statefulSet = statefulSetFactory.createStatefulSet(
                provideStatefulSetName(),
                Collections.singletonMap(APP, "postgresStatefulset"),
                Collections.singletonMap(APP, "db"),
                composeService.getContainerName(),
                composeService.getImage(),
                env,
                "",
                isSecretApplied ? "postgres-secret" : "",
                volumeList,
                volumeMountList
        );

        Service headlessService = headlessServiceFactory.createService(
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

    private String provideStatefulSetName() {
        return STATEFULSET_NAME + "-" + provideRandomCharacters();
    }

}
