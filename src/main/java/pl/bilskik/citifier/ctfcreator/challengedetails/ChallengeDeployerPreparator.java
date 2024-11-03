package pl.bilskik.citifier.ctfcreator.challengedetails;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.docker.entity.DockerCompose;
import pl.bilskik.citifier.ctfcreator.docker.service.DockerComposeParserManager;
import pl.bilskik.citifier.ctfcreator.dockerimagebuilder.DockerImageBuilder;
import pl.bilskik.citifier.ctfcreator.filemanager.FileManager;
import pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sResourceContext;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static pl.bilskik.citifier.ctfcreator.filemanager.FilePathBuilder.*;

@Service
@RequiredArgsConstructor
public class ChallengeDeployerPreparator {

    @Value("${repo.base-file-path}")
    private String baseFilePath;

    private final FileManager fileManager;
    private final DockerComposeParserManager dockerComposeParserManager;
    private final DockerImageBuilder dockerImageBuilder;

    public K8sResourceContext parseDockerComposeAndBuildImage(ChallengeDTO challengeDTO, boolean isNamespaceCreated) {
        String repoAbsolutePath = buildAbsolutePath(baseFilePath, challengeDTO.getRelativePathToRepo());
        String dockerComposeAbsolutePath = fileManager.provideDockerComposeAbsolutePath(repoAbsolutePath);

        DockerCompose compose = dockerComposeParserManager.parse(dockerComposeAbsolutePath);
        dockerImageBuilder.build(repoAbsolutePath);

        K8sResourceContext resourceContext = initK8sResourceContext(challengeDTO.getChallengeAppDataDTO(), repoAbsolutePath, isNamespaceCreated);
        resourceContext.setDockerCompose(compose);

        return resourceContext;
    }

    private K8sResourceContext initK8sResourceContext(ChallengeAppDataDTO appData, String fullRepoFilePath, boolean isNamespaceCreated) {
        return K8sResourceContext.builder()
                .appName(appData.getChallengeAppName())
                .isNamespaceCreated(isNamespaceCreated)
                .namespace(appData.getNamespace())
                .fullRepoFilePath(fullRepoFilePath)
                .startExposedPort(appData.getStartExposedPort())
                .numberOfApp(appData.getNumberOfApp())
                .build();
    }

}
