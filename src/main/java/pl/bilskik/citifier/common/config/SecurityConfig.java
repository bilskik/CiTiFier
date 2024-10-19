package pl.bilskik.citifier.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(x -> x.disable())
                .authorizeHttpRequests((auth) -> {
                    auth
                        .requestMatchers(
                                "/register", "/ctf-core/register", "/ctf-core/register/redirect-to-login",
                                "/login", "/ctf-core/login/redirect-to-register",
                                "/css/**", "/js/**", "/webjars/**"
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated();
                })
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .build();
    }
}
