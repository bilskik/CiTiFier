package pl.bilskik.citifier.ctfdomain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "STUDENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Student extends User {

    @Id
    @SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Column(name = "STUDENT_ID", nullable = false)
    private Long studentId;

    @ManyToOne
    @JoinColumn(
            name = "FK_TEAM_ID",
            referencedColumnName = "TEAM_ID"
    )
    private Team team;

    @ManyToOne
    @JoinColumn(
            name = "FK_TOURNAMENT_ID",
            referencedColumnName = "TOURNAMENT_ID"
    )
    private Tournament tournament;

}
