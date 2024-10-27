package pl.bilskik.citifier.ctfdomain.service;

import pl.bilskik.citifier.ctfcreator.challengedetails.ChallengeAppDataDTO;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.enumeration.ChallengeStatus;

import java.util.List;

public interface ChallengeDao {
    List<ChallengeDTO> findAllByLogin(String login);

    ChallengeDTO findById(Long id);

    ChallengeAppDataDTO findChallengeAppDataDTOByChallengeId(Long id);

    String findRepoNameByChallengeId(Long challengeId);

    String findNamespaceByChallengeId(Long challengeId);

    ChallengeDTO createNewChallenge(ChallengeDTO challengeDTO);

    void updateChallengeStatus(ChallengeStatus status, Long challengeId);

}
