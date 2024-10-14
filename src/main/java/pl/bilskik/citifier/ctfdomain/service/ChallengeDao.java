package pl.bilskik.citifier.ctfdomain.service;

import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfcreator.challengelist.ChallengeAppDataDTO;

import java.util.List;

public interface ChallengeDao {

    List<ChallengeDTO> findAll();

    List<ChallengeDTO> findAllByLogin(String login);

    ChallengeAppDataDTO findChallengeAppDataDTOByChallengeId(Long id);

    List<ChallengeDTO> findAllByTournamentCode(String tournamentCode);

    ChallengeDTO createNewChallenge(ChallengeDTO challengeDTO);

}
