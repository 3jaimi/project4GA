package org.ga.chess.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class  Player extends User{

    @Column
    private Integer rating;

    @OneToMany(mappedBy = "black")
    private List<Game> blackGames;

    @OneToMany(mappedBy = "white")
    private List<Game> whiteGames;
}
