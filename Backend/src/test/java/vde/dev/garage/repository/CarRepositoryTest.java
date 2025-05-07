package vde.dev.garage.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import vde.dev.garage.modele.Car;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CarRepositoryTest {

@Autowired
   private CarRepository carRepository;
@Test
    public void shouldReturnAllCars(){
        //arrange
            //definie par defaut
        //Act
        List<Car> cars=carRepository.findAll();
        //Assert
        assertEquals(3, cars.size());
        assertEquals("toyota", cars.get(0).getMarque());

    }

    @Test
    public void shouldReturnCarByImmatriculation(){
        Car car=carRepository.findById("DZ-568-KC").get();
        assertEquals("toyota", car.getMarque());
        assertEquals("yaris", car.getModele());
    }
@Test
    public void shouldCreateCar(){
    Car car=new Car();
    car.setImmatriculation("EF-565-JU");
    car.setMarque("Audi");
    car.setModele("Vimus");
    car.setEtat("neuve");
    Car savedCar=carRepository.save(car);

    assertNotNull(car.getImmatriculation());
    assertEquals("EF-565-JU", car.getImmatriculation());
    assertEquals("Audi", car.getMarque());
    assertEquals("Vimus", car.getModele());
    }

    @Test
    public void shouldUpdateCar(){

    Car car=carRepository.findById("DZ-568-KC").get();

    car.setMarque("Ford");

    Car savedCar=carRepository.save(car);

    assertEquals("Ford", savedCar.getMarque());

    }

    @Test
    public void shouldDeleteCar(){
    carRepository.deleteById("EF-565-JU");

    Optional<Car> carDel=carRepository.findById("EF-565-JU");

    assertFalse(carDel.isPresent());

    }


}