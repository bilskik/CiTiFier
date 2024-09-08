package pl.bilskik.citifier.ctfcreator.tournament;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.bilskik.citifier.common.validator.LocalDateTimeComparasion;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@LocalDateTimeComparasion(firstField = "start", secondField = "finish", message = "Ej ty! Turniej nie może się skończyć przed rozpoczęciem!")
//@LocalDateTimeComparasion(firstField = "teamsEditionTo", secondField = "finish", message = "Ej ty! Chcesz edytować drużyny po zakończeniu turnieju!")
public class TournamentDTO {

    private String tournamentCode;
    @NotNull
    private String name;
    @NotNull
    private String description;
    private LocalDateTime start;
    private LocalDateTime finish;
    private UserCreationMethod userCreationMethod;
    private boolean teamsEnabled;
//    @Min(value = 2, message = "Daj choć dwie osoby do drużyny...!")
    private Integer teamsSize;
    private boolean teamsCreationEnabledAfterLaunch;
    private LocalDateTime teamsEditionTo;

}
