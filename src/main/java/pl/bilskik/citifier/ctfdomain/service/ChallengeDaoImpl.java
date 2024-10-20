package pl.bilskik.citifier.ctfdomain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfcreator.challengelist.ChallengeAppDataDTO;
import pl.bilskik.citifier.ctfdomain.entity.CTFCreator;
import pl.bilskik.citifier.ctfdomain.entity.Challenge;
import pl.bilskik.citifier.ctfdomain.entity.ChallengeAppData;
import pl.bilskik.citifier.ctfdomain.exception.ChallengeException;
import pl.bilskik.citifier.ctfdomain.mapper.ChallengeAppDataMapper;
import pl.bilskik.citifier.ctfdomain.mapper.ChallengeMapper;
import pl.bilskik.citifier.ctfdomain.repository.CTFCreatorRepository;
import pl.bilskik.citifier.ctfdomain.repository.ChallengeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeDaoImpl implements ChallengeDao {

    private final ChallengeRepository challengeRepository;
    private final CTFCreatorRepository ctfCreatorRepository;
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
        List<Challenge> challengeList = challengeRepository.findChallengesByCtfCreator_Login(login);
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

        ChallengeAppData challengeAppData = ChallengeAppData.builder()
                .challengeAppName("EXAMPLE TO CHANGE")
                .startExposedPort(challengeDTO.getStartExposedPort())
                .numberOfApp(challengeDTO.getNumberOfApp())
                .build();
        challenge.setChallengeAppData(challengeAppData);
        challenge.setCtfCreator(provideCTFCreator());

        challengeRepository.save(challenge);
        log.info("Challenge was successfully created!");
        return challengeDTO;
    }

    private CTFCreator provideCTFCreator() {
        return ctfCreatorRepository.findByLogin(provideLogin())
                .orElseThrow(() -> new ChallengeException("Cannot create new Challenge!"));
    }

    private String provideLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return "";
    }

    @Override
    public String findRepoNameByChallengeId(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeException("Challenge not found!"));
        return challenge.getRepoName();
    }
}
