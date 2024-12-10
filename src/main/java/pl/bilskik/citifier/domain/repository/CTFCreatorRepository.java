package pl.bilskik.citifier.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bilskik.citifier.domain.entity.CTFCreator;

import java.util.Optional;

@Repository
public interface CTFCreatorRepository extends JpaRepository<CTFCreator, Long> {

    Optional<CTFCreator> findByLogin(String login);
    
}
