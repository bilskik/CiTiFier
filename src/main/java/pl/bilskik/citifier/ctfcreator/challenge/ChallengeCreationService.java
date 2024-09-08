package pl.bilskik.citifier.ctfcreator.challenge;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

@Service
@RequiredArgsConstructor
public class ChallengeCreationService {

    private final ChallengeDao challengeDao;

    public void createNewChallenge(ChallengeDTO challengeDTO) {
        if(challengeDTO == null) {
            throw new IllegalArgumentException("OOps cos poszlo nie tak :(");
        }

        challengeDTO.setPointCalculationFunction(PointCalculationFunction.LINEAR); //mocked
        challengeDao.createNewChallenge(challengeDTO);

    }

}
