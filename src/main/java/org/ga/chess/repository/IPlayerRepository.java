package org.ga.chess.repository;

import org.ga.chess.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPlayerRepository extends JpaRepository<Long, Player> {
    Optional<Player> findByEmail(String email);
    Optional<Player> findByRatingRatingGreaterThanEqual(Integer rating);
    Optional<Player> findByRatingRatingLessThanEqual(Integer rating);

}
