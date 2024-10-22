package org.ga.chess.repository;

import org.ga.chess.ENUM.GAME_RESULT;
import org.ga.chess.model.Game;
import org.ga.chess.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IGameRepository extends JpaRepository<Long, Game> {

    List<Game> findByResult(GAME_RESULT gameResult);
    List<Game> findByBlackOrWhite(Player player);

}
