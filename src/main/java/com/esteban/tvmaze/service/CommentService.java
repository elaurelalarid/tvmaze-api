package com.esteban.tvmaze.service;

import java.time.Instant; 

import org.springframework.stereotype.Service; 

import com.esteban.tvmaze.dto.CommentRequest;
import com.esteban.tvmaze.model.ShowComment;
import com.esteban.tvmaze.repository.CommentRepository;


@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	private final ShowService showService; 
	
	public CommentService(
			CommentRepository commentRepository,
			ShowService showService) {
		this.commentRepository = commentRepository; 
		this.showService = showService;
	}
	
	public void saveComment(CommentRequest request) {
		showService.getShowById(request.show_id());
		
		ShowComment comment = new ShowComment(
				null, 
				request.show_id(),
				request.comment().trim(),
				request.rating(),
				Instant.now());
		
		commentRepository.save(comment); 
	}
	

}
