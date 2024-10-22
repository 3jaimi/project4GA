package org.ga.chess.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ga.chess.ENUM.GAME_RESULT;

import java.util.Optional;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Player black;

    @ManyToOne
    @JoinColumn
    private Player white;

    @Column
    private GAME_RESULT result;

    @ManyToOne
    @JoinColumn
    private Tournament tournament;

}
