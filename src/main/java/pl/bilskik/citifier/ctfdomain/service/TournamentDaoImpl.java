package pl.bilskik.citifier.ctfdomain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.tournament.TournamentDTO;
import pl.bilskik.citifier.ctfdomain.entity.CTFCreator;
import pl.bilskik.citifier.ctfdomain.entity.Tournament;
import pl.bilskik.citifier.ctfdomain.mapper.TournamentMapper;
import pl.bilskik.citifier.ctfdomain.repository.TournamentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentDaoImpl implements TournamentDao {

    private final TournamentRepository tournamentRepository;
    private final TournamentMapper mapper;

    @Override
    public List<TournamentDTO> findAll() {
        List<Tournament> tournaments = tournamentRepository.findAll();

        return tournaments.parallelStream()
                        .map(mapper::toTournamentDTO)
                        .toList();
    }

    @Override
    public List<TournamentDTO> findAllByCTFCreator(CTFCreator ctfCreator) {
        List<Tournament> tournaments = tournamentRepository.findByCtfCreator(ctfCreator);

        return tournaments.parallelStream()
                .map(mapper::toTournamentDTO)
                .toList();
    }

    @Override
    @Transactional
    public TournamentDTO createNewTournament(TournamentDTO tournamentDTO) {
        if(tournamentDTO == null) {
            throw new IllegalArgumentException("Oops cos poszlo nie tak :(");
        }

        Tournament tournament = mapper.toTournament(tournamentDTO);
        tournamentRepository.save(tournament);

        return tournamentDTO;
    }
}
