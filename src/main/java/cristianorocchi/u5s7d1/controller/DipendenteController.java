package cristianorocchi.u5s7d1.controller;

import cristianorocchi.u5s7d1.entities.Dipendente;
import cristianorocchi.u5s7d1.payloads.NewDipendenteDTO;
import cristianorocchi.u5s7d1.services.DipendenteService;
import cristianorocchi.u5s7d1.exceptions.BadRequestException;
import cristianorocchi.u5s7d1.exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/dipendenti")
public class DipendenteController {

    @Autowired
    private DipendenteService dipendenteService;

    @GetMapping
    public Page<Dipendente> trovaTuttiDipendentiPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return dipendenteService.trovaTuttiPageable(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente creaDipendente(@Valid @RequestBody NewDipendenteDTO newDipendenteDTO) {
        // Verifica campi obbligatori
        if (newDipendenteDTO.email() == null || newDipendenteDTO.email().isEmpty()) {
            throw new BadRequestException("L'email del dipendente è obbligatoria.");
        }
        if (newDipendenteDTO.username() == null || newDipendenteDTO.username().isEmpty()) {
            throw new BadRequestException("L'username del dipendente è obbligatorio.");
        }

        Dipendente dipendente = new Dipendente();
        dipendente.setNome(newDipendenteDTO.nome());
        dipendente.setCognome(newDipendenteDTO.cognome());
        dipendente.setUsername(newDipendenteDTO.username());
        dipendente.setEmail(newDipendenteDTO.email());
        dipendente.setImmagineProfilo(newDipendenteDTO.immagineProfilo());

        return dipendenteService.salva(dipendente);
    }

    @GetMapping("/{dipendenteId}")
    public Dipendente trovaDipendentePerId(@PathVariable Long dipendenteId) {
        return dipendenteService.trovaPerId(dipendenteId);
    }

    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dipendente aggiornaDipendente(@PathVariable Long dipendenteId, @Valid @RequestBody NewDipendenteDTO newDipendenteDTO) {
        Dipendente esistente = dipendenteService.trovaPerId(dipendenteId);
        if (esistente == null) {
            throw new NotFoundException("Dipendente non trovato con ID: " + dipendenteId);
        }

        esistente.setUsername(newDipendenteDTO.username());
        esistente.setNome(newDipendenteDTO.nome());
        esistente.setCognome(newDipendenteDTO.cognome());
        esistente.setEmail(newDipendenteDTO.email());
        esistente.setImmagineProfilo(newDipendenteDTO.immagineProfilo());

        return dipendenteService.salva(esistente);
    }

    @DeleteMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaDipendente(@PathVariable Long dipendenteId) {
        try {
            dipendenteService.cancella(dipendenteId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Dipendente " + dipendenteId + " non trovato " );
        }
    }

    @PostMapping("/{dipendenteId}/img")
    @ResponseStatus(HttpStatus.OK)
    public Dipendente uploadImmagineProfilo(@PathVariable Long dipendenteId, @RequestParam("img") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException("Il file dell'immagine è obbligatorio.");
        }
        return dipendenteService.uploadImmagineProfilo(dipendenteId, file);
    }
}
