package pl.bilskik.citifier.ctfdomain.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeType;
import pl.bilskik.citifier.ctfcreator.challenge.FlagGenerationMethod;
import pl.bilskik.citifier.ctfcreator.challenge.PointCalculationFunction;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHALLENEGE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Challenge {

    @Id
    @Column(name = "CHALLENGE_ID", nullable = false)
    private Long challengeId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "CHALLENGE_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeType challengeType;

    @Column(name = "GITHUB_LINK", nullable = false)
    private String githubLink;

    @Column(name = "START", nullable = false)
    private LocalDateTime start;

    @Column(name = "FINISH", nullable = false)
    private LocalDateTime finish;

    @Column(name = "SHARE_TASK_DETAILS_AT_START")
    private boolean shareTaskDetailsAtStart;

    @Column(name = "IS_TEAMS_ENABLED")
    private boolean isTeamsEnabled;

    @Column(name = "FLAG_GENERATION_METHOD", nullable = false)
    @Enumerated(EnumType.STRING)
    private FlagGenerationMethod flagGenerationMethod;

    @Column(name = "MIN_POINTS", nullable = false)
    private Integer minPoints;

    @Column(name = "MAX_POINTS", nullable = false)
    private Integer maxPoints;

    @Column(name = "POINT_CALCULATION_FUNCTION", nullable = false)
    @Enumerated(EnumType.STRING)
    private PointCalculationFunction pointCalculationFunction;

    @ManyToOne
    private Tournament tournament;

}
