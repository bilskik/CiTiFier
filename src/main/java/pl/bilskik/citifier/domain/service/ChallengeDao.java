package pl.bilskik.citifier.domain.service;

import pl.bilskik.citifier.domain.dto.ChallengeDTO;
import pl.bilskik.citifier.domain.entity.enumeration.ChallengeStatus;

import java.util.List;

public interface ChallengeDao {
    List<ChallengeDTO> findAllByLogin(String login);

    ChallengeDTO findById(Long id);

    ChallengeDTO createNewChallenge(ChallengeDTO challengeDTO);

    void updateChallengeStatus(ChallengeStatus status, Long challengeId);

}
