package pl.bilskik.citifier.ctfcreator.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeCreationService {

    private final ChallengeDao challengeDao;

    public void createNewChallenge(ChallengeDTO challengeDTO) {
        if(challengeDTO == null) {
            log.info("ChallengeDTO is null!");
            throw new ChallengeCreationException("OOps cos poszlo nie tak :(");
        }
        challengeDTO.setRepoName(provideGithubRepoName(challengeDTO.getGithubLink()));
        challengeDao.createNewChallenge(challengeDTO);
    }

    private String provideGithubRepoName(String githubRepoLink) {
        return githubRepoLink.substring(githubRepoLink.lastIndexOf('/') + 1);
    }

}
