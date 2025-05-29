package org.authorization.repository;

import org.authorization.Authorities;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private final Map<String, String> users = Map.of(
            "admin", "admin",
            "user", "user"
    );

    private final Map<String, List<Authorities>> authorities = Map.of(
            "admin", List.of(Authorities.READ, Authorities.WRITE, Authorities.DELETE),
            "user", List.of(Authorities.READ)
    );


    public List<Authorities> getUserAuthorities(String user, String password) {
        if (!users.containsKey(user) || !users.get(user).equals(password)) {
            return List.of();
        }
        return authorities.get(user);
    }
}