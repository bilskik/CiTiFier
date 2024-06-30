package pl.bilskik.citifier.ctfdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bilskik.citifier.ctfdomain.entity.Student;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByLogin(String login);

    @Query(
            "SELECT S FROM Student S " +
            "WHERE S.login = :login " +
            "AND S.tournament = (" +
                "SELECT T " +
                "FROM Tournament T " +
                "WHERE T.tournamentCode = :tournamentCode" +
            ")"
    )
    Optional<Student> findByLoginAndTournamentCode(String login, String tournamentCode);

}
