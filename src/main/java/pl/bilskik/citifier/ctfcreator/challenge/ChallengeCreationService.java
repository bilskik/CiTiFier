package pl.bilskik.citifier.ctfcreator.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.challenge.github.GithubDataInputDTO;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeCreationService {

    private final ChallengeDao challengeDao;

    public void createNewChallenge(ChallengeInputDTO challengeInputDTO, GithubDataInputDTO githubDataInputDTO) {
        if(challengeInputDTO == null) {
            log.info("ChallengeInputDTO is null!");
            throw new ChallengeCreationException("OOps cos poszlo nie tak :(");
        }
        if(githubDataInputDTO == null) {
            log.info("GithubDataInputDTO is null!");
            throw new ChallengeCreationException("OOps cos poszlo nie tak :(!");
        }

        ChallengeDTO challengeDTO = buildChallengeDTO(challengeInputDTO, githubDataInputDTO);
        challengeDao.createNewChallenge(challengeDTO);
    }

    private ChallengeDTO buildChallengeDTO(ChallengeInputDTO challengeInputDTO, GithubDataInputDTO githubDataInputDTO) {
        return ChallengeDTO.builder()
                .name(challengeInputDTO.getName())
                .numberOfApp(challengeInputDTO.getNumberOfApp())
                .startExposedPort(challengeInputDTO.getStartExposedPort())
                .flagGenerationMethod(challengeInputDTO.getFlagGenerationMethod())
                .githubLink(githubDataInputDTO.getGithubLink())
                .repoName(provideGithubRepoName(githubDataInputDTO.getGithubLink()))
                .isRepoClonedSuccessfully(githubDataInputDTO.getIsRepoClonedSuccessfully())
                .build();
    }

    private String provideGithubRepoName(String githubRepoLink) {
        return githubRepoLink.substring(githubRepoLink.lastIndexOf('/') + 1);
    }

}
