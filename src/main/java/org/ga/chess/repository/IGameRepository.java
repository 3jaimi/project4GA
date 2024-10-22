package org.ga.chess.repository;

import org.ga.chess.ENUM.GAME_RESULT;
import org.ga.chess.model.Game;
import org.ga.chess.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGameRepository extends JpaRepository< Game, Long> {

    List<Game> findByResult(GAME_RESULT gameResult);
    List<Game> findByBlackOrWhite(Player player, Player player2);

}
