package cristianorocchi.u5s7d1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class Config {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Disabilita comportamenti di default non graditi
        httpSecurity.formLogin(http -> http.disable()); // Disabilita il form di login di Spring Security
        httpSecurity.csrf(http -> http.disable()); // Disabilita la protezione CSRF
        httpSecurity.sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Sessioni stateless

        // Configura autorizzazioni delle richieste
        httpSecurity.authorizeHttpRequests(http -> http
                .requestMatchers("/auth/**").permitAll() // Permetti l'accesso a tutti gli endpoint di autenticazione senza autenticazione
                .anyRequest().authenticated() // Richiedi autenticazione per ogni altra richiesta
        );

        // Aggiungi il filtro JWTCheckFilter prima del filtro standard di autenticazione
        httpSecurity.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
