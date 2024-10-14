package pl.bilskik.citifier.ctfdomain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfcreator.challengelist.ChallengeAppDataDTO;
import pl.bilskik.citifier.ctfdomain.entity.Challenge;
import pl.bilskik.citifier.ctfdomain.entity.ChallengeAppData;
import pl.bilskik.citifier.ctfdomain.exception.ChallengeException;
import pl.bilskik.citifier.ctfdomain.mapper.ChallengeAppDataMapper;
import pl.bilskik.citifier.ctfdomain.mapper.ChallengeMapper;
import pl.bilskik.citifier.ctfdomain.repository.ChallengeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeDaoImpl implements ChallengeDao {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper mapper;
    private final ChallengeAppDataMapper appDataMapper;

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
    public ChallengeAppDataDTO findChallengeAppDataDTOByChallengeId(Long id) {
        Optional<Challenge> optionalChallenge = challengeRepository.findById(id);

        if(optionalChallenge.isPresent()) {
            ChallengeAppData challengeAppData = optionalChallenge.get().getChallengeAppData();
            if(challengeAppData == null) {
                throw new ChallengeException("Challenge app data not found!");
            }
            return appDataMapper.toChallengeAppDataDTO(challengeAppData);
        }

       throw new ChallengeException("Challenge not found!");
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
            throw new ChallengeException("Cannot create new Challenge!");
        }

        Challenge challenge = mapper.toChallenge(challengeDTO);
        challengeRepository.save(challenge);

        return challengeDTO;
    }
}
