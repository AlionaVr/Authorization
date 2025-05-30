package org.authorization.controller;

import org.authorization.Authorities;
import org.authorization.exception.UnauthorizedUser;
import org.authorization.model.User;
import org.authorization.service.AuthorizationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorizationController.class)
public class AuthorizationControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthorizationService service;

    @Test
    public void shouldReturnAuthoritiesForValidUser() throws Exception {
        when(service.getAuthorities(any(User.class)))
                .thenReturn(List.of(Authorities.READ, Authorities.WRITE, Authorities.DELETE));

        mockMvc.perform(get("/authorize")
                        .queryParam("user", "admin")
                        .queryParam("password", "admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("READ"))
                .andExpect(jsonPath("$[1]").value("WRITE"))
                .andExpect(jsonPath("$[2]").value("DELETE"));
    }

    @Test
    public void shouldReturnBadRequestForEmptyInput() throws Exception {
        mockMvc.perform(get("/authorize")
                        .param("username", "")
                        .param("password", ""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("User name must not be empty"))
                .andExpect(jsonPath("$.password").value("password must not be empty"));
    }

    @Test
    public void shouldReturnUnauthorizedForNonRegisteredUser() throws Exception {
        when(service.getAuthorities(any(User.class)))
                .thenThrow(new UnauthorizedUser("Unknown user unknown"));

        mockMvc.perform(get("/authorize")
                        .param("user", "unknown")
                        .param("password", "pass"))
                .andExpect(status().isUnauthorized());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public AuthorizationService authorizationService() {
            return Mockito.mock(AuthorizationService.class);
        }
    }
}