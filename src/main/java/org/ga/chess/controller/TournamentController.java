package org.ga.chess.controller;

import org.ga.chess.model.Tournament;
import org.ga.chess.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tournament")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @PostMapping("/create")
    public ResponseEntity<?> createTournament(@RequestParam Integer numOfPlayers) {
        return tournamentService.createTournament(numOfPlayers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTournamentById(@PathVariable Long id) {
        return tournamentService.getTournament(id);
    }
    @GetMapping("/pending")
    public ResponseEntity<?> getAllPendingTournaments() {
        return tournamentService.getAllNotStartedTournaments();
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<?> joinTournament(@PathVariable Long id) {
        return tournamentService.joinTournament(id);
    }

    @PostMapping("/play/{id}")
    public ResponseEntity<?> playTournament(@PathVariable Long id) {
        return tournamentService.playTournament(id);
    }


}
