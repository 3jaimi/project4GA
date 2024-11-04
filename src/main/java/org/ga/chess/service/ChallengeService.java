package org.ga.chess.service;

import lombok.Setter;
import org.ga.chess.ENUM.CHALLENGE_STATUS;
import org.ga.chess.ENUM.GAME_RESULT;
import org.ga.chess.ENUM.PLAYER_COLOUR;
import org.ga.chess.exception.NotFoundException;
import org.ga.chess.model.Challenge;
import org.ga.chess.model.Game;
import org.ga.chess.model.Player;
import org.ga.chess.repository.IChallengeRepository;
import org.ga.chess.repository.IGameRepository;
import org.ga.chess.repository.IPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Setter
public class ChallengeService {
    @Autowired
    private IChallengeRepository challengeRepository;

    @Autowired
    private IPlayerRepository playerRepository;

    @Autowired
    private IGameRepository gameRepository;

    public ResponseEntity<?> issueChallenge(String email, PLAYER_COLOUR challengeeColour){
        Player challengee=playerRepository.findByEmail(email).orElseThrow(()->new NotFoundException(Player.class.getSimpleName()));
        Challenge challenge = new Challenge(null, playerRepository.findByEmail(UserService.getCurrentLoggedInUser().getEmail()).orElseThrow(()->new NotFoundException(Player.class.getSimpleName())),challengee,challengeeColour, CHALLENGE_STATUS.PENDING);
        return new ResponseEntity<>(challengeRepository.save(challenge), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> deleteChallenge(Long id){
        Challenge challenge= challengeRepository.findById(id).orElseThrow(()->new NotFoundException(Challenge.class.getSimpleName()));
        if (challenge.getStatus().equals(CHALLENGE_STATUS.PENDING) && challenge.getChallenger().getEmail().equals(UserService.getCurrentLoggedInUser().getEmail())){
            challengeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        }
        else return new ResponseEntity<>(HttpStatusCode.valueOf(401));
    }

    public ResponseEntity<?>processChallenge(Long id, CHALLENGE_STATUS status){
        Challenge challenge= challengeRepository.findById(id).orElseThrow(()->new NotFoundException(Challenge.class.getSimpleName()));
        if (challenge.getStatus().equals(CHALLENGE_STATUS.PENDING) && challenge.getChallengee().getEmail().equals(UserService.getCurrentLoggedInUser().getEmail())){
            challenge.setStatus(status);
            if (status.equals(CHALLENGE_STATUS.ACCEPTED)) {
                Game game;
                if (challenge.getChallengeeColour().equals(PLAYER_COLOUR.BLACK))
                    game= new Game(null,challenge.getChallengee(),challenge.getChallenger(), GAME_RESULT.NOT_PLAYED );
                else
                    game= new Game(null,challenge.getChallenger(),challenge.getChallengee(), GAME_RESULT.NOT_PLAYED );

                return new ResponseEntity<>(gameRepository.save(game),HttpStatusCode.valueOf(200));
            }
            return new ResponseEntity<>(challengeRepository.save(challenge),HttpStatusCode.valueOf(200));
        }
        else return new ResponseEntity<>(HttpStatusCode.valueOf(401));
    }



}
