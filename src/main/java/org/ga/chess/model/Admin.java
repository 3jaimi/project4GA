package org.ga.chess.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ga.chess.ENUM.USER_TYPE;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Admin extends User{

    public Admin(Long userId, USER_TYPE userType, String email, String password) {
        super(userId, userType, email, password);
    }

    public Admin(Long userId, USER_TYPE userType, String email, String password, List<Tournament> tournaments) {
        super(userId, userType, email, password);
        this.tournaments = tournaments;
    }

    @OneToMany(mappedBy = "organiser", orphanRemoval = true)
    private List<Tournament> tournaments;

}
