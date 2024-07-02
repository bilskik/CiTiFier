package pl.bilskik.citifier.ctfdomain.mapper;

import org.mapstruct.Mapper;
import pl.bilskik.citifier.ctfcreator.tournament.TournamentDTO;
import pl.bilskik.citifier.ctfdomain.entity.Tournament;

@Mapper(componentModel = "spring")
public interface TournamentMapper {

    Tournament toTournament(TournamentDTO tournamentDTO);

    TournamentDTO toTournamentDTO(Tournament tournament);

}
