package org.authorization.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.authorization.Authorities;
import org.authorization.helper.FromQuery;
import org.authorization.model.User;
import org.authorization.service.AuthorizationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class AuthorizationController {
    AuthorizationService service;

    @GetMapping("/authorize")
    public List<Authorities> getAuthorities(@Valid @FromQuery User user) {
        return service.getAuthorities(user);
    }
}