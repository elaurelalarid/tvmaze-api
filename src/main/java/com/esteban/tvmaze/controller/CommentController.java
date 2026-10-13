package com.esteban.tvmaze.controller;



import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esteban.tvmaze.dto.CommentRequest;
import com.esteban.tvmaze.service.CommentService;


@RestController
@RequestMapping("/api/comments")
public class CommentController {
	
	private final CommentService commentService; 
	
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@PostMapping
	public ResponseEntity<Map<String, String>> createComment(
			@Valid @RequestBody CommentRequest request){
		
		commentService.saveComment(request);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("status", "created")); 
	}
}
