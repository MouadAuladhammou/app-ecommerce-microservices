package com.mouad.ecommerce.keycloakApiAdmin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire.")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit être entre 3 et 50 caractères.")
    private String userName;

    @NotBlank(message = "L'adresse e-mail est obligatoire.")
    @Email(message = "L'adresse e-mail doit être valide.")
    private String email;

    private String firstName;

    private String lastName;

    @NotBlank(message = "Le mot de passe est obligatoire.")
    @Size(min = 6, message = "Le mot de passe doit comporter au moins 6 caractères.")
    private String password;

    // @NotNull(message = "Le département est obligatoire.")
    private String department;

    // @NotNull(message = "Le département est obligatoire.")
    private String tel;
}