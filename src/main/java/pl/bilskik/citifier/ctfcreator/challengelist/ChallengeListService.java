package pl.bilskik.citifier.ctfcreator.challengelist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfcreator.docker.DockerComposeParserManager;
import pl.bilskik.citifier.ctfcreator.docker.model.DockerCompose;
import pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceContext;
import pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceManager;
import pl.bilskik.citifier.ctfdomain.entity.ChallengeAppData;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeListService {

    private final static String EMPTY = "";
    private final static String K8S_LABEL_REGEX = "^([A-Za-z0-9]([-A-Za-z0-9_.]{0,55}[A-Za-z0-9])?)?$"; //max 55 characters long for additional values

    private final ChallengeDao challengeDao;
    private final K8sResourceManager k8SResourceManager;
    private final DockerComposeParserManager dockerComposeParserManager;

    public List<ChallengeDTO> findAllChallengesByTournamentCode(String tournamentCode) {
        if(!EMPTY.equals(tournamentCode)) {
            return challengeDao.findAllByTournamentCode(tournamentCode);
        }

        return new ArrayList<>();
    }

    public List<ChallengeDTO> findAllChallenges() {
        return challengeDao.findAll();
    }

    public void parseComposeAndDeployApp() {
        K8sResourceContext resourceContext = loadAppDataToK8sResourceContext();
        DockerCompose compose = dockerComposeParserManager.parse("D:\\GitHubProjects\\secure-notes-app\\compose.yaml");
        resourceContext.setDockerCompose(compose);

        k8SResourceManager.deploy(resourceContext);
    }

    private K8sResourceContext loadAppDataToK8sResourceContext() {
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
}
