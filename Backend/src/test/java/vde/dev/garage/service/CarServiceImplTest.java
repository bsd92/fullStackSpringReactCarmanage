package vde.dev.garage.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vde.dev.garage.modele.Car;
import vde.dev.garage.repository.CarRepository;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CarServiceImplTest {


    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void createCar() {
    }

    @Test
    void shouldReadCars() {
        //Arrange
       Car car1 = new Car("TG-545-YH","Yamaha","sonny","usage");
       Car car2 = new Car("VC-545-YT","BZZZ","Tony","occasion");

       when(carRepository.findAll()).thenReturn(List.of(car1, car2));

       List<Car> cars=carService.readCars();

       //Assert
       assertThat(cars).hasSize(2).contains(car1, car2);

    }

    @Test
    void shouldUpdateCar() {
        // GIVEN - Création de la voiture existante
        String immatriculation = "TG-545-YH"; // L'ID de la voiture
        Car existingCar = new Car(immatriculation, "Yamaha", "Sonny", "Usage");

        // Nouvelle voiture avec les nouvelles valeurs à mettre à jour
        Car updatedCar = new Car(immatriculation, "Honda", "Civic", "Personnel");

        // Simulation du comportement du repository
        when(carRepository.findById(immatriculation))
                .thenReturn(Optional.of(existingCar)); //  findById() retourne un Optional<Car>

        when(carRepository.save(any(Car.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Simule la sauvegarde de la voiture mise à jour

        // WHEN - Exécution du service de mise à jour
        Car carUpdate = carService.updateCar(immatriculation, updatedCar);

        // THEN - Vérification des résultats
        assertThat(carUpdate).isNotNull();
        assertThat(carUpdate.getImmatriculation()).isEqualTo(immatriculation); // L'immatriculation ne doit pas changer
        assertThat(carUpdate.getMarque()).isEqualTo("Honda"); // Marque mise à jour
        assertThat(carUpdate.getModele()).isEqualTo("Civic"); // Modèle mis à jour

    }

    @Test
    void deleteCar() {
        carService.deleteCar("TG-545-YH");
        verify(carRepository).deleteById("TG-545-YH");
    }


}