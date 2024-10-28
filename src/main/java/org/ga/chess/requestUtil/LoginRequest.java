package org.ga.chess.requestUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email,password,verificationToken;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
