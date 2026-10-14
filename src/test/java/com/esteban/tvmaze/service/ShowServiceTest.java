package com.esteban.tvmaze.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esteban.tvmaze.client.TvMazeClient;
import com.esteban.tvmaze.dto.CommentResponse;
import com.esteban.tvmaze.model.CachedShow;
import com.esteban.tvmaze.model.ShowComment;
import com.esteban.tvmaze.repository.CommentRepository;
import com.esteban.tvmaze.repository.ShowRepository;

class ShowServiceTest {

	private TvMazeClient client; 
	private ShowRepository shows;
	private CommentRepository comments; 
	private ShowService service; 
	
	@BeforeEach
	void setUp() {
		client = mock(TvMazeClient.class); 
		shows = mock(ShowRepository.class); 
		comments = mock(CommentRepository.class); 
		service = new ShowService(client, shows, comments); 
	}
	
	@Test
	void cachedShowDoesNotCallTvMaze() {
		Map<String, Object> data = Map.of("id", 975, "name", "Batman"); 
		
		when(shows.findById(975))
		.thenReturn(Optional.of(new CachedShow(975, data))); 
		
		assertEquals(data, service.getShowById(975)); 
		
		verifyNoInteractions(client); 
		verify(shows, never()).save(any(CachedShow.class)); 
	}
	
	@Test
	void missingShowIsFetchedAndSaved() {
		Map<String, Object> data = Map.of("id", 975, "name", "Batman"); 
		
		when(shows.findById(975)).thenReturn(Optional.empty()); 
		when(client.getShowById(975)).thenReturn(data); 
		
		assertEquals(data, service.getShowById(975));
		
		verify(client).getShowById(975);
		verify(shows).save(new CachedShow(975, data)); 
		
	}
	
	
	@Test
	void detailIncludeCommentsWithoutChangingCachedData() {
		Map<String, Object> data = Map.of("id", 975, "name", "Batman"); 
		
		when(shows.findById(975)).thenReturn(Optional.of(new CachedShow(975, data))); 
		
		when(comments.findByShowIdOrderByCreatedAtAsc(975))
		.thenReturn(List.of(new ShowComment("comment-1", 975, "Película excelente",5, Instant.EPOCH)));
		
		var response = service.getShowWithComments(975); 
		
		assertEquals(
				List.of(new CommentResponse("Película excelente", 5)), 
				response.get("comments")
				);
		
		assertEquals("Batman", response.get("name")); 
		assertFalse(data.containsKey("comments"));
		verifyNoInteractions(client); 

	}
}
