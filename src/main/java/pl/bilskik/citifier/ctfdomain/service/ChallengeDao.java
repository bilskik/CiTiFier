package pl.bilskik.citifier.ctfdomain.service;

import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;

public interface ChallengeDao {

    ChallengeDTO createNewChallenge(ChallengeDTO challengeDTO);

}
