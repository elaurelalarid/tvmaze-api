package com.esteban.tvmaze.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import com.esteban.tvmaze.client.TvMazeClient;
import com.esteban.tvmaze.dto.ShowSearchResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esteban.tvmaze.model.CachedShow;
import com.esteban.tvmaze.repository.ShowRepository;

import java.util.stream.Collectors;

import com.esteban.tvmaze.dto.CommentResponse;
import com.esteban.tvmaze.model.ShowComment;
import com.esteban.tvmaze.repository.CommentRepository;



@Service
public class ShowService {
	
	
	private static final Logger log = LoggerFactory.getLogger(ShowService.class);
	
	private final TvMazeClient tvMazeClient;
	private final ShowRepository showRepository;
	private final CommentRepository commentRepository;
	
	
	public ShowService(
			TvMazeClient tvMazeClient, 
			ShowRepository showRepository,
			CommentRepository commentRepository) {
		this.tvMazeClient = tvMazeClient;
		this.showRepository = showRepository;
		this.commentRepository = commentRepository;
		
	}
	
	
	public List<ShowSearchResponse> searchShows(String query){
		var results = tvMazeClient.searchRows(query.trim());
		
		if(results.isEmpty()) {
			return List.of();
		}
		
		List<Integer> showIds = results.stream()
				.map(result -> result.show().id())
				.distinct()
				.toList();
		
		Map<Integer, List<CommentResponse>> commentsByShow = 
				commentRepository.findByShowIdInOrderByCreatedAtAsc(showIds)
				.stream()
				.collect(Collectors.groupingBy(
						ShowComment::showId,
						Collectors.mapping(
								comment -> new CommentResponse(
										comment.comment(),
										comment.rating()
						),
								Collectors.toList()
								)
						));
		
		return results.stream()
				.map(result -> {
					var show = result.show(); 
					
					String channel = null; 
					
					if(show.network() != null) {
						channel = show.network().name();
					}
					else if(show.webChannel()!= null) {
						channel = show.webChannel().name();
					}
					
					return new ShowSearchResponse(
							show.id(),
							show.name(),
							channel,
							show.summary(),
							show.genres(),
							commentsByShow.getOrDefault(show.id(), List.of())
							);
				})
				.toList();
			
	}
	
	
	// Obtener el show por id
	public Map<String, Object> getShowById(Integer showId){
		
		
		var cachedShow = showRepository.findById(showId);
		
		if(cachedShow.isPresent()) {
			log.info("Show {} obtenido de MongoDB", showId);
			return cachedShow.get().data();
		}
		
		
		try {
			log.info("Show {} no está en caché; consultando TVMaze", showId);
			Map<String, Object> show = tvMazeClient.getShowById(showId);
			showRepository.save(new CachedShow(showId, show));
			
			log.info("Show {} guardado en MongoDB", showId);
			return show;
			
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
