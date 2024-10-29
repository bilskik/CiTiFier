package pl.bilskik.citifier.ctfdomain.service;

import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.enumeration.ChallengeStatus;

import java.util.List;

public interface ChallengeDao {
    List<ChallengeDTO> findAllByLogin(String login);

    ChallengeDTO findById(Long id);

    ChallengeDTO createNewChallenge(ChallengeDTO challengeDTO);

    void updateChallengeStatus(ChallengeStatus status, Long challengeId);

}
