package org.ga.chess.model;

import jakarta.persistence.*;
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
public class Admin extends User{

    @OneToMany(mappedBy = "organiser", orphanRemoval = true)
    private List<Tournament> tournaments;

}
