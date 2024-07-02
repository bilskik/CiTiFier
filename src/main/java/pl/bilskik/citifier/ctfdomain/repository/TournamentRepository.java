package pl.bilskik.citifier.ctfdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bilskik.citifier.ctfdomain.entity.CTFCreator;
import pl.bilskik.citifier.ctfdomain.entity.Tournament;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    List<Tournament> findByCtfCreator(CTFCreator ctfCreator);

}
