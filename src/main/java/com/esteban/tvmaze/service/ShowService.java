package com.esteban.tvmaze.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
	
}
