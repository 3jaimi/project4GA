package org.ga.chess.service;


import org.ga.chess.ENUM.GAME_RESULT;
import org.ga.chess.exception.NotFoundException;
import org.ga.chess.model.Game;
import org.ga.chess.model.Player;
import org.ga.chess.repository.IGameRepository;
import org.ga.chess.repository.IPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class GameService {

    @Autowired
    private IGameRepository gameRepository;

    @Autowired
    private IPlayerRepository playerRepository;

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

    public ResponseEntity<?> playGame(Long id){
        Game game=gameRepository.findById(id).orElseThrow(()->new NotFoundException(Game.class.getSimpleName()));
        if ((UserService.getCurrentLoggedInUser().getEmail().equals(game.getBlack().getEmail())||UserService.getCurrentLoggedInUser().getEmail().equals(game.getWhite().getEmail()))&&game.getResult().equals(GAME_RESULT.NOT_PLAYED)){
            GAME_RESULT [] resultArr  = new GAME_RESULT[]{GAME_RESULT.DRAW,GAME_RESULT.BLACK_WON,GAME_RESULT.WHITE_WON};
            Random random = new Random();
            int result=random.nextInt(3);
            game.setResult(resultArr[result]);
            if (result==2)
                for (Player player:processGameResult(game.getWhite(),game.getBlack(),resultArr[2])){
                    playerRepository.save(player);
                }
            else if (result==1) {
                for (Player player:processGameResult(game.getBlack(),game.getWhite(),resultArr[2])){
                    playerRepository.save(player);
                }

            }
            else {
                if (game.getWhite().getRating()>game.getBlack().getRating())
                    for (Player player:processGameResult(game.getBlack(),game.getWhite(),resultArr[2])){
                        playerRepository.save(player);
                    }
                else
                    for (Player player:processGameResult(game.getWhite(),game.getBlack(),resultArr[2])){
                        playerRepository.save(player);
                    }
            }
            return new ResponseEntity<>(gameRepository.save(game),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(401));
    }

    private Player[] processGameResult(Player winner, Player loser, GAME_RESULT result){
        int change=0;
        if (!result.equals(GAME_RESULT.DRAW))
            change+=8;
        for (int i=Math.abs(winner.getRating()-loser.getRating());(i-24)>=0;i-=25){
            change++;
        }
        winner.setRating(winner.getRating()+change);
        loser.setRating(loser.getRating()-change);
        return new Player[]{winner,loser};
    }
}

