package com.progweb.siri_cascudo_api.config;

import com.progweb.siri_cascudo_api.filter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF (Cross-Site Request Forgery)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
                    corsConfiguration.addAllowedOrigin("http://localhost:5173"); // Permite o front-end
                    corsConfiguration.addAllowedMethod("*"); // Permite todos os métodos (GET, POST, etc.)
                    corsConfiguration.addAllowedHeader("*"); // Permite todos os cabeçalhos
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Rotas de autenticação públicas
                        .requestMatchers("/uploads/**").permitAll() // Rotas de autenticação públicas
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Rotas de administrador
                        .requestMatchers("/api/u/**").permitAll() // Rotas específicas terminando com /u
                        .requestMatchers("/api/products", "/api/products/{id}").permitAll()
                        .requestMatchers("/api/products/**").hasRole("ADMIN")
                        .requestMatchers("/api/categories", "/api/categories/{id}").permitAll()
                        .requestMatchers("/api/categories/**").hasRole("ADMIN")
                        .requestMatchers("/api/recipes/**").hasRole("ADMIN")
                        .requestMatchers("/api/ingredients/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated() // Todas as outras rotas /api/** exigem autenticação
                        .anyRequest().denyAll() // Bloqueia todas as outras rotas não mapeadas
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica CORS para todas as rotas
                        .allowedOrigins("http://localhost:5173") // Permite o front-end
                        .allowedMethods("*") // Permite todos os métodos (GET, POST, etc.)
                        .allowedHeaders("*"); // Permite todos os cabeçalhos
            }
        };
    }
}
