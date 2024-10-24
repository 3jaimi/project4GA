package org.ga.chess.controller;
import org.ga.chess.model.Player;
import org.ga.chess.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/{email}")
    public ResponseEntity<?> getPlayerByEmail(@PathVariable String email) {
        return playerService.getPlayer(email);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getPlayerById(@PathVariable Long id) {
        return playerService.getPlayer(id);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editPlayer(@RequestBody Player player) {
        return playerService.editPlayer(player);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePlayer() {
        return playerService.deletePlayer();
    }
}

