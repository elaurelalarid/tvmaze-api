package com.esteban.tvmaze.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.esteban.tvmaze.model.CachedShow;


public interface ShowRepository extends MongoRepository<CachedShow, Integer>{

}
