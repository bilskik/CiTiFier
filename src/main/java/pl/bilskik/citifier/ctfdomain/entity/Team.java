package pl.bilskik.citifier.ctfdomain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "TEAM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team {

    @Id
    @Column(name = "TEAM_ID", nullable = false)
    private Long teamId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(
            name = "TOURNAMENT_ID",
            referencedColumnName = "TOURNAMENT_ID"
    )
    private Tournament tournament;

    @OneToMany(
            mappedBy = "TEAM"
    )
    private List<Student> studentList;
}
