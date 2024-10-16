package pl.bilskik.citifier.ctfdomain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CHALLENGE_APP_DATA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallengeAppData {

    @Id
    @SequenceGenerator(name = "challenge_app_data_seq", sequenceName = "CHALLENGE_APP_DATA_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "challenge_app_data_seq")
    @Column(name = "CHALLENGE_APP_DATA_ID", nullable = false)
    private Long challengeAppDataId;
    
    @Column(name = "CHALLENGE_APP_NAME")
    private String challengeAppName;

    @Column(name = "START_EXPOSED_PORT", nullable = false)
    private Integer startExposedPort;

    @Column(name = "NUMBER_OF_APP", nullable = false)
    private Integer numberOfApp;

}
