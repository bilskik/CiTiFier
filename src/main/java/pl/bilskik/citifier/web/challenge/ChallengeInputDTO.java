package pl.bilskik.citifier.web.challenge;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@NotNull
public class ChallengeInputDTO {
    @Size(min = ChallengeConstraints.MINIMUM_NUMBER_OF_CHARACTERS, message = "Nazwa zadania powinna być o długości co najmniej 3 znaków!")
    @Size(max = ChallengeConstraints.MAXIMUM_NUMBER_OF_CHARACTERS, message = "Nazwa zadania nie może być dłuższa niż 100 znaków!")
    @Pattern(regexp = "^[^#]*$", message = "Nazwa zadania nie może zawierać znaku '#'!")
    private String name;
    @Min(value = ChallengeConstraints.MINIMUM_NUMBER_OF_APP, message = "Liczba aplikacji powinna być w przedziale między 1 a 250")
    @Max(value = ChallengeConstraints.MAXIMUM_NUMBER_OF_APP, message = "Liczba aplikacji powinna być w przedziale między 1 a 250")
    @NotNull(message = "Proszę podać liczbę!")
    private Integer numberOfApp;
    @Min(value = ChallengeConstraints.START_NODE_PORT, message = "Początkowy port może być co najmniej 30000!")
    @NotNull(message = "Proszę podać początkowy port!")
    private Integer startExposedPort;
}
