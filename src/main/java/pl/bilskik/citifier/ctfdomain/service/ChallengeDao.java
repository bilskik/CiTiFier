package pl.bilskik.citifier.ctfdomain.service;

import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;

import java.util.List;

public interface ChallengeDao {

    List<ChallengeDTO> findAll();

    List<ChallengeDTO> findAllByLogin(String login);

    List<ChallengeDTO> findAllByTournamentCode(String tournamentCode);

    ChallengeDTO createNewChallenge(ChallengeDTO challengeDTO);

}
