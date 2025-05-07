package vde.dev.garage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vde.dev.garage.modele.*;
import vde.dev.garage.repository.PermissionRepository;
import vde.dev.garage.repository.RoleRepository;
import vde.dev.garage.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
/*
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository  permissionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (permissionRepository.count() == 0) {
            Permission viewCars = new Permission();
            viewCars.setName(PermissionName.CAN_CREATE_CARS);

            Permission createCars = new Permission();
            createCars.setName(PermissionName.CAN_CREATE_CARS);

            Permission editCars = new Permission();
            editCars.setName(PermissionName.CAN_UPDATE_CARS);

            Permission deleteCars = new Permission();
            deleteCars.setName(PermissionName.CAN_DELETE_CARS);

            permissionRepository.saveAll(Arrays.asList(viewCars,createCars, editCars, deleteCars));

            // Créer les rôles avec les permissions
            Roles admin = new Roles();
            admin.setName(RoleName.ADMIN);
            admin.setPermissions(new HashSet<>(Arrays.asList(viewCars, createCars, editCars, deleteCars)));

            Roles manager = new Roles();
            admin.setName(RoleName.MANAGER);
            admin.setPermissions(new HashSet<>(Arrays.asList(viewCars, editCars, deleteCars)));


            Roles user = new Roles();
            user.setName(RoleName.USER);
            user.setPermissions(new HashSet<>(Collections.singletonList(viewCars)));

            roleRepository.saveAll(Arrays.asList(admin, manager, user));

            // Ajouter un utilisateur ADMIN
            AppUser adminUser = new AppUser();
            adminUser.setUsername("BoubacarS");
            adminUser.setPassword(passwordEncoder.encode("Boubacar2025"));
            adminUser.setRoles(Collections.singleton(admin));
            userRepository.save(adminUser);
            // Ajouter un utilisateur MANAGER
            AppUser managerUser = new AppUser();
            adminUser.setUsername("BoubacarSS");
            adminUser.setPassword(passwordEncoder.encode("Boubacar2025"));
            adminUser.setRoles(Collections.singleton(manager));
            userRepository.save(managerUser);
            // Ajouter un utilisateur USER
            AppUser userUser = new AppUser();
            adminUser.setUsername("BoubacarSSS");
            adminUser.setPassword(passwordEncoder.encode("Boubacar2025"));
            adminUser.setRoles(Collections.singleton(user));
            userRepository.save(userUser);

            System.out.println("Permissions et rôles initialisés !");
        }
    }
}

 */
