package com.mouad.ecommerce.keycloakApiAdmin;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class AdminUserController {
    @Autowired
    private KeycloakAdminService keycloakAdminService;

    // Endpoint pour créer un nouvel utilisateur
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        // Extraire le token JWT depuis le header Authorization
        // String token = extractTokenFromHeader(authorizationHeader);

        keycloakAdminService.createUser(
                userDTO.getUserName(),
                userDTO.getEmail(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getPassword(),
                userDTO.getDepartment(),
                userDTO.getTel()
        );
        return ResponseEntity.status(201).body("Utilisateur créé avec succès.");
    }

    // Endpoint pour supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        // Extraire le token JWT depuis le header Authorization
        // String token = extractTokenFromHeader(authorizationHeader);

        keycloakAdminService.deleteUser(id);
        return ResponseEntity.ok("Utilisateur supprimé avec succès.");
    }

    // Endpoint pour récupérer tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<UserRepresentation>> getAllUsers () {
        List<UserRepresentation> users = keycloakAdminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Endpoint pour récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<UserRepresentation> getUserById(@PathVariable String id) {
        UserRepresentation user = keycloakAdminService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Endpoint pour réinitialiser le mot de passe d'un utilisateur
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<String> resetUserPassword(@PathVariable String id, @RequestBody Map<String, String> newPassword) {
        keycloakAdminService.resetUserPassword(id, newPassword.get("newPassword"));
        return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
    }

    // Endpoint pour mettre à jour un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserDTO updateUserDTO) {
        keycloakAdminService.updateUser(id, updateUserDTO.getEmail(), updateUserDTO.getFirstName(), updateUserDTO.getDepartment());
        return ResponseEntity.ok("Utilisateur mis à jour avec succès.");
    }

    // Méthode utilitaire pour extraire le token du header Authorization
    /*
    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Enlever "Bearer " pour obtenir le token
        } else {
            throw new RuntimeException("Token JWT manquant ou mal formé dans le header Authorization");
        }
    }
    */
}
