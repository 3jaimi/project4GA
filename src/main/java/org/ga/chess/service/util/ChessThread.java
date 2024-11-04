package org.ga.chess.service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ga.chess.ENUM.GAME_RESULT;
import org.ga.chess.model.Game;
import org.ga.chess.model.Player;
import org.ga.chess.model.TournamentGame;

import org.ga.chess.security.MyUserDetails;
import org.ga.chess.service.TournamentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
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
    private CopyOnWriteArrayList<Boolean> flags;

    public ChessThread(TournamentService tournamentService, TournamentGame tournamentGame, ArrayList<HashMap<String, String>> gameResults,  CopyOnWriteArrayList<Boolean> flags) {
        this.tournamentService = tournamentService;
        this.tournamentGame = tournamentGame;
        this.gameResults = gameResults;
        this.flags=flags;
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
                Game game = tournamentService.getGameService().playGame(tournamentGame.getGame().getId());
                GAME_RESULT result=game.getResult();
//                System.out.println(game.getId()+"  "+result.toString());
                if (result.equals(GAME_RESULT.DRAW)) {
                    Player black = tournamentGame.getGame().getBlack();
                    tournamentGame.getGame().setBlack(tournamentGame.getGame().getWhite());
                    tournamentGame.getGame().setWhite(black);
                } else{
                    if (result.equals(GAME_RESULT.BLACK_WON)) {
                        if (tournamentGame.getGame().getBlack()!=null)
                            tournamentService.advanceInTournament(tournamentGame.getTournament().getId(), tournamentGame.getGame().getBlack(), false);
                        gameResult = GAME_RESULT.BLACK_WON;

                    } else {
                        if (tournamentGame.getGame().getWhite()!=null)
                            tournamentService.advanceInTournament(tournamentGame.getTournament().getId(), tournamentGame.getGame().getWhite(), false);
                        gameResult = GAME_RESULT.WHITE_WON;
                    }

                    HashMap<String,String> gameResponseMap;
                    Object gameResponse=tournamentService.getGameService().saveGame(game).getBody();
                    gameResponseMap=(HashMap<String,String>)gameResponse;
                    gameResults.add(gameResponseMap);
                    flags.add(true);
                }
            }
        } finally {
            gameLock.writeLock().unlock();
        }

    }
}
