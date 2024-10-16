package pl.bilskik.citifier.ctfcreator.challenge;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChallengeDTO {

    @Pattern(regexp = "^[a-zA-Z0-9]{4,50}$", message = "Dozwolone są tylko znaki alfanumeryczne o długości od 4 do 50 znaków.")
    private String name;
    @Pattern(regexp = "https:\\/\\/github\\.com\\/([^\\/]+)\\/([^\\/]+)", message = "Link do repozytorium powinien być w formacie: https://github.com/{użytkownik}/{repozytorium}")
    private String githubLink;
    private FlagGenerationMethod flagGenerationMethod;
}
