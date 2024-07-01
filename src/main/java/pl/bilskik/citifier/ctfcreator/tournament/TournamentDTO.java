package pl.bilskik.citifier.ctfcreator.tournament;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDTO {

    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime finish;
    private String userCreationMethod;
    private boolean teamsEnabled;
    private Integer teamsSize;
    private boolean teamsCreationEnabledAfterLaunch;
    private LocalDateTime teamsEditionTo;

}
