package pl.bilskik.citifier.ctfcore.register;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfdomain.dto.CTFCreatorDTO;
import pl.bilskik.citifier.ctfdomain.dto.StudentDTO;
import pl.bilskik.citifier.ctfdomain.service.CTFCreatorDao;
import pl.bilskik.citifier.ctfdomain.service.StudentDao;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final CTFCreatorDao ctfCreatorDao;
    private final StudentDao studentDao;

    public void register(RegisterDTO registerDTO) {
        if(registerDTO == null) {
            throw new RegisterException("Oops coś poszło nie tak :(");
        }

        if(registerDTO.isCtfCreator()) {

            CTFCreatorDTO ctfCreatorDTO = CTFCreatorDTO.builder()
                            .login(registerDTO.getLogin())
                            .password(registerDTO.getPassword())
                            .build();

            ctfCreatorDao.createCTFCreator(ctfCreatorDTO);

        } else {

            StudentDTO studentDTO = StudentDTO.builder()
                    .login(registerDTO.getLogin())
                    .password(registerDTO.getPassword())
                    .build();

            studentDao.createNewStudent(studentDTO);
        }
    }
}