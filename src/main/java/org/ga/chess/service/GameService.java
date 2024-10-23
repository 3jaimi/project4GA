package org.ga.chess.service;


import org.ga.chess.ENUM.GAME_RESULT;
import org.ga.chess.exception.NotFoundException;
import org.ga.chess.model.Game;
import org.ga.chess.repository.IGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private IGameRepository gameRepository;

    public ResponseEntity<Game> createGame(Game game) {
        Game savedGame = gameRepository.save(game);
        return new ResponseEntity<>(savedGame, HttpStatus.valueOf(200));
    }

    public ResponseEntity<Game> getGameById(Long id) {
        Optional<Game> game = gameRepository.findById(id);
        return game.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = gameRepository.findAll();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    public ResponseEntity<?> editGame(Long id,Game game){
        Game inDb=gameRepository.findById(id).orElseThrow(()->new NotFoundException(Game.class.getSimpleName()));
        if (inDb.getBlack()==null&&game.getBlack()!=null)inDb.setBlack(game.getBlack());
        if (inDb.getWhite()==null&&game.getWhite()!=null)inDb.setWhite(game.getWhite());
        if (inDb.getResult().equals(GAME_RESULT.NOT_PLAYED)&&game.getResult()!=null)inDb.setResult(game.getResult());
        return new ResponseEntity<>(gameRepository.save(inDb), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<Void> deleteGameById(Long id) {
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

