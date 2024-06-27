package pl.bilskik.citifier.ctfdomain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "CTF_CREATOR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CTFCreator extends User {
    @Id
    @Column(name = "CTF_CREATOR_ID", nullable = false)
    private Long ctfCreatorId;

    @OneToMany(
            mappedBy = "CTF_CREATOR"
    )
    private List<Tournament> tournamentList;

}
