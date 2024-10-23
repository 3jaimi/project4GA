package org.ga.chess.security;



import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtUtil {

    Logger logger=Logger.getLogger(JwtUtil.class.getName());

    @Value("${jwt-secret}")
    private String secret;

    @Value("${jwt-expiration-ms}")
    private int jwtExpirationPeriod;

    public String generateToken(MyUserDetails user){
        return generateToken(user,secret);
    }


    public String generateToken(MyUserDetails user,String secret){
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+jwtExpirationPeriod))
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public String getUserNameFromToken(String token){
         return getUserNameFromToken(token,secret);
    }

    public String getUserNameFromToken(String token,String secret){
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String Token){
        return validateToken(Token,secret);
    }


    public boolean validateToken(String Token,String secret){
        try {
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(Token);
            return true;
        }catch (SecurityException e){
            logger.log(Level.SEVERE,"Invalid jwt Signature: "+e.getMessage());
        }catch (ExpiredJwtException e){
            logger.log(Level.SEVERE,"token expired: "+e.getMessage());
        }catch (MalformedJwtException e){
            logger.log(Level.SEVERE,"Invalid jwt token: "+e.getMessage());
        }catch (UnsupportedJwtException e){
            logger.log(Level.SEVERE,"jwt token sent is unsupported: "+e.getMessage());
        }catch (IllegalArgumentException e){
            logger.log(Level.SEVERE,"token is empty: "+e.getMessage());
        }
        return false;
    }

}
