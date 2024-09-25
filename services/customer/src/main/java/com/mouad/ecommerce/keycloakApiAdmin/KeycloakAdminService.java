package com.mouad.ecommerce.keycloakApiAdmin;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakAdminService {

    @Autowired
    private Keycloak keycloak;

    /*
    // NB: On peut mettre cette fonction ci-dessous pour initialiser Keycloak, mais il est préférable de l'initialiser en tant que Bean (voir le fichier : KeycloakAdminConfig.java)
    private Keycloak getKeycloakInstanceWithToken(token) {
        // Cette méthode utilisant le Token ne fonctionne pas ! (Le token est fourni par le contrôleur)
        // return KeycloakBuilder.builder()
                // .serverUrl("http://localhost:9098")
                // .realm("app-micro-services-realm")
                // .authorization("Bearer " + token)
                // .build();

        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:9098")
                .realm("app-micro-services-realm")
                .grantType("password")
                .username("admin@gmail.com")
                .password("123456")
                .clientId("client-angular")
                .build();
    }
    */

    public void createUser(String username, String email, String firstName, String lastName, String password, String department, String tel) {
        // Keycloak keycloak = getKeycloakInstanceWithToken();
        RealmResource realmResource = keycloak.realm("app-micro-services-realm");
        UsersResource usersResource = realmResource.users();

        // Vérification des valeurs des champs
        if (username == null || username.isEmpty() || email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur et l'adresse e-mail sont obligatoires.");
        }

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        // Ajout d'attributs personnalisés
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("department", Collections.singletonList(department));
        attributes.put("tel", Collections.singletonList(tel));
        user.setAttributes(attributes);

        // Initialiser le mot de passe
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        // Envoi de la requête à Keycloak
        Response response = usersResource.create(user);

        if (response.getStatus() == 201) {
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            System.out.println("Utilisateur créé avec succès avec l'ID : " + userId);
        } else {
            // Affiche le code d'erreur pour déboguer
            System.err.println("Erreur de création de l'utilisateur. Statut : " + response.getStatus());
            System.err.println("Détails de l'erreur : " + response.readEntity(String.class));
            throw new RuntimeException("Échec de la création de l'utilisateur. Statut : " + response.getStatus());
        }
    }

    public void deleteUser(String userId) {
        // Keycloak keycloak = getKeycloakInstanceWithToken();

        RealmResource realmResource = keycloak.realm("app-micro-services-realm");
        UsersResource usersResource = realmResource.users();
        try {
            usersResource.get(userId).remove();
        } catch (Exception e) {
            throw new RuntimeException("Échec de la suppression de l'utilisateur !");
        }
    }

    public List<UserRepresentation> getAllUsers() {
        RealmResource realmResource = keycloak.realm("app-micro-services-realm");
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> users = usersResource.list();
        return users;
    }

    public UserRepresentation getUserById(String userId) {
        RealmResource realmResource = keycloak.realm("app-micro-services-realm");
        UserResource userResource = realmResource.users().get(userId);

        return userResource.toRepresentation();
    }

    public void resetUserPassword(String userId, String newPassword) {
        RealmResource realmResource = keycloak.realm("app-micro-services-realm");
        UserResource userResource = realmResource.users().get(userId);

        // Créer une représentation du nouveau mot de passe
        CredentialRepresentation newCredential = new CredentialRepresentation();
        newCredential.setType(CredentialRepresentation.PASSWORD);
        System.out.println("newPassword " + newPassword);
        newCredential.setValue(newPassword);
        newCredential.setTemporary(false); // "false" signifie que l'utilisateur n'aura pas à changer le mot de passe à la première connexion

        // Réinitialiser le mot de passe de l'utilisateur
        userResource.resetPassword(newCredential);
    }

    public void updateUser(String userId, String email, String firstName, String department) {
        RealmResource realmResource = keycloak.realm("app-micro-services-realm");
        UserResource userResource = realmResource.users().get(userId);

        // Récupérer les informations actuelles de l'utilisateur
        UserRepresentation userRepresentation = userResource.toRepresentation();

        // Modifier les informations de l'utilisateur
        userRepresentation.setEmail(email);
        userRepresentation.setFirstName(firstName);

        // Ajouter ou mettre à jour l'attribut personnalisé "department"
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put("department", Collections.singletonList(department));  // Utilisation d'une liste pour l'attribut
        userRepresentation.setAttributes(attributes);

        // Mettre à jour l'utilisateur dans Keycloak
        userResource.update(userRepresentation);
    }
}
