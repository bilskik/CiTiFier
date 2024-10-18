package pl.bilskik.citifier.ctfdomain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    @Transactional
    public ChallengeDTO createNewChallenge(ChallengeDTO challengeDTO) {
        Challenge challenge = mapper.toChallenge(challengeDTO);
        if(challenge == null)  {
            log.info("Cannot create challenge because is null!");
            throw new ChallengeException("Cannot create new Challenge!");
        }
        challengeRepository.save(challenge);
        log.info("Challenge was successfully created!");
        return challengeDTO;
    }
}
