package org.ga.chess.service;

import lombok.Setter;
import org.ga.chess.ENUM.USER_STATUS;
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
public class AdminService {
    @Autowired
    private IAdminRepository adminRepository;
    @Autowired
    private IPlayerRepository playerRepository;
    @Autowired
    private IUserRepository userRepository;

    public ResponseEntity<?> getAdmin(String email){
        return new ResponseEntity<>(adminRepository.findByEmail(email).orElseThrow(()->new NotFoundException(User.class.getSimpleName())), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> getAdmin(Long id){
        return new ResponseEntity<>(adminRepository.findById(id).orElseThrow(()->new NotFoundException(User.class.getSimpleName())),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?>getAllAdmins(){
        return new ResponseEntity<>(adminRepository.findAll(),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?>editAdmin(Admin admin){
        Admin adminInDb=adminRepository.findByEmail(UserService.getCurrentLoggedInUser().getEmail()).orElseThrow(()->new NotFoundException(Admin.class.getSimpleName()));
        if (!(admin.getEmail().isEmpty()||adminInDb.getEmail().equals(admin.getEmail()))){
            if(userRepository.findByEmail(admin.getEmail()).isPresent()) throw new AlreadyExistsException(User.class.getSimpleName());
            adminInDb.setEmail(admin.getEmail());
        }
        if (!admin.getPassword().isEmpty()) adminInDb.setPassword(adminInDb.getPassword());
        if (admin.getTournaments()!=null) adminInDb.setTournaments(admin.getTournaments());
        return new ResponseEntity<>(adminRepository.save(adminInDb),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> deleteAdmin(){
        adminRepository.deleteById(UserService.getCurrentLoggedInUser().getUserId());
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> promotePlayer (String email){
        Player tempPLayer = playerRepository.findByEmail(email).orElseThrow(()->new NotFoundException(User.class.getSimpleName()));
        Admin newAdmin=new Admin(tempPLayer.getUserId(), USER_TYPE.ADMIN, tempPLayer.getEmail(), tempPLayer.getPassword());
        playerRepository.deleteById(tempPLayer.getUserId());
        return new ResponseEntity<>(adminRepository.save(newAdmin),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> deactivatePlayer(String email){
       Player tempPLayer = playerRepository.findByEmail(email).orElseThrow(()->new NotFoundException(User.class.getSimpleName()));
       tempPLayer.setStatus(USER_STATUS.INACTIVE);
       return new ResponseEntity<>(playerRepository.save(tempPLayer),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> deactivatePlayer(Long id){
        Player tempPLayer = playerRepository.findById(id).orElseThrow(()->new NotFoundException(User.class.getSimpleName()));
        tempPLayer.setStatus(USER_STATUS.INACTIVE);
        return new ResponseEntity<>(playerRepository.save(tempPLayer),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> activatePlayer(String email){
        Player tempPLayer = playerRepository.findByEmail(email).orElseThrow(()->new NotFoundException(User.class.getSimpleName()));
        tempPLayer.setStatus(USER_STATUS.ACTIVE);
        return new ResponseEntity<>(playerRepository.save(tempPLayer),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> activatePlayer(Long id){
        Player tempPLayer = playerRepository.findById(id).orElseThrow(()->new NotFoundException(User.class.getSimpleName()));
        tempPLayer.setStatus(USER_STATUS.ACTIVE);
        return new ResponseEntity<>(playerRepository.save(tempPLayer),HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> deletePlayer(String email){
        Player tempPLayer = playerRepository.findByEmail(email).orElseThrow(()->new NotFoundException(User.class.getSimpleName()));
        playerRepository.deleteById(tempPLayer.getUserId());
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> deletePlayer(Long id){
        Player tempPLayer = playerRepository.findById(id).orElseThrow(()->new NotFoundException(User.class.getSimpleName()));
        playerRepository.deleteById(tempPLayer.getUserId());
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }


}
