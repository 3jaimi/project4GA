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
import org.ga.chess.requestUtil.LoginRequest;
import org.ga.chess.requestUtil.SignUpRequest;
import org.ga.chess.security.EmailUtil;
import org.ga.chess.security.JwtUtil;
import org.ga.chess.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Setter
public class UserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IAdminRepository adminRepository;
    @Autowired
    private IPlayerRepository playerRepository;
    @Autowired
    @Lazy
    private JwtUtil jwtUtils;
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;
    @Autowired
    @Lazy
    private MyUserDetails myUserDetails;
    @Autowired
    @Lazy
    private EmailUtil emailUtil;
    @Value("${jwt-verification-secret}")
    private String secret;

    public ResponseEntity<?> createUser(SignUpRequest signUpRequest){
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent())
            throw new AlreadyExistsException(User.class.getSimpleName());
        User user= new User(null,signUpRequest.getUserType(),signUpRequest.getEmail(), signUpRequest.getPassword(), null);
        user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
        if (user.getUserType().equals(USER_TYPE.ADMIN)){
            return new ResponseEntity<>(adminRepository.save(new Admin(user.getUserId(),user.getUserType(),user.getEmail(), user.getPassword())), HttpStatusCode.valueOf(200));
        }
        else{
             ResponseEntity<?> response=new ResponseEntity<>(playerRepository.save(new Player(user.getUserId(),user.getUserType(),user.getEmail(), user.getPassword(),400, USER_STATUS.UNVERIFIED)),HttpStatusCode.valueOf(200));
             emailUtil.sendVerificationEmail(user);
             return response;
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

    public ResponseEntity<?> verifyEmail(String token) {
        try {
            if (!jwtUtils.validateToken(token,secret))
                return new ResponseEntity<>(HttpStatusCode.valueOf(401));

            String email = jwtUtils.getUserNameFromToken(token,secret);

            Player player = playerRepository.findByEmail(email).orElseThrow(()->new NotFoundException(Player.class.getSimpleName()));

            if (player.getStatus().equals(USER_STATUS.UNVERIFIED)){
                    player.setStatus(USER_STATUS.ACTIVE);
                    playerRepository.save(player);
            }
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(401));
        }
    }

    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        Optional<Player> player=playerRepository.findByEmail(loginRequest.getEmail());
        if (
                player.isEmpty() || !player.get().getStatus().equals(USER_STATUS.UNVERIFIED)||
                (loginRequest.getVerificationToken()!=null&&(verifyEmail(loginRequest.getVerificationToken()).getStatusCode().value()==200))
        ){
            if ((loginRequest.getEmail()!=null&&loginRequest.getPassword()!=null)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
                try {
                    Authentication authentication = authenticationManager
                            .authenticate(authenticationToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    myUserDetails = (MyUserDetails) authentication.getPrincipal();
                    final String JWT = jwtUtils.generateToken(myUserDetails);
                    return new ResponseEntity<>("Token: ".concat(JWT),HttpStatusCode.valueOf(200));
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatusCode.valueOf(401));
                }
            }
        }
        return new ResponseEntity<>("Bad Request",HttpStatusCode.valueOf(400));
    }

    public static User getCurrentLoggedInUser(){
        MyUserDetails userDetails=(MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }

}
