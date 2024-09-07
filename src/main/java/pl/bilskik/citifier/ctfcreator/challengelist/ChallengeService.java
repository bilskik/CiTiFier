package pl.bilskik.citifier.ctfcreator.challengelist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final static String EMPTY = "";

    private final ChallengeDao challengeDao;

    public List<ChallengeDTO> findAllChallengesByTournamentCode(String tournamentCode) {
        if(!EMPTY.equals(tournamentCode)) {
            return challengeDao.findAllByTournamentCode(tournamentCode);
        }

        return new ArrayList<>();
    }
}
