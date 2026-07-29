package com.example.backend.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    private static final String NAME_REGEX = "^[\\p{L} ]+$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9._]+$";

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 4, max = 20, message = "El usuario debe tener entre 4 y 20 caracteres")
    @Pattern(regexp = USERNAME_REGEX, message = "El usuario solo puede contener letras, números, puntos y guiones bajos")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Pattern(regexp = ".*[A-Z].*", message = "La contraseña debe contener al menos una letra mayúscula")
    private String password;

    @NotBlank(message = "Debes repetir la contraseña")
    private String confirmPassword;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    @Pattern(regexp = NAME_REGEX, message = "El nombre solo puede contener letras y espacios")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede superar los 50 caracteres")
    @Pattern(regexp = NAME_REGEX, message = "El apellido solo puede contener letras y espacios")
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Size(max = 254, message = "El email no puede superar los 254 caracteres")
    @Email(message = "Email inválido")
    private String email;

    @AssertTrue(message = "Las contraseñas no coinciden")
    private boolean isConfirmPasswordValid() {
        return password != null && password.equals(confirmPassword);
    }
}