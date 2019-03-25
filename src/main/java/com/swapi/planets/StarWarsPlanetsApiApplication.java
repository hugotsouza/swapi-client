package com.swapi.planets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class StarWarsPlanetsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarWarsPlanetsApiApplication.class, args);
	}
	

}
