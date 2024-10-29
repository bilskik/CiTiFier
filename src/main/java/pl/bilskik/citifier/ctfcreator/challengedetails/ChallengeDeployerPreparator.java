package pl.bilskik.citifier.ctfcreator.challengedetails;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.docker.entity.DockerCompose;
import pl.bilskik.citifier.ctfcreator.docker.service.DockerComposeParserManager;
import pl.bilskik.citifier.ctfcreator.dockerimagebuilder.DockerImageBuilder;
import pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sResourceContext;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static pl.bilskik.citifier.ctfcreator.challengedetails.FilePathBuilder.baseFilePathToRepo;
import static pl.bilskik.citifier.ctfcreator.challengedetails.FilePathBuilder.dockerComposeFilePath;

@Service
@RequiredArgsConstructor
public class ChallengeDeployerPreparator {

    private final static List<String> DOCKER_COMPOSE = Arrays.asList("docker-compose.yml", "docker-compose.yaml", "compose.yaml", "compose.yml");

    @Value("${repo.base-file-path}")
    private String baseFilePath;

    private final DockerComposeParserManager dockerComposeParserManager;
    private final DockerImageBuilder dockerImageBuilder;

    public K8sResourceContext parseDockerComposeAndBuildImage(ChallengeDTO challengeDTO, boolean isNamespaceCreated) {
        String repoFilePath = buildBaseFilePathWithRepo(challengeDTO.getRepoName());
        String composeFilePath = provideDockerComposeYamlName(repoFilePath);

        DockerCompose compose = dockerComposeParserManager.parse(dockerComposeFilePath(repoFilePath, composeFilePath));
        dockerImageBuilder.build(repoFilePath);

        K8sResourceContext resourceContext = initK8sResourceContext(challengeDTO.getChallengeAppDataDTO(), repoFilePath, isNamespaceCreated);
        resourceContext.setDockerCompose(compose);

        return resourceContext;
    }

    private String buildBaseFilePathWithRepo(String repoName) {
        return baseFilePathToRepo(baseFilePath, repoName);
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

    private String provideDockerComposeYamlName(String baseFilePath) {
        for(var fileName: DOCKER_COMPOSE) {
            File file = new File(dockerComposeFilePath(baseFilePath, fileName));
            if(file.exists()) {
                return fileName;
            }
        }
        throw new IllegalArgumentException("Docker compose not found!"); //TO DO change it
    }
}
