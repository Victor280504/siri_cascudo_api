package com.progweb.siri_cascudo_api.config;

import com.progweb.siri_cascudo_api.filter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF (Cross-Site Request Forgery)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Rotas de autenticação públicas
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Rotas de administrador
                        .requestMatchers("/api/u/**").permitAll() // Rotas específicas terminando com /u
                        .requestMatchers("/api/products", "/api/products/**").permitAll() // Qualquer usuário pode ver os produtos
                        .requestMatchers("/api/products").hasRole("ADMIN") // Apenas ADMIN pode adicionar produtos
                        .requestMatchers("/api/products/**").hasRole("ADMIN") // Apenas ADMIN pode editar/deletar produtos
                        .requestMatchers("/api/**").authenticated() // Todas as outras rotas /api/** exigem autenticação
                        .anyRequest().denyAll() // Bloqueia todas as outras rotas não mapeadas
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}