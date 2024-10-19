package pl.bilskik.citifier.ctfdomain.service;

import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfcreator.challengelist.ChallengeAppDataDTO;

import java.util.List;

public interface ChallengeDao {

    List<ChallengeDTO> findAll();

    ChallengeAppDataDTO findChallengeAppDataDTOByChallengeId(Long id);

    ChallengeDTO createNewChallenge(ChallengeDTO challengeDTO);

    String findRepoNameByChallengeId(Long challengeId);
}
