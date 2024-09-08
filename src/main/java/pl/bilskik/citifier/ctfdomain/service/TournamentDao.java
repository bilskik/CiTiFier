package pl.bilskik.citifier.ctfdomain.service;

import pl.bilskik.citifier.ctfcreator.tournament.TournamentDTO;
import pl.bilskik.citifier.ctfdomain.entity.CTFCreator;

import java.util.List;

public interface TournamentDao {

    List<TournamentDTO> findAll();

    List<String> findAllTournamentCodes();

    List<TournamentDTO> findAllByCTFCreator(CTFCreator ctfCreator);

    List<TournamentDTO> findAllByCTFCreatorLogin(String login);

    TournamentDTO createNewTournament(TournamentDTO tournament);

}
