package com.esteban.tvmaze.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "shows")
public record CachedShow(@Id Integer id, Map<String, Object> data) {
	
}
