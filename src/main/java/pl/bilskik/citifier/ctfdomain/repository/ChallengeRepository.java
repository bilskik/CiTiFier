package pl.bilskik.citifier.ctfdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.bilskik.citifier.ctfdomain.entity.Challenge;
import pl.bilskik.citifier.ctfdomain.entity.enumeration.ChallengeStatus;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query(
        "SELECT C FROM Challenge C " +
        "JOIN C.challengeAppData " +
        "WHERE C.ctfCreator.login = :login"
    )
    List<Challenge> findChallengesByCtfCreatorLogin(String login);

    @Query(
        "UPDATE Challenge C " +
        "SET C.status = :status " +
        "WHERE C.challengeId = :challengeId"
    )
    @Modifying
    void updateChallengeStatus(@Param("status") ChallengeStatus status, @Param("challengeId") Long challengeId);
}
