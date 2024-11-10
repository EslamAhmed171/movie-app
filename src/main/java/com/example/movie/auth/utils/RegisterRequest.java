package com.example.movie.auth.utils;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "The email field can't be blank")
    @Email(message = "Please enter a proper email format")
    private String email;

    @NotBlank(message = "The password field can't be blank")
    @Size(min = 5, message = "password must have at least 5 characters")
    private String password;

    @NotBlank(message = "The username field can't be blank")
    @Column(unique = true, nullable = false)
    private String username;
}
