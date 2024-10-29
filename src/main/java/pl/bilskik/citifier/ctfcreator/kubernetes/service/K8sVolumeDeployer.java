package pl.bilskik.citifier.ctfcreator.kubernetes.service;

import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sResourceContext;
import pl.bilskik.citifier.ctfcreator.kubernetes.factory.statefulset.volume.K8sVolumeFactory;
import pl.bilskik.citifier.ctfcreator.kubernetes.factory.statefulset.volume.K8sVolumeMountFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class K8sVolumeDeployer {

    private final static String HOST_PATH = "HOST_PATH";
    private final static String VOLUME_NAME = "init-script";

    private final K8sVolumeFactory volumeFactory;
    private final K8sVolumeMountFactory volumeMountFactory;

    public List<Volume> createVolumes(K8sResourceContext context, List<pl.bilskik.citifier.ctfcreator.docker.entity.Volume> dockerVolumes) {
        return dockerVolumes.stream()
                .filter(volume -> volume.isBindMount()) //TO DO only bind mount supported
                .map(volume -> volumeFactory.createVolume(
                        HOST_PATH,
                        VOLUME_NAME,
                        null,
                        provideFullPath(context.getFullRepoFilePath(), volume.getHostPath())
                ))
                .collect(Collectors.toList());
    }

    public List<VolumeMount> createVolumeMounts(List<pl.bilskik.citifier.ctfcreator.docker.entity.Volume> dockerVolumes) {
        return dockerVolumes.stream()
                .filter(volume -> volume.isBindMount())
                .map(volume -> volumeMountFactory.createVolumeMount(
                        VOLUME_NAME,
                        volume.getContainerPath()
                ))
                .collect(Collectors.toList());
    }

    private String provideFullPath(String fullRepoFilePath, String hostPath) {
        if(hostPath.startsWith(".")) {
            if(hostPath.contains("/")) {
                hostPath = hostPath.replace("/", "\\");
            }
            hostPath = fullRepoFilePath + hostPath.substring(1);
            return "/home/docker/postgres";
        }
        return "";
    }
}
