package com.example.reactive.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reactive.mongo.repo.ReactiveTodoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("todo")
public class ReactiveTodoController {

	@Autowired
	private ReactiveTodoRepository repo;
	
	@GetMapping(value="add/{id}/{daysLeft}/{desc}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<Todo> add(@PathVariable("id") String id, @PathVariable("daysLeft") int daysLeft, @PathVariable("desc") String desc) {
		Todo todo=new Todo();
		todo.setId(id);
		todo.setDaysLeft(daysLeft);
		todo.setDesc(desc);
		return repo.save(todo);
	}
	
	@GetMapping(value="all", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Todo> getAll() {
		return repo.findAll();
	}
	
	@GetMapping(value="all/daysLeft/{daysLeft}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Todo> getAll(@PathVariable("daysLeft") int daysLeft) {
		return repo.findAllByDaysLeft(daysLeft);
	}

}
