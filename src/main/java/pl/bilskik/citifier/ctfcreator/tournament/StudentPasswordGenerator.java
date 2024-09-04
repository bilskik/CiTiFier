package pl.bilskik.citifier.ctfcreator.tournament;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class StudentPasswordGenerator {

    private final static int passwordLength = 8;

    public List<String> generatePasswords(int passwordNumber) {
        Set<String> passwordList = new HashSet<>();

        while(passwordList.size() < passwordNumber) {
            String password = generatePassword();
            passwordList.add(password);
        }

        return passwordList.stream().toList();
    }

    public List<String> generateAdditionalPasswords(List<String> existingPasswords, int passwordNumberToGenerate) {
        Set<String> newPasswords = new HashSet<>();

        int existingPasswordsLength = existingPasswords.size();

        while(newPasswords.size() + existingPasswords.size() < existingPasswordsLength + passwordNumberToGenerate) {
            String password = generatePassword();
            newPasswords.add(password);
        }

        return newPasswords.stream().toList();
    }

    private String generatePassword() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "").substring(0, passwordLength);
    }

}
