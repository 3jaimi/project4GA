package org.ga.chess.model;


import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ga.chess.ENUM.TOURNAMENT_STATUS;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Admin organiser;

    @OneToMany(mappedBy = "tournament")
    private List<TournamentGame> games;

    @Column
    private Integer numberOfPlayers;

    @Column
    private Integer numberOfPlayersJoined;

    @Column
    private TOURNAMENT_STATUS status;

}
