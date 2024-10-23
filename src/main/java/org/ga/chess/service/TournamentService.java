package org.ga.chess.service;


import lombok.Setter;
import org.ga.chess.ENUM.GAME_RESULT;
import org.ga.chess.ENUM.TOURNAMENT_STATUS;
import org.ga.chess.ENUM.USER_TYPE;
import org.ga.chess.exception.NotFoundException;
import org.ga.chess.model.Game;
import org.ga.chess.model.Tournament;
import org.ga.chess.model.TournamentGame;
import org.ga.chess.model.util.TournamentGameId;
import org.ga.chess.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

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

    public ResponseEntity<?> createTournament(Integer numOfPlayers) {
        if (UserService.getCurrentLoggedInUser().getUserType().equals(USER_TYPE.ADMIN)) {
            Tournament tournament = new Tournament(null, adminRepository.findByEmail(UserService.getCurrentLoggedInUser().getEmail()).get(), null, numOfPlayers, 0, TOURNAMENT_STATUS.PENDING_START);
            Tournament savedTournament = tournamentRepository.save(tournament);
            List<TournamentGame> tournamentGames = new ArrayList<>();
            for (int i = generateTournamentGames(numOfPlayers); i > 0; i--) {
                Game game = new Game(null, null, null, GAME_RESULT.NOT_PLAYED);
                Game savedGame = gameRepository.save(game);
                TournamentGameId gameId = new TournamentGameId(savedTournament.getId(), savedTournament.getId());
                TournamentGame temp = new TournamentGame(null, savedTournament, savedGame);
                tournamentGameRepository.save(temp);
            }
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } else return new ResponseEntity<>(HttpStatusCode.valueOf(401));
    }

    private int generateTournamentGames(int numOfPlayers) {
        int returnedVal = 0;
        for (int i = numOfPlayers; i < 1; ) {
            returnedVal += i;
            i /= 2;
        }
        return returnedVal;
    }

    public ResponseEntity<?> getTournament(Long id) {
        return new ResponseEntity<>(tournamentRepository.findById(id).orElseThrow(() -> new NotFoundException(Tournament.class.getSimpleName())), HttpStatusCode.valueOf(200));
    }


    public ResponseEntity<?> joinTournament(Long id) {
        if (UserService.getCurrentLoggedInUser().getUserType().equals(USER_TYPE.PLAYER)) {
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
}