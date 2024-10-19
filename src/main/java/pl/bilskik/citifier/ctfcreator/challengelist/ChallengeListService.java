package pl.bilskik.citifier.ctfcreator.challengelist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfcreator.dockebuilder.BuildDockerContainers;
import pl.bilskik.citifier.ctfcreator.docker.DockerComposeParserManager;
import pl.bilskik.citifier.ctfcreator.docker.model.DockerCompose;
import pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceContext;
import pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceManager;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

import java.io.File;
import java.util.ArrayList;
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
    private final BuildDockerContainers dockerContainers;

    public List<ChallengeDTO> findAllChallengesByTournamentCode(String tournamentCode) {
        return new ArrayList<>();
    }

    public List<ChallengeDTO> findAllChallenges() {
        return challengeDao.findAll();
    }

    public void parseComposeAndDeployApp(Long challengeId) {
        String repoName = challengeDao.findRepoNameByChallengeId(challengeId);
        String baseFilePathWithRepo = baseFilePath + "\\" + repoName;
        K8sResourceContext resourceContext = loadAppDataToK8sResourceContext(challengeId);
        DockerCompose compose = dockerComposeParserManager.parse(baseFilePathWithRepo + provideDockerComposeYamlName(baseFilePathWithRepo));
        resourceContext.setDockerCompose(compose);

        dockerContainers.build(baseFilePathWithRepo);

        k8SResourceManager.deploy(resourceContext);
    }

    private K8sResourceContext loadAppDataToK8sResourceContext(Long challengeId) {
//        ChallengeAppDataDTO appData = challengeDao.findChallengeAppDataDTOByChallengeId(1L);

//        return K8sResourceContext.builder()
//                .namespace("DEFAULT")
//                .deploymentLabel(provideLabelValue(appData.getChallengeAppName()))
//                .serviceLabel(provideLabelValue(appData.getChallengeAppName()))
//                .startExposedPort(appData.getStartExposedPort())
//                .numberOfApp(appData.getNumberOfApp())
//                .build();
        return K8sResourceContext.builder()
                .namespace("default")
                .deploymentLabel("secure-notes-app")
                .serviceLabel("secure-notes-app")
                .startExposedPort(32100)
                .numberOfApp(2)
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
            File file = new File(baseFilePath + fileName);
            if(file.exists()) {
                return fileName;
            }
        }
        throw new IllegalArgumentException("NIE MAAAA");
    }
}
