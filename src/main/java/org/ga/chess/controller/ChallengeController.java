package org.ga.chess.controller;

import org.ga.chess.ENUM.CHALLENGE_STATUS;
import org.ga.chess.ENUM.PLAYER_COLOUR;
import org.ga.chess.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @PostMapping("/issue-{email}")
    public ResponseEntity<?> issueChallenge(@PathVariable String email, @RequestParam PLAYER_COLOUR colour) {
        return challengeService.issueChallenge(email, colour);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteChallenge(@PathVariable Long id) {
        return challengeService.deleteChallenge(id);
    }

    @PutMapping("/process/{id}")
    public ResponseEntity<?> processChallenge(@PathVariable Long id, @RequestParam CHALLENGE_STATUS status) {
        return challengeService.processChallenge(id, status);
    }
}
