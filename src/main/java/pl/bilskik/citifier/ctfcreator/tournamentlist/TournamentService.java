package pl.bilskik.citifier.ctfcreator.tournamentlist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.tournament.TournamentDTO;
import pl.bilskik.citifier.ctfdomain.service.TournamentDao;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentDao tournamentDao;

    public List<TournamentDTO> findAllTournamentsByLogin(String login) {
        if(login == null || login.isEmpty()) {
            return new ArrayList<>();
        }

        return tournamentDao.findAllByCTFCreatorLogin(login);
    }
}
