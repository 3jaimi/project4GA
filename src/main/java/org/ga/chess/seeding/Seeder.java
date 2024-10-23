package org.ga.chess.seeding;

import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Setter
public class Seeder implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

    }

    public String seedUsers(){
        return "";
    }

    public String seedGames(){
        return "";
    }

    public String seedTournamentAndTournamentGames(){
    return "";
    }
}
