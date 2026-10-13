package com.esteban.tvmaze.dto;

import java.util.List;

public record ShowSearchResponse(Integer id, String name, String channel, String summary, 
		List<String> genres, List<CommentResponse> comments) {

		
}
