package pl.bilskik.citifier.ctfcore.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfdomain.dto.CTFCreatorDTO;
import pl.bilskik.citifier.ctfdomain.service.CTFCreatorDao;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final CTFCreatorDao ctfCreatorDao;

    //mocked login
    public void login(LoginDTO loginDTO) {
        if(loginDTO == null) {
            throw new LoginException("Oops cos poszlo nie tak :(");
        }

        if(loginDTO.isCtfCreator()) {
            CTFCreatorDTO ctfCreatorDTO = ctfCreatorDao.findByLogin(loginDTO.getLogin());

            if(ctfCreatorDTO == null) {
                throw new LoginException(
                        String.format("Nie mogę znależć CTFCreator'a z loginem: %s !", loginDTO.getLogin())
                );
            }

        }
    }
}
