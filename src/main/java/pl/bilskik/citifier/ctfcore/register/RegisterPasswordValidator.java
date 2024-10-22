package pl.bilskik.citifier.ctfcore.register;

import org.springframework.stereotype.Service;

@Service
public class RegisterPasswordValidator {

    private final static String LOGIN_MIN_LENGTH_ERROR = "Hasło musi zawierać co najmniej 4 znaków!";
    private final static int MIN_LOGIN_LENGTH = 5;

    private final static String PASSWORD_MIN_LENGTH_ERROR = "Hasło musi zawierać co najmniej 8 znaków!";
    private final static int MIN_PASSWORD_LENGTH = 8;

    public void validateLogin(String login) {
        if(login.length() < MIN_LOGIN_LENGTH) {
            throw new RegisterException(LOGIN_MIN_LENGTH_ERROR);
        }
    }

    public void validatePassword(String password) {
        if(password.length() < MIN_PASSWORD_LENGTH) {
            throw new RegisterException(PASSWORD_MIN_LENGTH_ERROR);
        }
    }
}
