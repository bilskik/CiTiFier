package pl.bilskik.citifier.ctfcreator.challengelist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChallengeAppDataDTO {
    private String challengeAppName;
    private String namespace;
    private Integer startExposedPort;
    private Integer numberOfApp;
}
