package pl.bilskik.citifier.ctfdomain.mapper;

import org.mapstruct.Mapper;
import pl.bilskik.citifier.ctfdomain.dto.StudentDTO;
import pl.bilskik.citifier.ctfdomain.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDTO toStudentDTO(Student student);

    Student toStudent(StudentDTO studentDTO);
}
