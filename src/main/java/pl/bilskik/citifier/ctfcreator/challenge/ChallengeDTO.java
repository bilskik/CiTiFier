package pl.bilskik.citifier.ctfcreator.challenge;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.*;

import static pl.bilskik.citifier.ctfcreator.challenge.ChallengeConstraints.*;

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

    private boolean isRepoClonedSuccessfully;

    @Null
    private String repoName;

    @Min(MINIMUM_NUMBER_OF_APP)
    @Max(MAXIMUM_NUMBER_OF_APP)
    @NotNull
    private Integer numberOfApp;

    @Min(START_NODE_PORT)
    @NotNull
    private Integer startExposedPort;

    private FlagGenerationMethod flagGenerationMethod;


    public boolean getIsRepoClonedSuccessfully() {
        return isRepoClonedSuccessfully;
    }
}
