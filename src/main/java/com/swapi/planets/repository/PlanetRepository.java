package com.swapi.planets.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swapi.planets.model.Planet;

import reactor.core.publisher.Mono;

public interface PlanetRepository extends ReactiveMongoRepository<Planet, String>{

	Mono<Planet> findByName(String name);
}
