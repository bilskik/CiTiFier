package pl.bilskik.citifier.ctfcore.register;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfdomain.dto.CTFCreatorDTO;
import pl.bilskik.citifier.ctfdomain.service.CTFCreatorDao;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final PasswordEncoder passwordEncoder;
    private final CTFCreatorDao ctfCreatorDao;

    public void register(RegisterDTO registerDTO) {
        if(registerDTO == null) {
            throw new RegisterException("RegisterDTO is null");
        }

        CTFCreatorDTO ctfCreatorDTO = CTFCreatorDTO.builder()
                        .login(registerDTO.getLogin())
                        .password(passwordEncoder.encode(registerDTO.getPassword()))
                        .build();

        ctfCreatorDao.createCTFCreator(ctfCreatorDTO);
    }
}
