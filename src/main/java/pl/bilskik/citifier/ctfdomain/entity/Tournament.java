package pl.bilskik.citifier.ctfdomain.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.bilskik.citifier.ctfdomain.entity.converter.LocalDateTimeTimestampConverter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TOURNAMENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tournament {

    @Id
    @SequenceGenerator(name = "tournament_seq", sequenceName = "TOURNAMENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournament_seq")
    @Column(name = "TOURNAMENT_ID", nullable = false)
    private Long tournamentId;

    @Column(name = "TOURNAMENT_CODE", nullable = false, unique = true)
    private String tournamentCode;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Convert(converter = LocalDateTimeTimestampConverter.class)
    @Column(name = "START", nullable = false)
    private LocalDateTime start;

    @Convert(converter = LocalDateTimeTimestampConverter.class)
    @Column(name = "FINISH", nullable = false)
    private LocalDateTime finish;

    @Column(name = "TEAM_CREATION_POST_LAUNCH", nullable = false)
    private boolean teamsCreationPostLaunch;

    @Convert(converter = LocalDateTimeTimestampConverter.class)
    @Column(name = "TEAM_EDITION_DEADLINE")
    private LocalDateTime teamEditionDeadline;

    @JoinTable(
            name = "TOURNAMENT_CHALLENGE",
            joinColumns = @JoinColumn(name = "TOURNAMENT_ID"),
            inverseJoinColumns = @JoinColumn(name  = "CHALLENGE_ID")
    )
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = { CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE }
    )
    private List<Challenge> challengeList;

    @OneToMany(
            mappedBy = "tournament"
    )
    private List<Student> studentList;

    @OneToMany(
            mappedBy = "tournament"
    )
    private List<Team> teamList;

    @ManyToOne
    @JoinColumn(
            name = "FK_CTF_CREATOR_ID",
            referencedColumnName = "CTF_CREATOR_ID"
    )
    private CTFCreator ctfCreator;

}
