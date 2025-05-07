package vde.dev.garage.modele;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Set<RoleName> roles;
    private List<PermissionName> permissions;

}
