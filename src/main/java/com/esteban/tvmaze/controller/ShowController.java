package com.esteban.tvmaze.controller;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esteban.tvmaze.dto.ShowSearchResponse;
import com.esteban.tvmaze.service.ShowService;


@RestController
@RequestMapping("/api")
public class ShowController {
	
	public final ShowService showService;
	
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

	
	
}
