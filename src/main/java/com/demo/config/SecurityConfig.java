package com.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

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
                .formLogin().permitAll()
                .and()
                .build();
    }
}
