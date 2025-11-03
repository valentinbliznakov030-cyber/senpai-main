package bg.senpai_main.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Signature;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET = "MySuperSecretKeyThatIsAtLeast32CharsLong";
    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username){
        Instant now = Instant.now();

        String generatedToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.DAYS)))
                .signWith(SECRET_KEY)
                .compact();


        return generatedToken;
    }

    public Claims extractClaims(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    public String extractUsername(Claims claims){
        return claims.getSubject();
    }

    public boolean isValidToken(Claims claims, String username){
        return username.equals(claims.getSubject()) &&
                claims.getExpiration().after(Date.from(Instant.now()));
    }


}
