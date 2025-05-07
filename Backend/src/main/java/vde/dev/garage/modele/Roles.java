package vde.dev.garage.modele;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Roles {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Enumerated(EnumType.STRING)
        private RoleName name;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "roles_permissions",
                joinColumns = @JoinColumn(name = "role_id"),
                inverseJoinColumns = @JoinColumn(name = "permission_id")
        )
        private Set<Permission> permissions = new HashSet<>();

        @ManyToMany(mappedBy = "roles") // important pour la relation inverse
        private Set<AppUser> users = new HashSet<>();
}


