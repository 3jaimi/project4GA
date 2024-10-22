package org.ga.chess.repository;

import org.ga.chess.model.Game;
import org.ga.chess.model.Tournament;
import org.ga.chess.model.TournamentGame;
import org.ga.chess.model.util.TournamentGameId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ITournamentGameRepository extends JpaRepository<TournamentGameId, TournamentGame> {
    Optional<TournamentGame> findByGame(Game game);
}
