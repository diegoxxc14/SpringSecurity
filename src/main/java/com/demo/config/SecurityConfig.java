package com.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    // Configuration One
    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            // Cross-Site Request Forgery (Vulnerabilidad en formularios desde el navegador)
            // Por defecto viene habilitado.
            // Inhabilitar cuando el consumo no sea desde un navegador.
            //.csrf().disable()  
            .authorizeHttpRequests()
                .requestMatchers("v1/index2").permitAll()  // Recursos que se van a autorizar
                .anyRequest().authenticated()  // Cualquier otro recurso debe estar autenticado
            .and()  // Para agregar más configuraciones
            .formLogin().permitAll()  // Cualquier puede acceder al formulario de login
            .and()
            .build();
    }*/


    // Configuration Two
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Usando una función lambda
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("v1/index2").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin()
                    .successHandler(successHandler())  // Al iniciar sesión redirigir a URL
                    .permitAll()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)  // ALWAYS, IF_REQUIRED, NEVER, STATELESS
                    .invalidSessionUrl("/login")  // Redirigir cuando no se ha creado una sessión
                    .maximumSessions(1)  // Si es multiplataforma puede ser más de 1
                    .expiredUrl("/login")  // Redirigir 
                    .sessionRegistry(sessionRegistry())  // Restreo de los datos de sesión
                .and()
                .sessionFixation()  // Vulnerabilidad Web (detecta un ataque de Fijación de sesión)
                    // migrateSession() - Crea otra sessión y migra los datos (Por defecto)
                    // newSession() - Crea una nueva sessión no copia los datos
                    // none() - No hace nada (No recomendable)
                    .migrateSession()  
                .and()
                .httpBasic()  // Método que permite la autenticación en el header de la petición
                .and()
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            response.sendRedirect("v1/session");
        });
    }
}
