package vde.dev.garage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"vde.dev.garage.service","vde.dev.garage.configuration","vde.dev.garage.controller","vde.dev.garage.repository","vde.dev.garage.configuration"})
//@SpringBootApplication
public class GarageApplication {

	public static void main(String[] args) {
		SpringApplication.run(GarageApplication.class, args);
	}

}
