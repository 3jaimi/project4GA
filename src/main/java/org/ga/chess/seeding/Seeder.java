package org.ga.chess.seeding;

import lombok.Setter;
import org.ga.chess.ENUM.GAME_RESULT;
import org.ga.chess.ENUM.TOURNAMENT_STATUS;
import org.ga.chess.ENUM.USER_STATUS;
import org.ga.chess.ENUM.USER_TYPE;
import org.ga.chess.model.*;
import org.ga.chess.model.util.TournamentGameId;
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
    @Autowired
    private ITournamentGameRepository tournamentGameRepository;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        System.out.println(tournamentTest());
    }

    public void seedUsers(){
        if (userRepository.count()==0){
            Admin admin = new Admin(1L, USER_TYPE.ADMIN,"admin",new BCryptPasswordEncoder().encode("123"));
            Player player1 = new Player(2L, USER_TYPE.PLAYER, "player1", new BCryptPasswordEncoder().encode("123"), 400, USER_STATUS.ACTIVE);
            Player player2 = new Player(3L, USER_TYPE.PLAYER, "player2", new BCryptPasswordEncoder().encode("123"), 400, USER_STATUS.ACTIVE);
            Player player3 = new Player(4L, USER_TYPE.PLAYER, "player3", new BCryptPasswordEncoder().encode("123"), 800, USER_STATUS.ACTIVE);
            Player player4 = new Player(5L, USER_TYPE.PLAYER, "player4", new BCryptPasswordEncoder().encode("123"), 1400, USER_STATUS.ACTIVE);
            Player player5 = new Player(6L, USER_TYPE.PLAYER, "player5", new BCryptPasswordEncoder().encode("123"), 1000, USER_STATUS.ACTIVE);
            Player player6 = new Player(7L, USER_TYPE.PLAYER, "player6", new BCryptPasswordEncoder().encode("123"), 1200, USER_STATUS.ACTIVE);
            Player player7 = new Player(8L, USER_TYPE.PLAYER, "player7", new BCryptPasswordEncoder().encode("123"), 1100, USER_STATUS.ACTIVE);
            Player player8 = new Player(9L, USER_TYPE.PLAYER, "player8", new BCryptPasswordEncoder().encode("123"), 900, USER_STATUS.ACTIVE);
            Player player9 = new Player(10L, USER_TYPE.PLAYER, "player9", new BCryptPasswordEncoder().encode("123"), 950, USER_STATUS.ACTIVE);
            Player player10 = new Player(11L, USER_TYPE.PLAYER, "player10", new BCryptPasswordEncoder().encode("123"), 1300, USER_STATUS.ACTIVE);
            Player player11 = new Player(12L, USER_TYPE.PLAYER, "player11", new BCryptPasswordEncoder().encode("123"), 1250, USER_STATUS.ACTIVE);
            Player player12 = new Player(13L, USER_TYPE.PLAYER, "player12", new BCryptPasswordEncoder().encode("123"), 850, USER_STATUS.ACTIVE);
            Player player13 = new Player(14L, USER_TYPE.PLAYER, "player13", new BCryptPasswordEncoder().encode("123"), 1150, USER_STATUS.ACTIVE);
            Player player14 = new Player(15L, USER_TYPE.PLAYER, "player14", new BCryptPasswordEncoder().encode("123"), 1350, USER_STATUS.ACTIVE);
            Player player15 = new Player(16L, USER_TYPE.PLAYER, "player15", new BCryptPasswordEncoder().encode("123"), 1000, USER_STATUS.ACTIVE);
            Player player16 = new Player(17L, USER_TYPE.PLAYER, "player16", new BCryptPasswordEncoder().encode("123"), 1100, USER_STATUS.ACTIVE);

            adminRepository.save(admin);
            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);
            playerRepository.save(player4);
            playerRepository.save(player5);
            playerRepository.save(player6);
            playerRepository.save(player7);
            playerRepository.save(player8);
            playerRepository.save(player9);
            playerRepository.save(player10);
            playerRepository.save(player11);
            playerRepository.save(player12);
            playerRepository.save(player13);
            playerRepository.save(player14);
            playerRepository.save(player15);
            playerRepository.save(player16);
            System.out.println( "db seeded");
        }
        else
            System.out.println("db already seeded");
    }

    public Long tournamentTest(){
        Admin admin = new Admin(1L, USER_TYPE.ADMIN,"admin",new BCryptPasswordEncoder().encode("123"));
        Tournament tournament=new Tournament(null,admin,null,16,16, TOURNAMENT_STATUS.PENDING_START);
        Tournament saved=tournamentRepository.save(tournament);
        Game game0 = gameRepository.save(new Game(null, playerRepository.findById(2L).get(), playerRepository.findById(17L).get(), GAME_RESULT.NOT_PLAYED));
        Game game1 = gameRepository.save(new Game(null, playerRepository.findById(4L).get(), playerRepository.findById(3L).get(), GAME_RESULT.NOT_PLAYED));
        Game game2 = gameRepository.save(new Game(null, playerRepository.findById(6L).get(), playerRepository.findById(5L).get(), GAME_RESULT.NOT_PLAYED));
        Game game3 = gameRepository.save(new Game(null, playerRepository.findById(8L).get(), playerRepository.findById(7L).get(), GAME_RESULT.NOT_PLAYED));
        Game game4 = gameRepository.save(new Game(null, playerRepository.findById(10L).get(), playerRepository.findById(9L).get(), GAME_RESULT.NOT_PLAYED));
        Game game5 = gameRepository.save(new Game(null, playerRepository.findById(12L).get(), playerRepository.findById(11L).get(), GAME_RESULT.NOT_PLAYED));
        Game game6 = gameRepository.save(new Game(null, playerRepository.findById(14L).get(), playerRepository.findById(13L).get(), GAME_RESULT.NOT_PLAYED));
        Game game7 = gameRepository.save(new Game(null, playerRepository.findById(16L).get(), playerRepository.findById(15L).get(), GAME_RESULT.NOT_PLAYED));


// Games with null players
        Game game8 = gameRepository.save(new Game(null, null, null, GAME_RESULT.NOT_PLAYED));
        Game game9 = gameRepository.save(new Game(null, null, null, GAME_RESULT.NOT_PLAYED));
        Game game10 = gameRepository.save(new Game(null, null, null, GAME_RESULT.NOT_PLAYED));
        Game game11 = gameRepository.save(new Game(null, null, null, GAME_RESULT.NOT_PLAYED));
        Game game12 = gameRepository.save(new Game(null, null, null, GAME_RESULT.NOT_PLAYED));
        Game game13 = gameRepository.save(new Game(null, null, null, GAME_RESULT.NOT_PLAYED));
        Game game14 = gameRepository.save(new Game(null, null, null, GAME_RESULT.NOT_PLAYED));




        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game0.getId()),saved,game0));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game1.getId()),saved,game1));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game2.getId()),saved,game2));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game3.getId()),saved,game3));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game4.getId()),saved,game4));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game5.getId()),saved,game5));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game6.getId()),saved,game6));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game7.getId()),saved,game7));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game8.getId()),saved,game8));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game9.getId()),saved,game9));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game10.getId()),saved,game10));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game11.getId()),saved,game11));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game12.getId()),saved,game12));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game13.getId()),saved,game13));
        tournamentGameRepository.save(new TournamentGame(new TournamentGameId(saved.getId(), game14.getId()),saved,game14));




        return saved.getId();
    }

}
