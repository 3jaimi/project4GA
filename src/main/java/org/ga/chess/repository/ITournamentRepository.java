package org.ga.chess.repository;

import org.ga.chess.model.Admin;
import org.ga.chess.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITournamentRepository extends JpaRepository<Tournament,Long > {

    List<Tournament> findByOrganiser(Admin admin);
    List<Tournament> findByNumberOfPlayers(Integer num);
}
