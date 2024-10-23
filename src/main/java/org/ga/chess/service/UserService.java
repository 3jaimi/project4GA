package org.ga.chess.service;

import lombok.Setter;
import org.ga.chess.ENUM.USER_TYPE;
import org.ga.chess.exception.AlreadyExistsException;
import org.ga.chess.exception.NotFoundException;
import org.ga.chess.model.Admin;
import org.ga.chess.model.Player;
import org.ga.chess.model.User;
import org.ga.chess.repository.IAdminRepository;
import org.ga.chess.repository.IPlayerRepository;
import org.ga.chess.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Setter
public class UserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IAdminRepository adminRepository;
    @Autowired
    private IPlayerRepository playerRepository;

    public ResponseEntity<?> createUser(User user){
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new AlreadyExistsException(User.class.getSimpleName());
        if (user.getUserType().equals(USER_TYPE.ADMIN)){
            return new ResponseEntity<>(adminRepository.save(new Admin(user.getUserId(),user.getUserType(),user.getEmail(), user.getPassword())), HttpStatusCode.valueOf(200));
        }
        else{
            return new ResponseEntity<>(playerRepository.save(new Player(user.getUserId(),user.getUserType(),user.getEmail(), user.getPassword(),400)),HttpStatusCode.valueOf(200));
        }
    }

    public ResponseEntity<?> getUser(String email){
        return new ResponseEntity<>(userRepository.findByEmail(email).orElseThrow(()->new NotFoundException(User.class.getSimpleName())),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> getUser(Long id){
        return new ResponseEntity<>(userRepository.findById(id).orElseThrow(()->new NotFoundException(User.class.getSimpleName())),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?>getAllUsers(){
        return new ResponseEntity<>(userRepository.findAll(),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> getAdmin(String email){
        return new ResponseEntity<>(adminRepository.findByEmail(email).orElseThrow(()->new NotFoundException(User.class.getSimpleName())), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> getAdmin(Long id){
        return new ResponseEntity<>(adminRepository.findById(id).orElseThrow(()->new NotFoundException(User.class.getSimpleName())),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?>getAllAdmins(){
        return new ResponseEntity<>(adminRepository.findAll(),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> getPlayer(String email){
        return new ResponseEntity<>(playerRepository.findByEmail(email).orElseThrow(()->new NotFoundException(User.class.getSimpleName())), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> getPlayer(Long id){
        return new ResponseEntity<>(playerRepository.findById(id).orElseThrow(()->new NotFoundException(User.class.getSimpleName())),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?>getAllPlayers(){
        return new ResponseEntity<>(playerRepository.findAll(),HttpStatusCode.valueOf(200));
    }
}
