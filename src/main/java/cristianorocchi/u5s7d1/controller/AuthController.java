package cristianorocchi.u5s7d1.controller;

import cristianorocchi.u5s7d1.entities.Dipendente;
import cristianorocchi.u5s7d1.enums.Role;
import cristianorocchi.u5s7d1.exceptions.BadRequestException;
import cristianorocchi.u5s7d1.payloads.NewDipendenteDTO;
import cristianorocchi.u5s7d1.payloads.NewDipendenteRespDTO;  // Aggiunto DTO di risposta per la registrazione
import cristianorocchi.u5s7d1.payloads.UserLoginDTO;
import cristianorocchi.u5s7d1.payloads.UserLoginRespDTO;
import cristianorocchi.u5s7d1.services.AuthService;
import cristianorocchi.u5s7d1.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private DipendenteService dipendenteService;

    @PostMapping("/login")
    public UserLoginRespDTO login(@RequestBody UserLoginDTO payload) {
        return new UserLoginRespDTO(this.authService.checkCredentialsAndGenerateToken(payload));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewDipendenteRespDTO save(@RequestBody @Validated NewDipendenteDTO body, BindingResult validationResult) {
        // Controlla se ci sono errori di validazione
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            // Creazione del nuovo dipendente utilizzando i dati del DTO
            Dipendente dipendente = new Dipendente();
            dipendente.setNome(body.nome());
            dipendente.setCognome(body.cognome());
            dipendente.setUsername(body.username());
            dipendente.setEmail(body.email());
            dipendente.setImmagineProfilo(body.immagineProfilo());
            dipendente.setPassword(body.password());
            dipendente.setRuolo(Role.USER);

            // Salva il nuovo dipendente e restituisce il DTO di risposta
            Dipendente dipendenteSalvato = this.dipendenteService.salva(dipendente);

            return new NewDipendenteRespDTO(dipendenteSalvato.getId());
        }
    }
}

