package pl.bilskik.citifier.ctfcreator.challenge;

import jakarta.validation.constraints.*;
import lombok.*;

import static pl.bilskik.citifier.ctfcreator.challenge.ChallengeConstraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChallengeInputDTO {
    @Pattern(regexp = "^[a-zA-Z0-9]{4,50}$", message = "Dozwolone są tylko znaki alfanumeryczne o długości od 4 do 50 znaków.")
    private String name;
    @Min(value = MINIMUM_NUMBER_OF_APP, message = "Liczba aplikacji powinna być w przedziale między 1 a 250")
    @Max(value = MAXIMUM_NUMBER_OF_APP, message = "Liczba aplikacji powinna być w przedziale między 1 a 250")
    @NotNull(message = "Podaj liczbę!")
    private Integer numberOfApp;
    @Min(value = START_NODE_PORT, message = "Początkowy port może być co najmniej 30000!")
    @NotNull(message = "Podaj port!")
    private Integer startExposedPort;
    private FlagGenerationMethod flagGenerationMethod;
}
