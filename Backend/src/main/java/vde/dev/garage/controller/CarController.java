package vde.dev.garage.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vde.dev.garage.configuration.JwtUtils;
import vde.dev.garage.modele.*;
import vde.dev.garage.repository.CarRepository;
import vde.dev.garage.repository.PermissionRepository;
import vde.dev.garage.repository.RoleRepository;
import vde.dev.garage.repository.UserRepository;
import vde.dev.garage.service.CarService;
import vde.dev.garage.service.CarServiceImpl;
import vde.dev.garage.service.CustomerUserDetailsService;
import vde.dev.garage.service.JwtService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/garage")
@Slf4j
public class CarController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CarServiceImpl carService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerUserDetailsService userDetailsService;

    @Autowired
    public CarController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nom d'utilisateur déjà utilisé.");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Roles defaultRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Rôle USER non trouvé"));
        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Utilisateur enregistré avec succès");
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                // Générer le token avec username et les rôles
                String accessToken = jwtUtils.generateAccessToken(userDetails.getUsername(), userDetails.getAuthorities());

                String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());

                // Retourner les deux tokens
                Map<String, Object> response = new HashMap<>();
                response.put("accessToken", accessToken);
                response.put("refreshToken", refreshToken);
                response.put("type", "Bearer");

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Nom d'utilisateur ou mot de passe invalide !");
            }

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Nom d'utilisateur ou mot de passe invalide"));
        }

    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        try {
            String username = jwtUtils.extractUsername(refreshToken);

            if (jwtUtils.isTokenExpired(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Refresh token expiré, veuillez vous reconnecter.");
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtUtils.generateAccessToken(
                    username,
                    userDetails.getAuthorities()
            );

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);

            return ResponseEntity.ok(tokens);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token invalide: " + e.getMessage());
        }
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
    List<AppUser> users = userRepository.findAll();
    List<UserDTO> userDTOS = users.stream()
            .map(user -> new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getRoles().stream()
                            .map(role -> role.getName().toString())
                            .collect(Collectors.toList())
            ))
            .collect(Collectors.toList());
    return ResponseEntity.ok(userDTOS);
}
    @PutMapping("/admin/users/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRoles(@PathVariable Long id, @RequestBody List<String> roles) {
        AppUser user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Set<Roles> newRoles = roles.stream()
                .map(roleName -> roleRepository.findByName(RoleName.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Rôle " + roleName + " introuvable")))
                .collect(Collectors.toSet());

        user.setRoles(newRoles);
        userRepository.save(user);

        return ResponseEntity.ok("Rôles mis à jour");
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PostMapping(path = "/create")
    public ResponseEntity<?> createCar(@RequestBody Car car) {
        if(carService.existsByImmatriculation(car.getImmatriculation())){
            return new ResponseEntity<>("Cette immatriculation existe déjà dans la base de données",HttpStatus.BAD_REQUEST);
        }
        Car createdCar=carService.createCar(car);
        return new ResponseEntity<>(createdCar, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/read")
    public ResponseEntity<?> readCar() {
        List<Car> listCar=carService.readCars();
        if(listCar.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listCar);
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/read/{immatriculation}")
    public ResponseEntity<?> readCarById(@PathVariable String immatriculation) {
        Optional<Car> car = carService.findCarById(immatriculation);
        if(car.isPresent()){
            return ResponseEntity.ok(car.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Voiture avec l'immatriculation " + immatriculation + " non trouvée.");
        }
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PutMapping("update/{immatriculation}")
    public ResponseEntity<?> updateCar(@PathVariable String immatriculation, @RequestBody Car car) {
        Optional<Car> existingCar = carService.findCarById(immatriculation);
        if (existingCar.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Voiture avec l'immatriculation " + immatriculation + " non trouvée.");
        }
        Car updatedCar = carService.updateCar(immatriculation, car);
        return ResponseEntity.ok(updatedCar);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{immatriculation}")
    public ResponseEntity<Map<String, Boolean>> deleteCar(@PathVariable String immatriculation) {
        Car car = carRepository.findById(immatriculation)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée !"));

        carRepository.delete(car);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/cars/{immatriculation}/status")
    public ResponseEntity<Void> updateCarStatus(@PathVariable String immatriculation, @RequestBody StatutName status) {
        carService.updateCarStatus(immatriculation, status);
        return ResponseEntity.ok().build();
    }


/*
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PutMapping("/cars/{immatriculation}/status")
    public ResponseEntity<?> updateCarStatus(@PathVariable String immatriculation, @RequestBody Map<String, String> request) {
        Optional<Car> carOptional = carRepository.findById(immatriculation);

        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            String newStatus = request.get("status");

            try {
                car.setStatus(StatutName.valueOf(newStatus));
                carRepository.save(car);
                return ResponseEntity.ok("Statut mis à jour avec succès !");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Statut invalide !");
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }

 */

}
