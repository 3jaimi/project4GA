package org.ga.chess.repository;

import org.ga.chess.model.Game;
import org.ga.chess.model.Tournament;
import org.ga.chess.model.TournamentGame;
import org.ga.chess.model.util.TournamentGameId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITournamentGameRepository extends JpaRepository<TournamentGame, TournamentGameId > {
    Optional<TournamentGame> findByGame(Game game);
}
