package pl.bilskik.citifier.ctfcreator.tournament;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfdomain.service.TournamentDao;

@Service
@RequiredArgsConstructor
public class TournamentCreationService {

    private final TournamentDao tournamentDao;

    public void createNewTournament(TournamentDTO tournamentDTO) {

        if(tournamentDTO == null) {
            throw new IllegalArgumentException("Oops coś poszło nie tak :(");
        }

        tournamentDao.createNewTournament(tournamentDTO);

    }

}
