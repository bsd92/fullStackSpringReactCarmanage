package vde.dev.garage.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vde.dev.garage.modele.AppUser;
import vde.dev.garage.modele.Car;
import vde.dev.garage.modele.StatutName;
import vde.dev.garage.repository.CarRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public List<Car> readCars() {
        return carRepository.findAll();
    }

    @Override
    public Optional<Car> findCarById(String immatriculation) {
        return carRepository.findById(immatriculation);
    }
/*
    @Override
    public Car findCarById(String immatriculation) {
        return carRepository.findById(immatriculation)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voiture non trouvée"));

    }

 */

    @Override
    public Car updateCar(String immatriculation, Car updatedCar) {
        Car car = carRepository.findByImmatriculation(immatriculation)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée"));

        car.setMarque(updatedCar.getMarque());
        car.setModele(updatedCar.getModele());
        car.setEtat(updatedCar.getEtat());
        car.setStatut(updatedCar.getStatut());


        return carRepository.save(car);
/*
        return carRepository.findById(immatriculation)
        .map(c->{
            c.setMarque(car.getMarque());
            c.setModele(car.getModele());
            c.setEtat(car.getEtat());
            return carRepository.save(c);
        }).orElseThrow(()-> new RuntimeException("Car non trouvé "));

 */
    }

    @Override
    public String deleteCar(String immatriculation) {
        carRepository.deleteById(immatriculation);
        return "Car supprimé avec succès !";
    }

    @Override
    public boolean existsByImmatriculation(String immatriculation) {
        return carRepository.existsByImmatriculation(immatriculation);
    }
    @Transactional
    public void updateCarStatus(String immatriculation, StatutName newStatus) {
        Car car = carRepository.findById(immatriculation)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        car.setStatut(newStatus);
        carRepository.save(car);

        sendEmailNotification(car);
    }

    private void sendEmailNotification(Car car) {
        String subject = "Notification sur l'état de votre voiture";
        String body = "Bonjour, votre voiture " + car.getMarque()+ " " + car.getModele()
                + " a changé de statut : " + car.getStatut();

        // L'utilisateur de la voiture
        AppUser user = car.getUser();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    }
}
