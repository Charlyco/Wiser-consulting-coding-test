package com.wiser.consulting.auth_service.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

import com.wiser.consulting.auth_service.entity.User;
import com.wiser.consulting.auth_service.repo.UserRepository;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> user = userRepository.findUserByEmail(username);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException("User not found");
            }
            return user.orElseThrow();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public RestClient restClient(){
        return RestClient
                .builder()
                .baseUrl("http://localhost:8084/")
                .build();
    }

}
