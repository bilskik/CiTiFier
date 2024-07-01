package pl.bilskik.citifier.ctfdomain.service;

import pl.bilskik.citifier.ctfdomain.dto.StudentDTO;

import java.util.List;

public interface StudentDao {

    StudentDTO findById(Long studentId);

    StudentDTO findByLogin(String login);

    StudentDTO findByLoginAndTournamentCode(String login, String tournamentCode);

    List<StudentDTO> findAll();

    StudentDTO createNewStudent(StudentDTO studentDTO);

}
