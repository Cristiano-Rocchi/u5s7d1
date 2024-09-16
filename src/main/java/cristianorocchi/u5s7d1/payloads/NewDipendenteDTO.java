package cristianorocchi.u5s7d1.payloads;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewDipendenteDTO(

        @NotEmpty(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 20, message = "Il nome deve essere compreso tra 2 e 20 caratteri")
        String nome,

        @NotEmpty(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 20, message = "Il cognome deve essere compreso tra 2 e 20 caratteri")
        String cognome,

        @NotEmpty(message = "L'username è obbligatorio")
        @Size(min =35, max = 20, message = "L'username deve essere compreso tra 3 e 20 caratteri")
        String username,

        @NotEmpty(message = "email obbligatoria")
        @Email(message = "email inserita non è valida")
        String email,

        String immagineProfilo

) {}

