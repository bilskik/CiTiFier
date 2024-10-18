package pl.bilskik.citifier.ctfcreator.challenge;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ChallengeDTO {
    private String name;
    private Integer numberOfApp;
    private Integer startExposedPort;
    private FlagGenerationMethod flagGenerationMethod;
    private String githubLink;
    private String repoName;
    private boolean isRepoClonedSuccessfully;
}
