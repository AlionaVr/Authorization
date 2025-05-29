package org.authorization.service;

import lombok.AllArgsConstructor;
import org.authorization.Authorities;
import org.authorization.exception.InvalidCredentials;
import org.authorization.exception.UnauthorizedUser;
import org.authorization.model.User;
import org.authorization.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorizationService {
    UserRepository userRepository;

    public List<Authorities> getAuthorities(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (isEmpty(username) || isEmpty(password)) {
            throw new InvalidCredentials("User name or password is empty");
        }
        List<Authorities> userAuthorities = userRepository.getUserAuthorities(username, password);
        if (isEmpty(userAuthorities)) {
            throw new UnauthorizedUser("Unknown user " + username);
        }
        return userAuthorities;
    }

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private boolean isEmpty(List<?> str) {
        return str == null || str.isEmpty();
    }
}