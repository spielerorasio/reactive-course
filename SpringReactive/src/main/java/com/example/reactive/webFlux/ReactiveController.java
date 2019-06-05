package com.example.reactive.webFlux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.reactive.mongo.Line;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReactiveController {

	@Autowired
	private HobbitFlux hobbit;
	
	
	//					Mono & Flux

	
	@GetMapping(value="read/line/{number}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<Line> readLine(@PathVariable("number")int line){
		return hobbit.flux.elementAt(line-1);
	}
	
	@GetMapping(value="read/line/{number}/{words}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> readLine(@PathVariable("number")int lineNo, @PathVariable("words")int numOfWords){
		List<String> words=new ArrayList<>();
		hobbit.flux.elementAt(lineNo-1).subscribe(line->{
			StringTokenizer tokens=new StringTokenizer(line.getContent()," ");
			for(int i=0;i<numOfWords&&tokens.hasMoreTokens();i++) {
				words.add(tokens.nextToken());
			}
		});
		return Flux.fromIterable(words);
	}
	
	@GetMapping(value="read/{lines}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> readLines(@PathVariable("lines")long lines){
		return hobbit.flux.take(lines);
	}
	
	@GetMapping(value="read/{from}/{to}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> readLines(@PathVariable("from")long from, 
			               @PathVariable("to")long to){
		return hobbit.flux.skipWhile(line->line.getIndex()<from).takeUntil(line->line.getIndex()>=to);
	}
	
	@GetMapping(value="read/{from}/{to}/{delayMillis}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> readLine(@PathVariable("from")long from,
			               @PathVariable("to")long to,
						   @PathVariable("delayMillis")long delayMillis){
		return readLines(from,to).delayElements(Duration.ofMillis(delayMillis));
	}
	
	@GetMapping(value="read/all/{delayMillis}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> readAll(@PathVariable("delayMillis")long delayMillis){
		return hobbit.flux.delayElements(Duration.ofMillis(delayMillis));
	}
	
	@GetMapping(value="read/until/{sec}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> readUntil(@PathVariable("sec")long sec){
		return hobbit.flux.take(Duration.ofSeconds(sec));
	}
	
	
	
	//                          ConnectableFlux
	
	// hobbit.last3 is a ConnectableFlux<T> which consumes lines from hobbit Flux
	// 2 sec. delay between each line consumption allows to run the following method and 
	// join reading...
	
	@GetMapping(value="read/from/last", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> joinStream(){
		return hobbit.last3;
	}
	
	@GetMapping(value="read/last", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> readLast3(){
		return hobbit.last3.take(3);
	}
}
