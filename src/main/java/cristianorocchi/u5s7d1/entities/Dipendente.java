package cristianorocchi.u5s7d1.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dipendente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dipendente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String immagineProfilo;

    @Column(nullable = false)
    private String password;


}

