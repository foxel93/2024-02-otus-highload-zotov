package ru.otus.hw.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filterChain(
        HttpSecurity http,
        UserDetailsService userDetailsService
    ) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/user/register").permitAll()
                .anyRequest().authenticated()
            )
            .userDetailsService(userDetailsService)
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(withDefaults());
        return http.build();
    }
}
