package com.example.reactive.mongo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.reactive.mongo.repo.ReactiveHobbitRepository;
import com.example.reactive.webFlux.HobbitFlux;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReactiveHobbitController {

	@Autowired
	private ReactiveHobbitRepository hobbit;
	@Autowired
	private HobbitFlux inMemory;
	private ConnectableFlux<Line> last3;
	
	
	@PostConstruct
	public void init() {
		if(!hobbit.existsById(1l).block()) {
		//create hobbit db	
			//hobbit.deleteAll().subscribe();
			List<Line> lines=new ArrayList<>();
			inMemory.flux.subscribe(line->lines.add(line));
			System.out.println(lines.size());
			System.out.println("EMPTY");
			lines.sort((l1,l2)->(int)(l1.getIndex()-l2.getIndex()));
			hobbit.saveAll(lines).subscribe();
		}
		last3=hobbit.findAll(Sort.by(Sort.Direction.ASC,"id")).delayElements(Duration.ofSeconds(2)).replay(3);
		last3.connect();
	}
	
	
	//					Mono & Flux

	
	@GetMapping(value="read/mongo/line/{number}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<Line> readLine(@PathVariable("number")long line){
		return hobbit.findById(line-1);
	}
	
	@GetMapping(value="read/mongo/line/{number}/{words}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> readLine(@PathVariable("number")long lineNo, @PathVariable("words")int numOfWords){
		List<String> words=new ArrayList<>();
		hobbit.findById(lineNo-1).subscribe(line->{
			StringTokenizer tokens=new StringTokenizer(line.getContent()," ");
			for(int i=0;i<numOfWords&&tokens.hasMoreTokens();i++) {
				words.add(tokens.nextToken());
			}
		});
		return Flux.fromIterable(words);
	}
	
	@GetMapping(value="read/mongo/{lines}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> readLines(@PathVariable("lines")long lines){
		return hobbit.findByIndexLessThan(lines-1);
	}
	
	@GetMapping(value="read/mongo/{from}/{to}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> readLines(@PathVariable("from")long from, 
			               @PathVariable("to")long to){
		return hobbit.findByIndexBetween(from, to);
	}
	
	@GetMapping(value="read/mongo/{from}/{to}/{delayMillis}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> readLine(@PathVariable("from")long from,
			               @PathVariable("to")long to,
						   @PathVariable("delayMillis")long delayMillis){
		return readLines(from-1,to+1).delayElements(Duration.ofMillis(delayMillis));
	}
	
	@GetMapping(value="read/mongo/all/{delayMillis}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> readAll(@PathVariable("delayMillis")long delayMillis){
		return hobbit.findAll(Sort.by(Sort.Direction.ASC,"id")).delayElements(Duration.ofMillis(delayMillis));
	}
	
	@GetMapping(value="read/mongo/until/{sec}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> readUntil(@PathVariable("sec")long sec){
		return hobbit.findAll(Sort.by(Sort.Direction.ASC,"id")).take(Duration.ofSeconds(sec));
	}
	
	
	
	//                          ConnectableFlux
	
	// hobbit.last3 is a ConnectableFlux<T> which consumes lines from hobbit Flux
	// 2 sec. delay between each line consumption allows to run the following method and 
	// join reading...
	
	@GetMapping(value="read/mongo/from/last", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> joinStream(){
		return last3;
	}
	
	@GetMapping(value="read/mongo/last", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Line> readLast3(){
		return last3.take(3);
	}
}
