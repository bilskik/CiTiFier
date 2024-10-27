package pl.bilskik.citifier.ctfcreator.kubernetes.service;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import pl.bilskik.citifier.ctfcreator.docker.entity.ComposeService;
import pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sResourceContext;
import pl.bilskik.citifier.ctfcreator.kubernetes.factory.service.K8sHeadlessServiceFactory;
import pl.bilskik.citifier.ctfcreator.kubernetes.factory.statefulset.K8sStatefulSetFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sLabelConstants.*;
import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sDeployerUtils.*;
import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sEnvironmentExtractor.extractNonPasswordEnv;
import static pl.bilskik.citifier.ctfcreator.kubernetes.util.K8sEnvironmentExtractor.extractPasswordEnv;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class K8sDbDeployer {

    private final static String STATEFULSET_NAME = "statefulset";
    private final static String DB = "db";
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
                buildName(STATEFULSET_NAME),
                Collections.singletonMap(APP, STATEFUL_SET_LABEL),
                Collections.singletonMap(APP, DB_LABEL),
                composeService.getContainerName(),
                composeService.getImage(),
                env,
                "",
                isSecretApplied ? "secret" : "",
                volumeList,
                volumeMountList
        );

        Service headlessService = headlessServiceFactory.createService(
                DB,
                Collections.singletonMap(APP, HEADLESS_SERVICE_LABEL),
                Collections.singletonMap(APP, DB_LABEL),
                providePortToApplication(composeService.getPorts()),
                providePortToApplication(composeService.getPorts()),
                null
        );

        client.apps().statefulSets().inNamespace(context.getNamespace()).resource(statefulSet).create();
        client.services().inNamespace(context.getNamespace()).resource(headlessService).create();
    }

}
