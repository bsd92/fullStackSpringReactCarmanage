package vde.dev.garage.controller;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    // Getters et setters
    private String username;
    private String password;
    public LoginRequest(){}

}
