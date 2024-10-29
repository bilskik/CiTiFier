package pl.bilskik.citifier.ctfcreator.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.github.GithubDataInputDTO;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.enumeration.ChallengeStatus;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeCreationService {

    private final ChallengeDao challengeDao;

    public void createNewChallenge(ChallengeInputDTO challengeInput, GithubDataInputDTO githubInput) {
        if(challengeInput == null) {
            log.error("ChallengeInput is null!");
            throw new ChallengeCreationException("OOps cos poszlo nie tak :(");
        }
        if(githubInput == null) {
            log.error("GithubInput is null!");
            throw new ChallengeCreationException("OOps cos poszlo nie tak :(!");
        }

        ChallengeDTO challengeDTO = ChallengeDTO.builder()
                .name(challengeInput.getName())
                .githubLink(githubInput.getGithubLink())
                .repoName(provideGithubRepoName(githubInput.getGithubLink()))
                .status(ChallengeStatus.NEW)
                .build();

        String challengeAppName = buildChallengeAppNameFromRepoName(challengeDTO.getRepoName());
        String namespace = buildNamespace(challengeAppName);

        ChallengeAppDataDTO challengeAppDataDTO = ChallengeAppDataDTO.builder()
                .challengeAppName(challengeAppName)
                .namespace(namespace)
                .startExposedPort(challengeInput.getStartExposedPort())
                .numberOfApp(challengeInput.getNumberOfApp())
                .build();

        challengeDTO.setChallengeAppDataDTO(challengeAppDataDTO);

        challengeDao.createNewChallenge(challengeDTO);
    }

    private String provideGithubRepoName(String githubRepoLink) {
        return githubRepoLink.substring(githubRepoLink.lastIndexOf('/') + 1);
    }
    private String buildChallengeAppNameFromRepoName(String repoName) {
        if(repoName.length() > 30) {
            repoName = repoName.substring(0,30);
        }
        return repoName
                .toLowerCase()
                .replace(".", "")
                .replace("_", "");
    }
    private String buildNamespace(String appName) {
        String uuid = UUID.randomUUID().toString();
        return appName + "-" + uuid.substring(0, 30);
    }

}
