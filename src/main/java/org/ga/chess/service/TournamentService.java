package org.ga.chess.service;


import lombok.Setter;
import org.ga.chess.ENUM.GAME_RESULT;
import org.ga.chess.ENUM.TOURNAMENT_STATUS;
import org.ga.chess.ENUM.USER_TYPE;
import org.ga.chess.exception.NotFoundException;
import org.ga.chess.model.Game;
import org.ga.chess.model.Player;
import org.ga.chess.model.Tournament;
import org.ga.chess.model.TournamentGame;
import org.ga.chess.model.util.TournamentGameId;
import org.ga.chess.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Setter
public class TournamentService {
    @Autowired
    private ITournamentRepository tournamentRepository;
    @Autowired
    private ITournamentGameRepository tournamentGameRepository;
    @Autowired
    private IAdminRepository adminRepository;
    @Autowired
    private IGameRepository gameRepository;
    @Autowired
    private IPlayerRepository playerRepository;
    @Autowired
    private GameService gameService;

    public ResponseEntity<?> createTournament(Integer numOfPlayers) {
        if (UserService.getCurrentLoggedInUser().getUserType().equals(USER_TYPE.ADMIN)) {
            Tournament tournament = new Tournament(null, adminRepository.findByEmail(UserService.getCurrentLoggedInUser().getEmail()).get(), null, numOfPlayers, 0, TOURNAMENT_STATUS.PENDING_START);
            Tournament savedTournament = tournamentRepository.save(tournament);
            if (generateTournamentGames(generateTournamentGamesNum(tournament.getNumberOfPlayers()),tournament))
                return new ResponseEntity<>(HttpStatusCode.valueOf(200));
            else
                return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
        else return new ResponseEntity<>(HttpStatusCode.valueOf(401));
    }

    private int generateTournamentGamesNum(int numOfPlayers) {
        int returnedVal = 0;
        for (int i = numOfPlayers; i < 1; ) {
            returnedVal += i;
            i /= 2;
        }
        return returnedVal;
    }

    private boolean generateTournamentGames(int numOfPlayers, Tournament tournament){
        boolean flag=false;
        for (int i = generateTournamentGamesNum(numOfPlayers); i > 0; i--) {
            Game game = new Game(null, null, null, GAME_RESULT.NOT_PLAYED);
            Game savedGame = gameRepository.save(game);
            TournamentGameId gameId = new TournamentGameId(tournament.getId(), savedGame.getId());
            TournamentGame temp = new TournamentGame(gameId, tournament, savedGame);
            TournamentGame savedTemp =tournamentGameRepository.save(temp);
            if (savedTemp==null){
                flag=false;
                break;
            }
            else flag=true;
        }
        return flag;
    }

    public ResponseEntity<?> getTournament(Long id) {
        return new ResponseEntity<>(tournamentRepository.findById(id).orElseThrow(() -> new NotFoundException(Tournament.class.getSimpleName())), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> getAllNotStartedTournaments(){
        return new ResponseEntity<>(tournamentRepository.findByStatus(TOURNAMENT_STATUS.PENDING_START), HttpStatusCode.valueOf(200));

    }

    public ResponseEntity<?> advanceInTournament(Long id, Player lastGameWinner) {
        if (lastGameWinner.getUserType().equals(USER_TYPE.PLAYER)) {
            Tournament tournament = tournamentRepository.findById(id).orElseThrow(() -> new NotFoundException(Tournament.class.getSimpleName()));
            if (tournament.getNumberOfPlayersJoined() < tournament.getNumberOfPlayers()) {
                for (TournamentGame tgame : tournament.getGames()) {
                    if (tgame.getGame().getWhite() == null) {
                        Game game = tgame.getGame();
                        game.setWhite(playerRepository.findById(UserService.getCurrentLoggedInUser().getUserId()).get());
                        gameRepository.save(game);
                        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
                    }
                    else if (tgame.getGame().getBlack() == null) {
                        Game game = tgame.getGame();
                        game.setBlack(playerRepository.findById(UserService.getCurrentLoggedInUser().getUserId()).get());
                        gameRepository.save(game);
                        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
                    }
                }
            }

        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(401));
    }
    public ResponseEntity<?> joinTournament(Long id) {
        Player player=playerRepository.findById(UserService.getCurrentLoggedInUser().getUserId()).get();
        return advanceInTournament(id,player);
    }

    public ResponseEntity<?> playTournament(Long id){
        Tournament tournament = tournamentRepository.findById(id).orElseThrow(() -> new NotFoundException(Tournament.class.getSimpleName()));
        if (tournament.getNumberOfPlayersJoined() == tournament.getNumberOfPlayers()) {
            ArrayList<HashMap<String,String>> gameResults=new ArrayList<>();
            for (TournamentGame tgame : tournament.getGames()) {
                GAME_RESULT gameResult=tgame.getGame().getResult();
                while(gameResult.equals(GAME_RESULT.NOT_PLAYED)){
                    Object gameResponse=gameService.playGame(tgame.getGame().getId()).getBody();
                    HashMap<String,String> gameResponseMap=(HashMap<String,String>)gameResponse;
                    String result=gameResponseMap.get("Result:");
                    if (result.equals(GAME_RESULT.DRAW)){
                        Player black=tgame.getGame().getBlack();
                        tgame.getGame().setBlack(tgame.getGame().getWhite());
                        tgame.getGame().setWhite(black);
                    }
                    else{
                        if (result.equals(GAME_RESULT.BLACK_WON)){
                        advanceInTournament(id,tgame.getGame().getBlack());
                        gameResult=GAME_RESULT.BLACK_WON;

                        }
                        else {
                            advanceInTournament(id, tgame.getGame().getWhite());
                            gameResult=GAME_RESULT.WHITE_WON;
                        }
                        gameResults.add(gameResponseMap);
                    }
                }
            }
            return new ResponseEntity<>(gameResults,HttpStatusCode.valueOf(200));
        }
        else{
            return new ResponseEntity<>("Tournament is not full", HttpStatusCode.valueOf(401));
        }
    }
}