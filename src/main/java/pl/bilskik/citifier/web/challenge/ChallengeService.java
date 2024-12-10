package pl.bilskik.citifier.web.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.domain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.domain.dto.ChallengeDTO;
import pl.bilskik.citifier.domain.entity.enumeration.ChallengeStatus;
import pl.bilskik.citifier.domain.service.ChallengeDao;
import pl.bilskik.citifier.web.github.GithubDataInputDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeService {

    private final ChallengePortFlagMapper challengePortFlagMapper;
    private final ChallengeDao challengeDao;

    public void createNewChallenge(ChallengeInputDTO challengeInput, GithubDataInputDTO githubInput) {
        if(challengeInput == null) {
            log.error("ChallengeInput is null!");
            throw new ChallengeCreationException("Nie mogę utworzyć zadania!");
        }
        if(githubInput == null) {
            log.error("GithubInput is null!");
            throw new ChallengeCreationException("Nie mogę utworzyć zadania!");
        }

        Map<Integer, String> portFlag = challengePortFlagMapper.map(challengeInput.getStartExposedPort(), challengeInput.getNumberOfApp());

        ChallengeDTO challengeDTO = ChallengeDTO.builder()
                .name(challengeInput.getName())
                .githubLink(githubInput.getGithubLink())
                .relativePathToRepo(githubInput.getRelativePathToRepo())
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
                .portFlag(portFlag)
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

    public boolean isChallengeNameUniqueForCTFCreator(String login, String name) {
        List<ChallengeDTO> challengeDTOList = challengeDao.findAllByLogin(login);
        if(challengeDTOList == null || challengeDTOList.isEmpty()) {
            return true;
        }
        return challengeDTOList.stream()
                .filter(x -> !x.getStatus().equals(ChallengeStatus.REMOVED))
                .filter(x -> x.getName().equals(name))
                .toList()
                .isEmpty();
    }

}
