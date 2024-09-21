package pl.bilskik.citifier.ctfdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bilskik.citifier.ctfdomain.entity.CTFCreator;
import pl.bilskik.citifier.ctfdomain.entity.Tournament;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    @Query("SELECT T.tournamentCode FROM Tournament T")
    List<String> findAllTournamentCodes();

    List<Tournament> findByCtfCreator(CTFCreator ctfCreator);

    @Query(
            "SELECT T " +
            "FROM Tournament T " +
            "WHERE T.ctfCreator = ( " +
                "SELECT C " +
                "FROM CTFCreator C " +
                "WHERE C.login = :login " +
            ")"
    )
    List<Tournament> findByCTFCreatorLogin(String login);

}
