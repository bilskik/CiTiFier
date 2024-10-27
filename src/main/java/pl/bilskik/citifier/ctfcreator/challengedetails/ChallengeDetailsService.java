package pl.bilskik.citifier.ctfcreator.challengedetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.docker.entity.DockerCompose;
import pl.bilskik.citifier.ctfcreator.docker.service.DockerComposeParserManager;
import pl.bilskik.citifier.ctfcreator.dockerimagebuilder.DockerImageBuilder;
import pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sResourceContext;
import pl.bilskik.citifier.ctfcreator.kubernetes.service.K8sResourceManager;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.enumeration.ChallengeStatus;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static pl.bilskik.citifier.ctfcreator.challengedetails.FilePathBuilder.baseFilePathToRepo;
import static pl.bilskik.citifier.ctfcreator.challengedetails.FilePathBuilder.dockerComposeFilePath;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeDetailsService {

    private final static List<String> DOCKER_COMPOSE = Arrays.asList("docker-compose.yml", "docker-compose.yaml", "compose.yaml", "compose.yml");

    private final ChallengeDao challengeDao;
    private final K8sResourceManager k8SResourceManager;
    private final DockerComposeParserManager dockerComposeParserManager;
    private final DockerImageBuilder dockerImageBuilder;

    @Value("${repo.base-file-path}")
    private String baseFilePath;

    public ChallengeDTO findChallengeById(Long challengeId) {
        if(challengeId == null) {
            return new ChallengeDTO(); // ?
        }

        return challengeDao.findById(challengeId);
    }

    public void startAndDeployApp(Long challengeId) {
    }

    public void startApp(Long challengeId) {
    }

    public void stopApp(Long challengeId) {
    }

    public void deleteApp(Long challengeId) {
    }

    public void deployApp(Long challengeId) {
        String repoFilePath = buildBaseFilePathWithRepo(challengeId);
        String composeFilePath = provideDockerComposeYamlName(repoFilePath);

        DockerCompose compose = dockerComposeParserManager.parse(dockerComposeFilePath(repoFilePath, composeFilePath));
        dockerImageBuilder.build(repoFilePath);

        K8sResourceContext resourceContext = initK8sResourceContext(challengeId, repoFilePath);
        resourceContext.setDockerCompose(compose);

        k8SResourceManager.deployAndStart(resourceContext);

        challengeDao.updateChallengeStatus(ChallengeStatus.RUNNING, challengeId);
    }

    private String buildBaseFilePathWithRepo(Long challengeId) {
        String repoName = challengeDao.findRepoNameByChallengeId(challengeId);
        return baseFilePathToRepo(baseFilePath, repoName);
    }

    private K8sResourceContext initK8sResourceContext(Long challengeId, String fullRepoFilePath) {
        ChallengeAppDataDTO appData = challengeDao.findChallengeAppDataDTOByChallengeId(challengeId);

        return K8sResourceContext.builder()
                .appName(appData.getChallengeAppName())
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

    public void undeployApp(Long challengeId) {
        log.info("DEPLOYING APP: {}", challengeId);

        String namespace = challengeDao.findNamespaceByChallengeId(challengeId);

        k8SResourceManager.stop(namespace);

        challengeDao.updateChallengeStatus(ChallengeStatus.STOPPED, challengeId);
    }


}
