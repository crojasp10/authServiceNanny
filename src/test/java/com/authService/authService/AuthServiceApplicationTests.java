package com.authService.authService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

@SpringBootTest
@RequiredArgsConstructor	
class AuthServiceApplicationTests {

    private final CustomUserDetailsService userDetailsService;

    private final UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void loadUserByUsernameUserExistsReturnsUserDetails() {
        
        UserEntity user = new UserEntity();
        user.setUsername("john");
        user.setPassword("password123");
        user.setEnabled(true);
        user.setRoles(Set.of(new RoleEntity("ROLE_USER")));
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
