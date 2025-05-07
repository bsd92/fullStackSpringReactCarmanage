package vde.dev.garage.modele;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
    // Getter et Setter
    private String token;


    public AuthResponse() {}

    public AuthResponse(String token) {
        this.token = token;
    }

}

