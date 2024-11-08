package pl.bilskik.citifier.ctfdomain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

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

    @Column(name = "CHALLENGE_APP_NAME", nullable = false)
    private String challengeAppName;
    
    @Column(name = "NAMESPACE", nullable = false)
    private String namespace;

    @Column(name = "START_EXPOSED_PORT", nullable = false)
    private Integer startExposedPort;

    @Column(name = "NUMBER_OF_APP", nullable = false)
    private Integer numberOfApp;

    @ElementCollection
    @CollectionTable(name = "PORT_FLAG",
        joinColumns = {
            @JoinColumn(name = "FK_PORT_FLAG", referencedColumnName = "CHALLENGE_APP_DATA_ID")
        }
    )
    @MapKeyColumn(name = "PORT")
    @Column(name = "FLAG")
    private Map<Integer, String> portFlag;

}
