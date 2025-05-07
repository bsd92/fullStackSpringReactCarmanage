package vde.dev.garage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vde.dev.garage.modele.RoleName;
import vde.dev.garage.modele.Roles;

import java.util.Optional;
@Repository
public interface RoleRepository  extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(RoleName name);
    //Roles findByName(RoleName roles);
}
