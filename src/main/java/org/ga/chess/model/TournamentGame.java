package org.ga.chess.model;


import org.ga.chess.model.util.TournamentGameId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TournamentGame {

    @EmbeddedId
    private TournamentGameId id;

    @ManyToOne
    @MapsId("tournamentId")
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Override
    public String toString() {
        return getGame().getId().toString();
    }
}

