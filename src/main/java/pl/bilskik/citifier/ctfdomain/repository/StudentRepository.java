package pl.bilskik.citifier.ctfdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bilskik.citifier.ctfdomain.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
