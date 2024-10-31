package org.ga.chess.service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ga.chess.ENUM.GAME_RESULT;
import org.ga.chess.model.Player;
import org.ga.chess.model.TournamentGame;

import org.ga.chess.security.MyUserDetails;
import org.ga.chess.service.TournamentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChessThread extends Thread{

    private TournamentService tournamentService;
    private TournamentGame tournamentGame;
    private ArrayList<HashMap<String,String>> gameResults;
    private ReadWriteLock gameLock = new ReentrantReadWriteLock();
    private boolean isFinal;

    public ChessThread(TournamentService tournamentService, TournamentGame tournamentGame, ArrayList<HashMap<String, String>> gameResults) {
        this.tournamentService = tournamentService;
        this.tournamentGame = tournamentGame;
        this.gameResults = gameResults;
    }

    @Override
    public void run() {
            processGame();
    }

    public void processGame() {
        gameLock.writeLock().lock();
        try{
            GAME_RESULT gameResult = tournamentGame.getGame().getResult();
            while (gameResult.equals(GAME_RESULT.NOT_PLAYED)) {
                Object gameResponse = tournamentService.getGameService().playGame(tournamentGame.getGame().getId()).getBody();
                HashMap<String, String> gameResponseMap = (HashMap<String, String>) gameResponse;
                String result = gameResponseMap.get("Result");
                System.out.println(result);
                if (result.equals(GAME_RESULT.DRAW.toString())) {
                    Player black = tournamentGame.getGame().getBlack();
                    tournamentGame.getGame().setBlack(tournamentGame.getGame().getWhite());
                    tournamentGame.getGame().setWhite(black);
                } else{
                    if (result.equals(GAME_RESULT.BLACK_WON.toString())) {
                        if (tournamentGame.getGame().getBlack()!=null)
                            tournamentService.advanceInTournament(tournamentGame.getTournament().getId(), tournamentGame.getGame().getBlack(), false);
                        gameResult = GAME_RESULT.BLACK_WON;
                        gameResponseMap.put("Result", gameResult.toString());
                    } else {
                        if (tournamentGame.getGame().getWhite()!=null)
                            tournamentService.advanceInTournament(tournamentGame.getTournament().getId(), tournamentGame.getGame().getWhite(), false);
                        gameResult = GAME_RESULT.WHITE_WON;
                        gameResponseMap.put("Result", gameResult.toString());
                    }
                    gameResults.add(gameResponseMap);
                }
            }
        }
        finally {
            gameLock.writeLock().unlock();
        }

    }
}
