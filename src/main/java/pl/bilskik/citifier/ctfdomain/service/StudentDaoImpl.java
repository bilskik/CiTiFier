package pl.bilskik.citifier.ctfdomain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfdomain.dto.StudentDTO;
import pl.bilskik.citifier.ctfdomain.entity.Student;
import pl.bilskik.citifier.ctfdomain.mapper.StudentMapper;
import pl.bilskik.citifier.ctfdomain.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentDaoImpl implements StudentDao {

    private final StudentRepository studentRepository;
    private final StudentMapper mapper;

    @Override
    public StudentDTO findById(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);

        return studentOptional.map(mapper::toStudentDTO).orElse(null);
    }

    @Override
    public StudentDTO findByLogin(String login) {
        Optional<Student> studentOptional = studentRepository.findByLogin(login);

        return studentOptional.map(mapper::toStudentDTO).orElse(null);
    }

    @Override
    public List<StudentDTO> findAll() {
        return studentRepository.findAll()
                .parallelStream()
                .map(mapper::toStudentDTO)
                .toList();
    }

    @Override
    public StudentDTO createNewStudent(StudentDTO studentDTO) {
        if(studentDTO == null) {
            throw new IllegalArgumentException("StudentDTO cannot be null");
        }

        Student student = mapper.toStudent(studentDTO);
        studentRepository.save(student);

        log.info("Student has been saved!");

        return studentDTO;
    }
}
