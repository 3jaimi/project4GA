package org.ga.chess.controller;

import org.ga.chess.ENUM.GAME_RESULT;
import org.ga.chess.model.Game;
import org.ga.chess.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

//    @PostMapping("/create")
//    public ResponseEntity<Game> createGame(@RequestBody Game game) {
//        return gameService.createGame(game);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        return gameService.getGameById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Game>> getAllGames() {
        return gameService.getAllGames();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editGame(@PathVariable Long id, @RequestBody Game game) {
        return gameService.editGame(id, game);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGameById(@PathVariable Long id) {
        return gameService.deleteGameById(id);
    }

    @PutMapping("/play/{id}")
    public ResponseEntity<?> playAndSaveIndividualGame(@PathVariable Long id) {
        return gameService.playAndSaveIndividualGame(id);
    }
}
