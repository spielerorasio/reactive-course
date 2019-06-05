package com.example.reactive.mongo.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.reactive.mongo.Line;

import reactor.core.publisher.Flux;

public interface ReactiveHobbitRepository extends ReactiveMongoRepository<Line, Long>{
	Flux<Line> findByIndexBetween(long low,long high);
	Flux<Line> findByIndexLessThan(long value);
	
}
