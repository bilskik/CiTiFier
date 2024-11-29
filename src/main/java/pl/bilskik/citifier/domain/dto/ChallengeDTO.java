package pl.bilskik.citifier.domain.dto;

import lombok.*;
import pl.bilskik.citifier.domain.entity.enumeration.ChallengeStatus;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallengeDTO {
    private Long challengeId;
    private String name;
    private String githubLink;
    private String relativePathToRepo;
    private String repoName;
    private ChallengeStatus status;
    private ChallengeAppDataDTO challengeAppDataDTO;
}
