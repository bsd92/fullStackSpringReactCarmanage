package vde.dev.garage.service;

import vde.dev.garage.modele.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {



    Car createCar(Car car);

    List<Car> readCars();
   // Car  findCarById(String immatriculation);
    Optional<Car> findCarById(String immatriculation);

    Car updateCar(String immatriculation, Car car);

    String deleteCar(String immatriculation);

    boolean existsByImmatriculation(String immatriculation);
}
