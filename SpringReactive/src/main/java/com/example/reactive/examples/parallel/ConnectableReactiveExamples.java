package com.example.reactive.examples.parallel;

import java.time.Duration;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

public class ConnectableReactiveExamples {

	public void connectableExample() {
		Flux<String> source = Flux.just("aaa", "bbb", "ccc", "ddd","eee","fff")
				.delayElements(Duration.ofSeconds(1))
	            .doOnNext(System.out::println).map(String::toUpperCase);
		 ConnectableFlux<String> connectable = source.publish();
		 connectable.connect();
		 connectable.subscribe(d -> System.out.println("Subscriber 1: "+d));
		
		 try {Thread.sleep(2000);}catch(Exception e) {}
		 connectable.subscribe(d -> System.out.println("Subscriber 2: "+d));
	}
	
	public void connectableReplayExample() {
		Flux<String> source = Flux.just("aaa", "bbb", "ccc", "ddd","eee","fff")
				.delayElements(Duration.ofSeconds(1))
	            .doOnNext(System.out::println).map(String::toUpperCase);
		 ConnectableFlux<String> connectable = source.publish();
		 connectable.subscribe(d -> System.out.println("Subscriber 1: "+d));
		 ConnectableFlux<String> connectableReplay=connectable.replay(Duration.ofSeconds(10));
		 connectableReplay.subscribe(d -> System.out.println("Subscriber 2: "+d));
		 connectable.connect();
		 try {Thread.sleep(2000);}catch(Exception e) {}
		 connectableReplay.connect();
	}
	
}
