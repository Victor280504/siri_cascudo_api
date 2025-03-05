package com.progweb.siri_cascudo_api.config;

import com.progweb.siri_cascudo_api.filter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF (Cross-Site Request Forgery)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
                    corsConfiguration.addAllowedOrigin("http://localhost:5173"); // Permite o front-end
                    corsConfiguration.addAllowedMethod("*"); // Permite todos os métodos (GET, POST, etc.)
                    corsConfiguration.addAllowedHeader("*"); // Permite todos os cabeçalhos
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(auth -> auth
                        // publics
                        .requestMatchers("/api/auth/**").permitAll() // Rotas de autenticação públicas
                        .requestMatchers("/uploads/**").permitAll() // Rotas de autenticação públicas
                        .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories", "/api/categories/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ingredients", "/api/ingredients/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sales", "/api/sales/detail/{id}", "/api/sales/user/{id}", "/api/sales/{id}").permitAll()

                        // Restringindo POST, PUT e DELETE para ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/sales/report").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/sales").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/products", "/api/categories", "/api/ingredients").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**", "/api/categories/**", "/api/ingredients/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**", "/api/categories/**", "/api/ingredients/**", "/api/sales/**").hasRole("ADMIN")

                        // Todas as outras rotas exigem autenticação
                        .requestMatchers("/api/**").authenticated()

                        // Bloqueia qualquer outra rota não mapeada
                        .anyRequest().denyAll()
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
