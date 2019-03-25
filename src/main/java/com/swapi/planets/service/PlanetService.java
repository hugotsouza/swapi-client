package com.swapi.planets.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.swapi.planets.model.Planet;
import com.swapi.planets.model.client.PlanetModel;
import com.swapi.planets.model.client.StarWarsModel;
import com.swapi.planets.repository.PlanetRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlanetService {

	private PlanetRepository repository;

	@Autowired
	public PlanetService(PlanetRepository repository) {
		this.repository = repository;
	}

	public Mono<Planet> save(Planet planet) {
		planet.setTotalFilms(getTotalFilmsForName(planet.getName()));
		return repository.save(planet);
	}

	public Flux<Planet> getAllPlanets() {
		Flux<Planet> planets = repository.findAll();
		return planets;
	}

	public Mono<Planet> getPlanetById(String id) {
		return repository.findById(id);
	}

	public Mono<Planet> getPlanetByName(String name) {
		return repository.findByName(name);
	}

	public Mono<StarWarsModel> getStarWarPlanets(int page) {
		String url = "https://swapi.co/api/planets/?page=" + page;
		WebClient api = WebClient.builder()
			     .baseUrl(url)
			     .defaultHeader(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON.toString())
			     .defaultHeader(HttpHeaders.USER_AGENT, "Chrome/54.0.2840.99 Safari/537.36 Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)").build();
		Mono<StarWarsModel> mono = api.get().retrieve().bodyToMono(StarWarsModel.class);
        return mono;

	}

	public Mono<Void> deletePlanet(String id) {
		return repository.deleteById(id);
		
	}

	private int getTotalFilmsForName(String name) {
		String url = "https://swapi.co/api/planets/?search=" + name;
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		List<PlanetModel> planets = new ArrayList();

		ResponseEntity<StarWarsModel> response = restTemplate.exchange(url, HttpMethod.GET, entity, StarWarsModel.class);
		int total = 0;
		if (response.getBody().getCount() >= 1)
			total = response.getBody().getResults().get(0).getFilms().size();
		return total;
	}
	
}
