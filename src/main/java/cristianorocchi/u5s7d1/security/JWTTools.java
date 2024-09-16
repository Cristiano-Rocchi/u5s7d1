package cristianorocchi.u5s7d1.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import cristianorocchi.u5s7d1.entities.Dipendente;
import cristianorocchi.u5s7d1.exceptions.UnauthorizedException;

import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(Dipendente dipendente) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .subject(String.valueOf(dipendente.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public void verifyToken(String token) { // Verifica il token
        try {
            Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build().parseClaimsJws(token);
        } catch (Exception ex) {
            // Gestione dell'eccezione per token non valido
            System.out.println(ex.getMessage());
            throw new UnauthorizedException("Problemi col token! Per favore effettua di nuovo il login!");
        }
    }
}


