package pl.bilskik.citifier.ctfdomain.dto;

import lombok.*;
import pl.bilskik.citifier.ctfdomain.entity.enumeration.ChallengeStatus;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallengeDTO {
    private Long challengeId;
    private String name;
    private String githubLink;
    private String repoName;
    private ChallengeStatus status;
    private ChallengeAppDataDTO challengeAppDataDTO;
}
