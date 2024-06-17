package pl.bilskik.citifier.ctfcreator.challenge;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChallengeDTO {
    private String name;
    private String description;
    private ChallengeTypeEnum challengeType;
    private String githubLink;
    private LocalDateTime start;
    private LocalDateTime finish;
    private boolean shareTaskDetailsAtStart;
    private boolean isTeamsEnabled;
    private FlagGenerationMethodEnum flagGenerationMethod;
    private Integer minPoints;
    private Integer maxPoints;
    private String pointCalculationFunction;
}
