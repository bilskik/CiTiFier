package pl.bilskik.citifier.web.common;

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
                .formLogin((login) -> { login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("login")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/challenge-list", true)
                        .failureUrl("/login?loginError=true")
                        .permitAll();
                })
                .authorizeHttpRequests((auth) -> {
                    auth
                        .requestMatchers(
                                "/register", "/register/redirect-to-login",
                                "/login", "/login/redirect-to-register",
                                "/css/**", "/js/**","/img/**", "/webjars/**"
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated();
                })
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .logout((logout) -> { logout
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID");
                })
                .build();
    }
}
