package vde.dev.garage.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import vde.dev.garage.configuration.JwtUtils;
import vde.dev.garage.modele.Car;

import vde.dev.garage.repository.UserRepository;
import vde.dev.garage.service.CarServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
   private CarServiceImpl carService;

    @Mock
    private UserRepository uRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Test
    @WithMockUser(username = "Boubacar1", authorities = {"CAN_VIEW_CARS"})
    void shouldReadCar() throws Exception {
        Car car1 = new Car("TG-545-YH","Yamaha","sonny","usage");
        Car car2 = new Car("VC-545-YT","BZZZ","Tony","occasion");

        lenient().when(carService.readCars()).thenReturn(List.of(car1, car2));

        mockMvc.perform(get("/garage/read").header("Authorization", ""))
        .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].immatriculation").value("DZ-568-K0"));
    }

    @Test
    @WithMockUser(username = "Boubacar1", authorities = {"CAN_CREATE_CARS"})
    void shouldCreateCar() throws Exception {
        String json= """
                {
                        "immatriculation": "DZ-568-KY",
                        "marque": "Toyota4",
                        "modele": "Yaris4",
                        "etat": "Neuve4"
                }
                """;
        Car car = new Car("TG-545-YH","Yamaha","sonny","usage");
        lenient().when(carService.createCar(any(Car.class))).thenReturn(new Car());

        mockMvc.perform(MockMvcRequestBuilders.post("/garage/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == HttpStatus.OK.value() || status == HttpStatus.CREATED.value());
                });

    }

    @Test
    @WithMockUser(username = "Boubacar1", authorities = {"CAN_UPDATE_CARS"})
    void shouldUpdateCar() throws Exception {
        String json= """
                {
                        "immatriculation": "DZ-568-KY",
                        "marque": "Berline",
                        "modele": "Hiernos",
                        "etat": "occasion"
                }
                """;
        Car existingCar = new Car("DZ-568-KY","toyota4","Yarris4","neuve4");
        Car updatingCar = new Car("DZ-568-KY","Berline","Hiernos","occasion");

        //lenient().when(carService.findCarById("DZ-568-KY")).thenReturn(existingCar).thenReturn(updatingCar);


        mockMvc.perform(MockMvcRequestBuilders.put("/garage/update/DZ-568-KY").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marque").value("Berline"));

    }

    @Test
    @WithMockUser(username = "Boubacar1", authorities = {"CAN_DELETE_CARS"})
    void shouldDeleteCar() throws Exception {
        Car car = new Car("DZ-568-KY","toyota4","Yarris4","neuve4");

       // lenient().when(carService.findCarById("DZ-568-KY")).thenReturn(car);
        mockMvc.perform(MockMvcRequestBuilders.delete("/garage/delete/DZ-568-KY"))
                .andExpect(status().isOk());
    }
}