package edu.eci.preparcial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "edu.eci.preparcial.persistence")
public class SportLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportLifeApplication.class, args);
    }
}
