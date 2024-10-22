package org.ga.chess.repository;

import org.ga.chess.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPlayerRepository extends JpaRepository<Player, Long > {

    Optional<Player> findByEmail(String email);
    Optional<Player> findByRatingGreaterThanEqual(Integer rating);
    Optional<Player> findByRatingLessThanEqual(Integer rating);

}
