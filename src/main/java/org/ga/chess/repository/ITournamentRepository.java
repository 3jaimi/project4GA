package org.ga.chess.repository;

import org.ga.chess.model.Admin;
import org.ga.chess.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITournamentRepository extends JpaRepository<Long, Tournament> {

    List<Tournament> findByAdmin(Admin admin);
    List<Tournament> findByNumberOfPlayers(Integer num);
}
