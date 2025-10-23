package com.authService.authService.infraestructure.in.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.authService.authService.config.TokenJwtConfig.CONTENT_TYPE;
import static com.authService.authService.config.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.authService.authService.config.TokenJwtConfig.PREFIX_TOKEN;
import static com.authService.authService.config.TokenJwtConfig.SECRET_KEY;
import com.authService.authService.infraestructure.out.UserEntity;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        UserEntity userEntity = null;
            String username = null;
            String password = null;

        try {
            userEntity = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class); 
            username = userEntity.getUsername();
            password = userEntity.getPassword();

        }  catch (StreamReadException | DatabindException | IOException e) {
             e.printStackTrace();
        }
               
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);

            return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
        User user = (User) authResult.getPrincipal();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        Claims claims = Jwts.claims()
        .add("authorities", new ObjectMapper().writeValueAsString(roles))
        .add("username",user.getUsername())
        .build();
        
            
        String token;
        token = Jwts.builder()
                .subject(user.getUsername())
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();

             response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);   


             Map <String,String> body = new HashMap<>();
             body.put("token", token);
             body.put("username", user.getUsername());
             body.put("message", String.format("Hello %s You han sing in successfully",user.getUsername()));

             response.getWriter().write(new ObjectMapper().writeValueAsString(body));
             response.setContentType(CONTENT_TYPE);
             response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        
                Map<String,String> body = new HashMap<>();
                body.put("message","Authentication error or incorrect password");
                body.put("error", failed.getMessage());
                response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                response.setStatus(401);
                response.setContentType(CONTENT_TYPE);
    }  
}
