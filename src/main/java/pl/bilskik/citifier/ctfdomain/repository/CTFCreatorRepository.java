package pl.bilskik.citifier.ctfdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bilskik.citifier.ctfdomain.entity.CTFCreator;

import java.util.Optional;

@Repository
public interface CTFCreatorRepository extends JpaRepository<CTFCreator, Long> {

    Optional<CTFCreator> findByLogin(String login);
    
}
