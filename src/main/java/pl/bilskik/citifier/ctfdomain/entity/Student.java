package pl.bilskik.citifier.ctfdomain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "STUDENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student extends User {

    @Id
    @SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Column(name = "STUDENT_ID", nullable = false)
    private Long studentId;

    @ManyToOne
    @JoinColumn(
            name = "TEAM_ID",
            referencedColumnName = "TEAM_ID"
    )
    private Team team;

    @ManyToOne
    @JoinColumn(
            name = "TOURNAMENT_ID",
            referencedColumnName = "TOURNAMENT_ID"
    )
    private Tournament tournament;

}
