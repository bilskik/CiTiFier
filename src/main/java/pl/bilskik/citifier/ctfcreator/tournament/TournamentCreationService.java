package pl.bilskik.citifier.ctfcreator.tournament;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.common.generator.IdGenerator;
import pl.bilskik.citifier.ctfdomain.service.TournamentDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentCreationService {

    private final TournamentDao tournamentDao;
    @Qualifier("numberBasedGenerator")
    private final IdGenerator idGenerator;

    public void createNewTournament(TournamentDTO tournamentDTO) {
        if(tournamentDTO == null) {
            throw new IllegalArgumentException("Oops coś poszło nie tak :(");
        }

        List<String> tournamentCodes = tournamentDao.findAllTournamentCodes();
        String newTournamentCode = idGenerator.generateId(tournamentCodes);
        if(newTournamentCode == null || newTournamentCode.isEmpty()) {
            throw new IllegalArgumentException("Ooop coś poszło nie tak :(");
        }
        tournamentDTO.setTournamentCode(newTournamentCode);

        tournamentDao.createNewTournament(tournamentDTO);
    }

}
