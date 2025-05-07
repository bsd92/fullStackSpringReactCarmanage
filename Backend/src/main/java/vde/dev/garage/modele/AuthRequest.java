package vde.dev.garage.modele;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequest {
    // Getters et Setters
    private String username;
    private String password;

    // Constructeurs
    public AuthRequest() {}

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
