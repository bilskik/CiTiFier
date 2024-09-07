package pl.bilskik.citifier.ctfdomain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.Challenge;
import pl.bilskik.citifier.ctfdomain.mapper.ChallengeMapper;
import pl.bilskik.citifier.ctfdomain.repository.ChallengeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeDaoImpl implements ChallengeDao {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper mapper;

    @Override
    public List<ChallengeDTO> findAll() {
        List<Challenge> challengeList = challengeRepository.findAll();

        return challengeList.parallelStream()
                .map(mapper::toChallengeDTO)
                .toList();
    }

    @Override
    public List<ChallengeDTO> findAllByLogin(String login) {
        List<Challenge> challengeList = challengeRepository.findByStudentLogin(login);

        return challengeList.parallelStream()
                .map(mapper::toChallengeDTO)
                .toList();
    }

    @Override
    public List<ChallengeDTO> findAllByTournamentCode(String tournamentCode) {
        List<Challenge> challengeList = challengeRepository.findByTournamentCode(tournamentCode);

        return challengeList.parallelStream()
                .map(mapper::toChallengeDTO)
                .toList();
    }

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
