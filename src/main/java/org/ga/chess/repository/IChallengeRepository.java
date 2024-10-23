package org.ga.chess.repository;

import org.ga.chess.model.Challenge;
import org.ga.chess.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IChallengeRepository extends JpaRepository<Challenge,Long> {
    List<Challenge> findByChallenger(Player player);
    List<Challenge> findByChallengee(Player player);

}
