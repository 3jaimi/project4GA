package org.ga.chess.requestUtil;

import lombok.Getter;
import lombok.Setter;
import org.ga.chess.ENUM.USER_TYPE;

@Getter
@Setter
public class SignUpRequest extends LoginRequest{
    private USER_TYPE userType;

    public SignUpRequest(String email, String password, USER_TYPE userType) {
        super(email, password);
        this.userType = userType;
    }
}
