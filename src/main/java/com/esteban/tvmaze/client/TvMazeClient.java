package com.esteban.tvmaze.client;


import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Component
public class TvMazeClient {
	
	private final RestClient restClient;
	
	public TvMazeClient(RestClient.Builder builder) {
		this.restClient = builder.baseUrl("https://api.tvmaze.com").build();
	}
	
	public List<SearchResult> searchRows(String query){
		List<SearchResult> results = restClient.get()
				.uri("/search/shows?q={query}", query)
				.retrieve()
				.body(new ParameterizedTypeReference<List<SearchResult>>() {});
		
		if(results == null) {
			throw new IllegalStateException("TvMaze devolvió una respuesta vacía"); 
		}
		
		return results;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record SearchResult(Show show) {
		
	}
	
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Show(Integer id, String name, Channel network, Channel webChannel,
				String summary, List<String> genres) {
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Channel(String name) {
		
	}
	
	
	
	

}
