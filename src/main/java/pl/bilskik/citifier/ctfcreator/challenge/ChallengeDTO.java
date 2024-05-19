package pl.bilskik.citifier.ctfcreator.challenge;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChallengeDTO {
    private String challengeName;
    private String challengeDescription;
//    private String challengeType;
    private String githubLink;
    private boolean isTeamsEnabled;
    private String flagGenerationMethod;
    private Integer maxPointsForChallenge;
    private String pointCalculationFunction;
    private Integer minPointsForChallenge;
}
