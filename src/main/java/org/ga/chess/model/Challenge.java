package org.ga.chess.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ga.chess.ENUM.CHALLENGE_STATUS;
import org.ga.chess.ENUM.PLAYER_COLOUR;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Player challenger;

    @ManyToOne
    @JoinColumn
    private Player challengee;

    @Column
    private PLAYER_COLOUR challengeeColour;

    @Column
    private CHALLENGE_STATUS status;


}
