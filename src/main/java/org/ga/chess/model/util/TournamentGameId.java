package org.ga.chess.model.util;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TournamentGameId implements Serializable {
    private Long tournamentId;
    private Long gameId;

}

