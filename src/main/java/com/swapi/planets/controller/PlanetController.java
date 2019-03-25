package com.swapi.planets.controller;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.swapi.planets.model.Planet;
import com.swapi.planets.model.client.StarWarsModel;
import com.swapi.planets.service.PlanetService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/planets")
public class PlanetController {

	private PlanetService service;

	@Autowired
	public PlanetController(PlanetService service) {
		this.service = service;
	}
	
	@GetMapping("/starwars")
	public Mono<ResponseEntity<StarWarsModel>> getStarWarsPlanets(@RequestParam(value="page", defaultValue="1") int page) {
		return service.getStarWarPlanets(page).map(planet -> ResponseEntity.ok(planet))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping
	public Flux<Planet> getAllPlanets() {
		return service.getAllPlanets();
	}

	@GetMapping("{id}")
	public Mono<ResponseEntity<Planet>> getPlanet(@PathVariable String id) {
		return service.getPlanetById(id).map(planet -> ResponseEntity.ok(planet))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/name/{name}")
	public Mono<ResponseEntity<Planet>> getPlanetByName(@PathVariable String name) {
		return service.getPlanetByName(name).map(planet -> ResponseEntity.ok(planet))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("{id}")
	public Mono<Object> deletePlanet(@PathVariable String id) {
		return service.deletePlanet(id).map(m -> ResponseEntity.status(204).build());
		
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Planet> savePlanet(@RequestBody Planet planet) {
		return service.save(planet);
	}
	
	@ExceptionHandler 
    public ResponseEntity<String> handle(WebClientResponseException ex) {
        return ResponseEntity.notFound().build();
    }

	@ExceptionHandler 
    public ResponseEntity<String> handle(UnknownHostException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Network error.");
    }
}
