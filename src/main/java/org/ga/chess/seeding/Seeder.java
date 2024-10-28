package org.ga.chess.seeding;

import lombok.Setter;
import org.ga.chess.ENUM.USER_STATUS;
import org.ga.chess.ENUM.USER_TYPE;
import org.ga.chess.model.Admin;
import org.ga.chess.model.Player;
import org.ga.chess.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Setter
public class Seeder implements CommandLineRunner {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IAdminRepository adminRepository;
    @Autowired
    private IPlayerRepository playerRepository;
    @Autowired
    private ITournamentRepository tournamentRepository;
    @Autowired
    private IGameRepository gameRepository;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
    }

    public void seedUsers(){
        if (userRepository.count()==0){
            Admin admin = new Admin(1L, USER_TYPE.ADMIN,"admin",new BCryptPasswordEncoder().encode("123"));
            Player player1 = new Player(2L, USER_TYPE.PLAYER,"player1",new BCryptPasswordEncoder().encode("123"),400, USER_STATUS.ACTIVE);
            Player player2 = new Player(3L, USER_TYPE.PLAYER,"player2",new BCryptPasswordEncoder().encode("123"),400,USER_STATUS.INACTIVE);
            Player player3 = new Player(4L, USER_TYPE.PLAYER,"player3",new BCryptPasswordEncoder().encode("123"),800,USER_STATUS.INACTIVE);
            Player player4 = new Player(5L, USER_TYPE.PLAYER,"player4",new BCryptPasswordEncoder().encode("123"),1400,USER_STATUS.INACTIVE);
            adminRepository.save(admin);
            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);
            playerRepository.save(player4);
            System.out.println( "db seeded");
        }
        else
            System.out.println("de already seeded");
    }

}
