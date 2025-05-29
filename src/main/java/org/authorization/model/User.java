package org.authorization.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotBlank(message = "User name must not be empty")
    private String username;
    @NotBlank(message = "password must not be empty")
    private String password;
}
