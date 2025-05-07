package vde.dev.garage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vde.dev.garage.modele.Car;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    Optional<Car> findByImmatriculation(String immatriculation);
    boolean existsByImmatriculation(String immatriculation);

}
