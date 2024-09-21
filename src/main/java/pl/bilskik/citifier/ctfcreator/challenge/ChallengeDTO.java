package pl.bilskik.citifier.ctfcreator.challenge;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.bilskik.citifier.common.validator.LocalDateTimeComparasion;
import pl.bilskik.citifier.common.validator.NumberComparasion;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@NumberComparasion(firstField = "minPoints", secondField = "maxPoints", message = "Maksymalna liczba punktów musi być większa od minimalnej!", errorPath = "minPoints")
@LocalDateTimeComparasion(firstField = "start", secondField = "finish", message = "Rozpoczęcie nie może być przed zakończeniem!")
public class ChallengeDTO {

    @Pattern(regexp = "^[a-zA-Z0-9]{4,50}$", message = "Dozwolone są tylko znaki alfanumeryczne o długości od 4 do 50 znaków.")
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9]{4,50}$", message = "Dozwolone są tylko znaki alfanumeryczne o długości od 4 do 50 znaków.")
    private String description;
    private ChallengeType challengeType;
    @Pattern(regexp = "https:\\/\\/github\\.com\\/([^\\/]+)\\/([^\\/]+)", message = "Link do repozytorium powinien być w formacie: https://github.com/{użytkownik}/{repozytorium}")
    private String githubLink;
    private LocalDateTime start;
    private LocalDateTime finish;
    private ChallengeStatus status;
    private boolean shareTaskDetailsAtStart;
    @Getter(AccessLevel.NONE)
    private boolean isTeamsEnabled;
    private FlagGenerationMethod flagGenerationMethod;
    @Min(value = 0, message = "Minimalna wartość punktów wynosi 0!")
    private Integer minPoints;
    private Integer maxPoints;
    private PointCalculationFunction pointCalculationFunction;

    public boolean getIsTeamsEnabled() {
        return isTeamsEnabled;
    }
}
