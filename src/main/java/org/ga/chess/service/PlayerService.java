package org.ga.chess.service;

import lombok.Setter;
import org.ga.chess.exception.AlreadyExistsException;
import org.ga.chess.exception.NotFoundException;
import org.ga.chess.model.Player;
import org.ga.chess.model.User;
import org.ga.chess.repository.IPlayerRepository;
import org.ga.chess.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Setter
public class PlayerService {
    @Autowired
    private IPlayerRepository playerRepository;
    @Autowired
    private IUserRepository userRepository;
    
    public ResponseEntity<?> editPlayer(Player player){
        Player playerInDb=playerRepository.findByEmail(UserService.getCurrentLoggedInUser().getEmail()).orElseThrow(()->new NotFoundException(Player.class.getSimpleName()));
        if (!(player.getEmail().isEmpty()||playerInDb.getEmail().equals(player.getEmail()))){
            if(userRepository.findByEmail(player.getEmail()).isPresent()) throw new AlreadyExistsException(User.class.getSimpleName());
            playerInDb.setEmail(player.getEmail());
        }
        if (!player.getPassword().isEmpty()) playerInDb.setPassword(playerInDb.getPassword());
        return new ResponseEntity<>(playerRepository.save(playerInDb),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> deletePlayer(){
        playerRepository.deleteById(UserService.getCurrentLoggedInUser().getUserId());
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
