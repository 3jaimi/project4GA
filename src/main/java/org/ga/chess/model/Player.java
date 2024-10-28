package org.ga.chess.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ga.chess.ENUM.USER_STATUS;
import org.ga.chess.ENUM.USER_TYPE;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class  Player extends User{

    public Player(Long userId, USER_TYPE userType, String email, String password, Integer rating, USER_STATUS status) {
        super(userId, userType, email, password, null);
        this.rating = rating;
        this.status=status;
    }

    public Player(Long userId, USER_TYPE userType, String email, String password, Integer rating, List<Game> blackGames, List<Game> whiteGames) {
        super(userId, userType, email, password, null);
        this.rating = rating;
        this.blackGames = blackGames;
        this.whiteGames = whiteGames;
    }

    @Column
    private Integer rating;

    @Column
    private USER_STATUS status;

    @OneToMany(mappedBy = "black")
    private List<Game> blackGames;

    @OneToMany(mappedBy = "white")
    private List<Game> whiteGames;

    @OneToMany(mappedBy = "challenger")
    private List<Challenge> challengesInitiated;

    @OneToMany(mappedBy = "challengee")
    private List<Challenge> challengesReceived;


}
