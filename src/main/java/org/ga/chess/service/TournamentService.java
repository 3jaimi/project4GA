package org.ga.chess.service;


import lombok.Getter;
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
import org.ga.chess.service.util.ChessThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Setter
@Getter
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
    private final ReadWriteLock gameLock = new ReentrantReadWriteLock();
    public ResponseEntity<?> createTournament(Integer numOfPlayers) {
        if (UserService.getCurrentLoggedInUser().getUserType().equals(USER_TYPE.ADMIN)) {
            Tournament tournament = new Tournament(null, adminRepository.findByEmail(UserService.getCurrentLoggedInUser().getEmail()).get(), null, numOfPlayers, 0, TOURNAMENT_STATUS.PENDING_START);
            Tournament savedTournament = tournamentRepository.save(tournament);
            if (generateTournamentGames(generateTournamentGamesNum(savedTournament.getNumberOfPlayers()),savedTournament))
                return new ResponseEntity<>(HttpStatusCode.valueOf(200));
            else
                return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
        else return new ResponseEntity<>(HttpStatusCode.valueOf(401));
    }

    private int generateTournamentGamesNum(int numOfPlayers) {
        int returnedVal = 0;
        for (int i = numOfPlayers/2; i >= 1; i /= 2) {
            returnedVal += i;
        }
        System.out.println(returnedVal);
        return returnedVal;
    }

    private boolean generateTournamentGames(int numOfPlayers, Tournament tournament){
        boolean flag=false;
        for (int i = numOfPlayers; i > 0; i--) {
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

    public ResponseEntity<?> advanceInTournament(Long id, Player lastGameWinner, boolean newJoinee) {
            if (lastGameWinner.getUserType().equals(USER_TYPE.PLAYER)) {
                Tournament tournament = tournamentRepository.findById(id).orElseThrow(() -> new NotFoundException(Tournament.class.getSimpleName()));
                if (tournament.getNumberOfPlayersJoined() <= tournament.getNumberOfPlayers()) {
                    if (newJoinee&&tournament.getNumberOfPlayersJoined()+1<= tournament.getNumberOfPlayers()) {
                        tournament.setNumberOfPlayersJoined(tournament.getNumberOfPlayersJoined()+1);
                        tournamentRepository.save(tournament);
                    } else if (newJoinee&&tournament.getNumberOfPlayersJoined()+1> tournament.getNumberOfPlayers()) {
                        return new ResponseEntity<>(HttpStatusCode.valueOf(401));
                    }

                    for (TournamentGame tgame : tournamentGameRepository.findByTournamentOrderByGame_IdAsc(tournament)) {
//                        gameLock.writeLock().lock();
//                        try {
                            if (tgame.getGame().getWhite() == null) {
                                Game game = tgame.getGame();
                                game.setWhite(lastGameWinner);
                                gameRepository.save(game);
                                return new ResponseEntity<>(HttpStatusCode.valueOf(200));
                            } else if (tgame.getGame().getBlack() == null) {
                                Game game = tgame.getGame();
                                game.setBlack(lastGameWinner);
                                gameRepository.save(game);
                                return new ResponseEntity<>(HttpStatusCode.valueOf(200));
                            }
//                        }
//                        finally {
//                            gameLock.writeLock().unlock();
//                        }
                    }

                }

            }
        return new ResponseEntity<>(HttpStatusCode.valueOf(401));
    }
    public ResponseEntity<?> joinTournament(Long id) {
        Player player=playerRepository.findById(UserService.getCurrentLoggedInUser().getUserId()).orElseThrow(()->new NotFoundException(Player.class.getSimpleName()));
        return advanceInTournament(id,player, true);
    }



    public ResponseEntity<?> playTournament(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Tournament.class.getSimpleName()));
        if (tournament.getNumberOfPlayersJoined() == tournament.getNumberOfPlayers()&&tournament.getStatus().equals(TOURNAMENT_STATUS.PENDING_START)) {
            ArrayList<HashMap<String, String>> gameResults = new ArrayList<>();
            List<TournamentGame> tournamentGames = tournamentGameRepository.findByTournamentOrderByGame_IdAsc(tournament);
            int index = 0;
            for (int i = (tournament.getNumberOfPlayers() / 2); i >= 1; i /= 2) {
                CopyOnWriteArrayList<Boolean> flags=new CopyOnWriteArrayList<>();
                ExecutorService executorService = Executors.newFixedThreadPool(i);
                    for (int j = 0; j < i; j++) {
                        TournamentGame temp = tournamentGames.get(index + j);
                        executorService.submit(new DelegatingSecurityContextRunnable(new ChessThread(this, temp, gameResults,flags), SecurityContextHolder.getContext()));
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    index += i;
                    boolean done = false;
                    while(!done){
                        if (flags.size()==i){
                            System.out.println(flags.size());
                            executorService.shutdownNow();
                            done=true;
                        }
                    }

            }
            tournament.setStatus(TOURNAMENT_STATUS.STARTED);
            tournamentRepository.save(tournament);
            return new ResponseEntity<>(gameResults, HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>("Tournament is not full", HttpStatusCode.valueOf(401));
        }
    }
    public ResponseEntity<?> playTournamentWithoutThreads(Long id){
        Tournament tournament = tournamentRepository.findById(id).orElseThrow(() -> new NotFoundException(Tournament.class.getSimpleName()));
        if (tournament.getNumberOfPlayersJoined() == tournament.getNumberOfPlayers()) {
            ArrayList<HashMap<String,String>> gameResults=new ArrayList<>();
            for (TournamentGame tgame : tournamentGameRepository.findByTournamentOrderByGame_IdAsc(tournament)) {
                GAME_RESULT gameResult=tgame.getGame().getResult();
                while(gameResult.equals(GAME_RESULT.NOT_PLAYED)){
                    Game game=gameService.playGame(tgame.getGame().getId());
                    GAME_RESULT result=game.getResult();
                    if (result.equals(GAME_RESULT.DRAW)){
                        Player black=tgame.getGame().getBlack();
                        tgame.getGame().setBlack(tgame.getGame().getWhite());
                        tgame.getGame().setWhite(black);
                    }
                    else{

                        if (result.equals(GAME_RESULT.BLACK_WON)){
                            advanceInTournament(id,tgame.getGame().getBlack(),false);
                            gameResult=GAME_RESULT.BLACK_WON;
                        }
                        else {
                            advanceInTournament(id, tgame.getGame().getWhite(),false);
                            gameResult=GAME_RESULT.WHITE_WON;
                        }
                        HashMap<String,String> gameResponseMap;
                        Object gameResponse=gameService.saveGame(game).getBody();
                        gameResponseMap=(HashMap<String,String>)gameResponse;
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

    public ResponseEntity<?> playTournamentDeprecated(Long id){
        Tournament tournament = tournamentRepository.findById(id).orElseThrow(() -> new NotFoundException(Tournament.class.getSimpleName()));
        if (tournament.getNumberOfPlayersJoined() == tournament.getNumberOfPlayers()) {
            ArrayList<HashMap<String,String>> gameResults=new ArrayList<>();
            List<TournamentGame> tournamentGames=tournamentGameRepository.findByTournamentOrderByGame_IdAsc(tournament);
            int index=0;
            for (int i=(tournament.getNumberOfPlayers()/2);i>=1;i/=2) {
                for (int j=0; j<i; j++){
                    TournamentGame temp=tournamentGames.get(index+j);
                    SecurityContext context=SecurityContextHolder.getContext();
                    Runnable task = new DelegatingSecurityContextRunnable(new ChessThread(this,temp,gameResults,new CopyOnWriteArrayList<>()),context );
                    new Thread(task).start();
                }
                index+=i;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return new ResponseEntity<>(gameResults,HttpStatusCode.valueOf(200));
        }
        else{
            return new ResponseEntity<>("Tournament is not full", HttpStatusCode.valueOf(401));
        }
    }

    public ResponseEntity<?> playTournamentDeprecated2(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Tournament.class.getSimpleName()));
        if (tournament.getNumberOfPlayersJoined() == tournament.getNumberOfPlayers()&&tournament.getStatus().equals(TOURNAMENT_STATUS.PENDING_START)) {
            ArrayList<HashMap<String, String>> gameResults = new ArrayList<>();
            List<TournamentGame> tournamentGames = tournamentGameRepository.findByTournamentOrderByGame_IdAsc(tournament);
            int index = 0;
            for (int i = (tournament.getNumberOfPlayers() / 2); i >= 1; i /= 2) {
                CopyOnWriteArrayList<Boolean> flags=new CopyOnWriteArrayList<>();
                ExecutorService executorService = Executors.newFixedThreadPool(i);
                List<Callable<Object>> tasks = new ArrayList<>();
                for (int j = 0; j < i; j++) {
                    TournamentGame temp = tournamentGames.get(index + j);
                    tasks.add(Executors.callable(new DelegatingSecurityContextRunnable(new ChessThread(this, temp, gameResults,flags), SecurityContextHolder.getContext())));
                }
                index += i;
                try {
                    executorService.invokeAll(tasks);
                    boolean done = false;
                    double c=0;
                    while(!done){
                        if (flags.size()==i){
                            executorService.shutdownNow();
                            done=true;
                        }
                        if (c%10000000==0)
                            System.out.print(flags.size());
                        c++;
                    }
                } catch (InterruptedException e ) {
                    return new ResponseEntity<>( HttpStatusCode.valueOf(400));
                }
            }
            tournament.setStatus(TOURNAMENT_STATUS.STARTED);
            tournamentRepository.save(tournament);
            return new ResponseEntity<>(gameResults, HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>("Tournament is not full", HttpStatusCode.valueOf(401));
        }
    }

    public ResponseEntity<?> playTournamentDeprecated3(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Tournament.class.getSimpleName()));

        if (tournament.getNumberOfPlayersJoined() == tournament.getNumberOfPlayers()
                && tournament.getStatus().equals(TOURNAMENT_STATUS.PENDING_START)) {

            ArrayList<HashMap<String, String>> gameResults = new ArrayList<>();
            List<TournamentGame> tournamentGames = tournamentGameRepository.findByTournamentOrderByGame_IdAsc(tournament);
            int index = 0;

            for (int i = (tournament.getNumberOfPlayers() / 2); i >= 1; i /= 2) {
                ExecutorService executorService = Executors.newFixedThreadPool(i);
                List<Callable<Object>> tasks = new ArrayList<>();

                for (int j = 0; j < i; j++) {
                    TournamentGame temp = tournamentGames.get(index + j);
                    tasks.add(Executors.callable(new DelegatingSecurityContextRunnable(
                            new ChessThread(this, temp, gameResults, new CopyOnWriteArrayList<>()), SecurityContextHolder.getContext())));
                }
                index += i;

                try {

                    List<Future<Object>> futures = executorService.invokeAll(tasks);
                    for (Future<Object> future : futures) {
                        try {
                            future.get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();  // Log the exception or handle it as needed
                            return new ResponseEntity<>(HttpStatusCode.valueOf(500));  // Return an error response if a task fails
                        }
                    }

                    executorService.shutdown();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // Reset the interrupt flag
                    return new ResponseEntity<>(HttpStatusCode.valueOf(400));
                }
            }

            tournament.setStatus(TOURNAMENT_STATUS.STARTED);
            tournamentRepository.save(tournament);
            return new ResponseEntity<>(gameResults, HttpStatusCode.valueOf(200));

        } else {
            return new ResponseEntity<>("Tournament is not full", HttpStatusCode.valueOf(401));
        }
    }

}