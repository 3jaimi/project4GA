package org.ga.chess.security;
import lombok.Setter;
import org.ga.chess.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Setter
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private JwtUtil jwtUtils;
    @Value("${jwt-verification-secret}")
    private String secret;

    public void sendVerificationEmail( User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("verify");
        message.setText(jwtUtils.generateToken(new MyUserDetails(user),secret));
        message.setFrom("chessApp");
        mailSender.send(message);
        System.out.println("Email sent successfully to " + user.getEmail());
    }

}