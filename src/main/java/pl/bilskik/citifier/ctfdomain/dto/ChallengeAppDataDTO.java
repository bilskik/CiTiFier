package pl.bilskik.citifier.ctfdomain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallengeAppDataDTO {
    private Long challengeAppDataId;
    private String challengeAppName;
    private String namespace;
    private Integer startExposedPort;
    private Integer numberOfApp;
}
