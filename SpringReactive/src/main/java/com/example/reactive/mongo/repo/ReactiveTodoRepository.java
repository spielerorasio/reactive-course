package com.example.reactive.mongo.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.reactive.mongo.Todo;

import reactor.core.publisher.Flux;

public interface ReactiveTodoRepository extends ReactiveCrudRepository<Todo, String>{
	
	Flux<Todo> findAllByDaysLeft(int daysLeft); 
}
