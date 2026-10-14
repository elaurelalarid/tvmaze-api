package com.esteban.tvmaze.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.esteban.tvmaze.dto.ShowSearchResponse;
import com.esteban.tvmaze.service.ShowService;


@RestController
@RequestMapping("/api")
public class ShowController {
	
	private final ShowService showService;
	
	public ShowController(ShowService showService) {
		this.showService = showService;
	}
	
	@GetMapping("/search")
	public List<ShowSearchResponse> search(
			@RequestParam(name = "search_query")
			@NotBlank(message = "search_query no puede estar vacío")
			@Size(max = 200, message = "search_query no debe superar los 200 caracteres")
			String query){
		return showService.searchShows(query);
	}

	@GetMapping("/shows/{show_id}")
	public Map<String, Object> getShow(
			@PathVariable(name = "show_id")
			@Positive(message = "show_id debe ser mayor que cero")
			Integer showId){
		return showService.getShowWithComments(showId); 
	}
	
	
}
