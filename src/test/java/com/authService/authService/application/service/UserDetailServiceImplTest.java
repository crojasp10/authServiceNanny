package com.authService.authService.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.authService.authService.domain.port.UserRepository;
import com.authService.authService.infraestructure.out.UserEntity;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor
public class UserDetailServiceImplTest {

    private final UserDetailServiceImpl userDetailsService;

    private final UserRepository userRepository;

    @Test
    void testLoadUserByUsername() {

    }

    @Test
    void loadUserByUsernameUserExistsReturnsUserDetails() {

        UserEntity user = new UserEntity();
        user.setUsername("john");
        user.setPassword("password123");
        user.setEnabled(true);
        user.setAdmin(true);
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("john");

        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsernameUserNotExistsThrowsException() {
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("no_existe"));
    }

}
