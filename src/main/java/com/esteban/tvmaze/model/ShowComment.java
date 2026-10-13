package com.esteban.tvmaze.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
public record ShowComment(
        @Id String id,
        Integer showId,
        String comment,
        Integer rating,
        Instant createdAt){
	}