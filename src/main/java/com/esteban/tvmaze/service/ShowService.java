package com.esteban.tvmaze.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;


import com.esteban.tvmaze.client.TvMazeClient;
import com.esteban.tvmaze.dto.ShowSearchResponse;


@Service
public class ShowService {

	private final TvMazeClient tvMazeClient;
	
	public ShowService(TvMazeClient tvMazeClient) {
		this.tvMazeClient = tvMazeClient;
	}
	
	
	// Transformamos la respuesta externa en los campos solicitados
	public List<ShowSearchResponse> searchShows(String query){
		return tvMazeClient.searchRows(query.trim())
				.stream()
				.map(result -> {
					var show = result.show();
					
					String channel = null;  
					
					// Si network no existe usamos webChannel
					if(show.network() != null) {
						channel = show.network().name(); 
					}
					else if(show.webChannel()!=null) {
						channel = show.webChannel().name();
					}
					
					return new ShowSearchResponse(
							show.id(),
							show.name(),
							channel,
							show.summary(),
							show.genres());
				})
				.toList();
	}
	
	
	// Obtener el show por id
	public Map<String, Object> getShowById(Integer showId){
		try {
			return tvMazeClient.getShowById(showId);
		}
		// Si no se encuentra, retornamos NOT FOUND (404)
		catch(HttpClientErrorException.NotFound exception) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, 
					"Show no encontrado", 
					exception);
		}
	}
	
}
