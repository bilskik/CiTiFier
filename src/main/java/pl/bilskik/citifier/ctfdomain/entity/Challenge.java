package pl.bilskik.citifier.ctfdomain.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.bilskik.citifier.ctfdomain.entity.enumeration.ChallengeStatus;

@Entity
@Table(name = "CHALLENGE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Challenge {

    @Id
    @SequenceGenerator(name = "challenge_seq", sequenceName = "CHALLENGE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "challenge_seq")
    @Column(name = "CHALLENGE_ID", nullable = false)
    private Long challengeId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "GITHUB_LINK", nullable = false)
    private String githubLink;

    @Column(name = "REPO_NAME", nullable = false)
    private String repoName;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "FK_CHALLENGE_APP_DATA",
            referencedColumnName = "CHALLENGE_APP_DATA_ID"
    )
    private ChallengeAppData challengeAppData;

    @ManyToOne
    @JoinColumn(
            name = "FK_CTF_CREATOR_ID",
            referencedColumnName = "CTF_CREATOR_ID"
    )
    private CTFCreator ctfCreator;

}
