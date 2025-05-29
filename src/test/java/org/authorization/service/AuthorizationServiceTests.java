package org.authorization.service;

import org.authorization.Authorities;
import org.authorization.exception.InvalidCredentials;
import org.authorization.exception.UnauthorizedUser;
import org.authorization.model.User;
import org.authorization.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthorizationServiceTests {

    private UserRepository userRepository;
    private AuthorizationService service;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        service = new AuthorizationService(userRepository);
    }

    @Test
    public void shouldReturnAuthoritiesForValidCredentials() {
        User user = new User("admin", "admin");
        when(userRepository.getUserAuthorities("admin", "admin"))
                .thenReturn(List.of(Authorities.READ, Authorities.WRITE));

        List<Authorities> result = service.getAuthorities(user);

        assertEquals(2, result.size());
        assertTrue(result.contains(Authorities.READ));
        assertTrue(result.contains(Authorities.WRITE));
    }

    @Test
    public void shouldThrowInvalidCredentialsForEmptyInput() {
        User user = new User("", "");
        assertThrows(InvalidCredentials.class, () -> {
            service.getAuthorities(user);
        });
    }

    @Test
    public void shouldThrowUnauthorizedUserWhenUserNotFound() {
        User user = new User("unknown", "wrong");
        when(userRepository.getUserAuthorities("unknown", "wrong"))
                .thenReturn(List.of());

        assertThrows(UnauthorizedUser.class, () -> {
            service.getAuthorities(user);
        });
    }
}