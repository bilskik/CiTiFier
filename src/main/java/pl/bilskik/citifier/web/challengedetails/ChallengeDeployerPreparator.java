package pl.bilskik.citifier.web.challengedetails;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.core.docker.entity.DockerCompose;
import pl.bilskik.citifier.core.docker.service.DockerComposeParserManager;
import pl.bilskik.citifier.core.dockerimagebuilder.DockerImageBuilder;
import pl.bilskik.citifier.core.filemanager.FileManager;
import pl.bilskik.citifier.core.kubernetes.data.K8sResourceContext;
import pl.bilskik.citifier.domain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.domain.dto.ChallengeDTO;

import static pl.bilskik.citifier.core.filemanager.FilePathBuilder.*;

@Service
@RequiredArgsConstructor
public class ChallengeDeployerPreparator {

    @Value("${repo.base-file-path}")
    public String baseFilePath;

    private final FileManager fileManager;
    private final DockerComposeParserManager dockerComposeParserManager;
    private final DockerImageBuilder dockerImageBuilder;

    public K8sResourceContext parseDockerComposeAndBuildImage(ChallengeDTO challengeDTO, boolean isNamespaceCreated) {
        String repoAbsolutePath = buildAbsolutePath(baseFilePath, challengeDTO.getRelativePathToRepo());
        String dockerComposeAbsolutePath = fileManager.provideDockerComposeAbsolutePath(repoAbsolutePath);

        DockerCompose compose = dockerComposeParserManager.parse(dockerComposeAbsolutePath);
        String imageName = getImageName(compose);
        dockerImageBuilder.build(repoAbsolutePath, imageName);

        K8sResourceContext resourceContext = initK8sResourceContext(challengeDTO.getChallengeAppDataDTO(), repoAbsolutePath, isNamespaceCreated);
        resourceContext.setDockerCompose(compose);

        return resourceContext;
    }

    private String getImageName(DockerCompose dockerCompose) {
        return dockerCompose.getServices().entrySet().stream()
                .filter(composeService -> !composeService.getKey().equals("db"))
                .map(composeService -> composeService.getValue().getImage())
                .findFirst()
                .orElse(null);
    }

    private K8sResourceContext initK8sResourceContext(ChallengeAppDataDTO appData, String fullRepoFilePath, boolean isNamespaceCreated) {
        return K8sResourceContext.builder()
                .appName(appData.getChallengeAppName())
                .isNamespaceCreated(isNamespaceCreated)
                .namespace(appData.getNamespace())
                .fullRepoFilePath(fullRepoFilePath)
                .startExposedPort(appData.getStartExposedPort())
                .numberOfApp(appData.getNumberOfApp())
                .portFlag(appData.getPortFlag())
                .build();
    }

}
