package pl.bilskik.citifier.ctfdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bilskik.citifier.ctfdomain.entity.Challenge;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    @Query(
            "SELECT C FROM Challenge C " +
                    "WHERE C.tournament = (" +
                        "SELECT S.tournament " +
                        "FROM Student S " +
                        "WHERE S.login = :login" +
                    ")"
    )
    List<Challenge> findByStudentLogin(String login);

    @Query(
            "SELECT C FROM Challenge C " +
                    "WHERE C.tournament = (" +
                    "SELECT T " +
                    "FROM Tournament T " +
                    "WHERE T.tournamentCode = :tournamentCode" +
                ")"
    )
    List<Challenge> findByTournamentCode(String tournamentCode);
}
