package com.authService.authService.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.authService.authService.infraestructure.in.filter.JwtAuthenticationFilter;
import com.authService.authService.infraestructure.in.filter.JwtValidationFilter;

@EnableMethodSecurity
@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;


    @Bean
    AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity,AuthenticationManager authenticationManager) throws Exception{
                                     

    
        return httpSecurity.csrf(config -> config.disable())
        .authorizeHttpRequests((auth) -> auth
            // .requestMatchers(HttpMethod.GET,"/api/users").permitAll()
            // .requestMatchers(HttpMethod.POST,"/api/users").permitAll()
            .requestMatchers(HttpMethod.GET,"/api/users/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/users/registerAdmin").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.POST,"/api/users/register").permitAll().
            anyRequest().permitAll())
            .addFilter(new JwtAuthenticationFilter(authenticationManager()))
            .addFilter(new JwtValidationFilter(authenticationManager()))
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();

            

    }

}
