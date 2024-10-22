package pl.bilskik.citifier.ctfcreator.challengelist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfcreator.docker.DockerComposeParserManager;
import pl.bilskik.citifier.ctfcreator.docker.model.DockerCompose;
import pl.bilskik.citifier.ctfcreator.dockerimagebuilder.DockerImageBuilder;
import pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceContext;
import pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceManager;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeListService {

    private final static String K8S_LABEL_REGEX = "^([A-Za-z0-9]([-A-Za-z0-9_.]{0,55}[A-Za-z0-9])?)?$"; //max 55 characters long for additional values
    private final static List<String> DOCKER_COMPOSE = Arrays.asList("docker-compose.yml", "docker-compose.yaml", "compose.yaml", "compose.yml");

    @Value("${repo.base-file-path}")
    private String baseFilePath;

    private final ChallengeDao challengeDao;
    private final K8sResourceManager k8SResourceManager;
    private final DockerComposeParserManager dockerComposeParserManager;
    private final DockerImageBuilder dockerImageBuilder;

    public List<ChallengeDTO> findAllChallenges(String login) {
        return challengeDao.findAllByLogin(login);
    }

    public void parseComposeAndDeployApp(Long challengeId) {
        String fullRepoFilePath = buildBaseFilePathWithRepo(challengeId);

        K8sResourceContext resourceContext = loadAppDataToK8sResourceContext(challengeId, fullRepoFilePath);
        DockerCompose compose = dockerComposeParserManager.parse(fullRepoFilePath + "\\" + provideDockerComposeYamlName(fullRepoFilePath));
        resourceContext.setDockerCompose(compose);

        dockerImageBuilder.build(fullRepoFilePath);

        k8SResourceManager.deploy(resourceContext);
    }

    private String buildBaseFilePathWithRepo(Long challengeId) {
        String repoName = challengeDao.findRepoNameByChallengeId(challengeId);
        return baseFilePath + "\\" + repoName;
    }

    private K8sResourceContext loadAppDataToK8sResourceContext(Long challengeId, String fullRepoFilePath) {
        ChallengeAppDataDTO appData = challengeDao.findChallengeAppDataDTOByChallengeId(challengeId);

        return K8sResourceContext.builder()
                .appName(appData.getChallengeAppName())
                .namespace(appData.getNamespace())
                .deploymentLabel("deploymentLabel")
                .serviceLabel("serviceLabel")
                .fullRepoFilePath(fullRepoFilePath)
                .startExposedPort(appData.getStartExposedPort())
                .numberOfApp(appData.getNumberOfApp())
                .build();
    }

    private String provideLabelValue(String base) {
        String uuid = UUID.randomUUID().toString();
        String labelValue = base + uuid;
        if(!isValidLabel(labelValue)) {
            log.info("Invalid labelValue {}", labelValue);
            return ""; //to change
        }
        return labelValue;
    }

    private boolean isValidLabel(String labelValue) {
        Pattern pattern = Pattern.compile(K8S_LABEL_REGEX);
        return pattern.matcher(labelValue).matches();
    }

    private String provideDockerComposeYamlName(String baseFilePath) {
        for(var fileName: DOCKER_COMPOSE) {
            File file = new File(baseFilePath + "\\" + fileName);
            if(file.exists()) {
                return fileName;
            }
        }
        throw new IllegalArgumentException("Docker compose not found!");
    }
}
