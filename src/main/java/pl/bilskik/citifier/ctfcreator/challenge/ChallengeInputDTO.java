package pl.bilskik.citifier.ctfcreator.challenge;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Map;

import static pl.bilskik.citifier.ctfcreator.challenge.ChallengeConstraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@NotNull
public class ChallengeInputDTO {
    @Size(min = MINIMUM_NUMBER_OF_CHARACTERS, message = "Nazwa zadania powinna być o długości co najmniej 3 znaków!")
    @Size(max = MAXIMUM_NUMBER_OF_CHARACTERS, message = "Nazwa zadania nie może być dłuższa niż 100 znaków!")
    private String name;
    @Min(value = MINIMUM_NUMBER_OF_APP, message = "Liczba aplikacji powinna być w przedziale między 1 a 250")
    @Max(value = MAXIMUM_NUMBER_OF_APP, message = "Liczba aplikacji powinna być w przedziale między 1 a 250")
    @NotNull(message = "Proszę podać liczbę!")
    private Integer numberOfApp;
    @Min(value = START_NODE_PORT, message = "Początkowy port może być co najmniej 30000!")
    @NotNull(message = "Proszę podać początkowy port!")
    private Integer startExposedPort;
}
