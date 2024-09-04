package pl.bilskik.citifier.ctfdomain.service;

import pl.bilskik.citifier.ctfcreator.tournament.TournamentDTO;
import pl.bilskik.citifier.ctfdomain.entity.CTFCreator;

import java.util.List;

public interface TournamentDao {

    List<TournamentDTO> findAll();

    List<TournamentDTO> findAllByCTFCreator(CTFCreator ctfCreator);

    TournamentDTO createNewTournament(TournamentDTO tournament);

}
