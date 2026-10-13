package com.esteban.tvmaze.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.esteban.tvmaze.model.ShowComment;


public interface CommentRepository extends MongoRepository<ShowComment, String>{

	List<ShowComment> findByShowIdOrderByCreatedAtAsc(Integer showId);
	
	List<ShowComment> findByShowIdInOrderByCreatedAtAsc(List<Integer> showIds); 
	
}
