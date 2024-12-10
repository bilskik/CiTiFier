package pl.bilskik.citifier.web.register;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class RegisterLoginPasswordValidator {

    private final static String LOGIN_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{5,64}$";
    private final static String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,64}$";

    private final static String LOGIN_MIN_LENGTH_ERROR = "Login musi zawierać co najmniej 4 znaki!";
    private final static String LOGIN_MAX_LENGTH_ERROR = "Login nie może zawierać więcej niż 64 znaków!";
    private final static String LOGIN_VALIDATION_ERROR = "Login musi zawierać co najmniej jedna duża literę i małą literę, jedną cyfrę!";
    private final static int MIN_LOGIN_LENGTH = 5;
    private final static int MAX_LOGIN_LENGTH = 64;

    private final static String PASSWORD_MIN_LENGTH_ERROR = "Hasło musi zawierać co najmniej 8 znaków!";
    private final static String PASSWORD_MAX_LENGTH_ERROR = "Hasło nie może zawierać więcej niż 64 znaki!";
    private final static String PASSWORD_VALIDATION_ERROR = "Hasło musi zawierać co najmniej jedną duża literę i małą literę, jedną cyfrę i jeden znak specjalny!";
    private final static int MIN_PASSWORD_LENGTH = 8;
    private final static int MAX_PASSWORD_LENGTH = 64;

    public void validateLogin(String login) {
        if(login.length() < MIN_LOGIN_LENGTH) {
            throw new RegisterException(LOGIN_MIN_LENGTH_ERROR);
        }
        if(login.length() > MAX_LOGIN_LENGTH) {
            throw new RegisterException(LOGIN_MAX_LENGTH_ERROR);
        }
        Pattern pattern = Pattern.compile(LOGIN_REGEX);
        if(!pattern.matcher(login).matches()) {
            throw new RegisterException(LOGIN_VALIDATION_ERROR);
        }
    }

    public void validatePassword(String password) {
        if(password.length() < MIN_PASSWORD_LENGTH) {
            throw new RegisterException(PASSWORD_MIN_LENGTH_ERROR);
        }
        if(password.length() > MAX_PASSWORD_LENGTH) {
            throw new RegisterException(PASSWORD_MAX_LENGTH_ERROR);
        }
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        if(!pattern.matcher(password).matches()) {
            throw new RegisterException(PASSWORD_VALIDATION_ERROR);
        }
    }
}
