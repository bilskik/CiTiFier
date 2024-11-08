package pl.bilskik.citifier.ctfdomain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.CTFCreator;
import pl.bilskik.citifier.ctfdomain.entity.Challenge;
import pl.bilskik.citifier.ctfdomain.entity.ChallengeAppData;
import pl.bilskik.citifier.ctfdomain.entity.enumeration.ChallengeStatus;
import pl.bilskik.citifier.ctfdomain.exception.ChallengeException;
import pl.bilskik.citifier.ctfdomain.mapper.ChallengeAppDataMapper;
import pl.bilskik.citifier.ctfdomain.mapper.ChallengeMapper;
import pl.bilskik.citifier.ctfdomain.repository.CTFCreatorRepository;
import pl.bilskik.citifier.ctfdomain.repository.ChallengeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeDaoImpl implements ChallengeDao {

    private final ChallengeRepository challengeRepository;
    private final CTFCreatorRepository ctfCreatorRepository;
    private final ChallengeMapper mapper;

    @Override
    @Transactional //required to keep session with database
    public List<ChallengeDTO> findAllByLogin(String login) {
        List<Challenge> challengeList = challengeRepository.findChallengesByCtfCreatorLogin(login);
        return challengeList.stream()
                .map(mapper::toChallengeDTO)
                .toList();
    }

    @Override
    public ChallengeDTO findById(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeException("Challenge not found!"));
        if(challenge.getChallengeAppData() == null) {
            throw new ChallengeException("ChallengeAppData is null!");
        }
        return mapper.toChallengeDTO(challenge);
    }

    @Override
    @Transactional
    public ChallengeDTO createNewChallenge(ChallengeDTO challengeDTO) {
        Challenge challenge = mapper.toChallenge(challengeDTO);
        if(challenge == null)  {
            log.info("Cannot create challenge because is null!");
            throw new ChallengeException("Cannot create new Challenge!");
        }
        CTFCreator ctfCreator = provideCTFCreator();
        challenge.setCtfCreator(ctfCreator);

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
        log.error("Cannot find current logged user! Challenge cannot be created!");
        throw new ChallengeException("Cannot create new Challenge! Cannot find current logged user!");
    }

    @Override
    @Transactional
    public void updateChallengeStatus(ChallengeStatus status, Long challengeId) {
        challengeRepository.updateChallengeStatus(status, challengeId);
    }

}
