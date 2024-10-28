package org.ga.chess.service;

import lombok.Setter;
import org.ga.chess.ENUM.USER_STATUS;
import org.ga.chess.exception.AlreadyExistsException;
import org.ga.chess.exception.NotFoundException;
import org.ga.chess.model.Player;
import org.ga.chess.model.User;
import org.ga.chess.repository.IPlayerRepository;
import org.ga.chess.repository.IUserRepository;
import org.ga.chess.security.JwtUtil;
import org.ga.chess.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@Setter
public class PlayerService {
    @Autowired
    private IPlayerRepository playerRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    @Lazy
    private JwtUtil jwtUtils;
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;
    @Autowired
    @Lazy
    private MyUserDetails myUserDetails;

    public ResponseEntity<?> getPlayer(String email){
        return new ResponseEntity<>(playerRepository.findByEmail(email).orElseThrow(()->new NotFoundException(User.class.getSimpleName())), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> getPlayer(Long id){
        return new ResponseEntity<>(playerRepository.findById(id).orElseThrow(()->new NotFoundException(User.class.getSimpleName())),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?>getAllPlayers(){
        return new ResponseEntity<>(playerRepository.findAll(),HttpStatusCode.valueOf(200));
    }

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
