package pl.bilskik.citifier.ctfdomain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.Challenge;
import pl.bilskik.citifier.ctfdomain.mapper.ChallengeMapper;
import pl.bilskik.citifier.ctfdomain.repository.ChallengeRepository;

@Service
@RequiredArgsConstructor
public class ChallengeDaoImpl implements ChallengeDao {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper mapper;

    @Override
    @Transactional
    public ChallengeDTO createNewChallenge(ChallengeDTO challengeDTO) {
        if(challengeDTO == null)  {
            throw new IllegalArgumentException("Oops cos poszlo nie tak :(");
        }

        Challenge challenge = mapper.toChallenge(challengeDTO);
        challengeRepository.save(challenge);

        return challengeDTO;
    }
}
