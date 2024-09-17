package cristianorocchi.u5s7d1.services;



import cristianorocchi.u5s7d1.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import cristianorocchi.u5s7d1.entities.Dipendente;
import cristianorocchi.u5s7d1.exceptions.UnauthorizedException;
import cristianorocchi.u5s7d1.payloads.UserLoginDTO;


@Service
public class AuthService {

    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(UserLoginDTO body) {
        Dipendente found = this.dipendenteService.findByEmail(body.email());
        if  (bcrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.createToken(found);
        } else {
            throw new UnauthorizedException("Credenziali errate!");
        }
    }
}

