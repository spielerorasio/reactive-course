package com.example.reactive.examples.parallel;

import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class ParallelReactiveExamples {

	public void handlingElementsInSeperateThread() {
		Flux.just("yellow","red", "white", "blue")
		.subscribeOn(Schedulers.parallel())  
		.map(String::toUpperCase)
		.log()
		.subscribe();
	}
	
	public void handlingEachElementInSeparateThread() {
		Flux.just("yellow","red", "white", "blue")
		.flatMap(s->Mono.just(s).subscribeOn(Schedulers.parallel()).delayElement(Duration.ofSeconds((int)(Math.random()*4))))
		.map(String::toUpperCase)
		.log()
		.subscribe();
	}
	
	public void parallelFlux() {
		Flux.just("yellow","red", "white", "blue")
		.parallel(2)
		.map(String::toUpperCase)
		.runOn(Schedulers.parallel())
		.log()
		.subscribe();
	}
	
	public void parallelFluxSequential() {
		Flux.just("yellow","red", "white", "blue")
		.parallel(2)
		.map(String::toUpperCase)
		.runOn(Schedulers.parallel())
		.log()
		.sequential()
		.map(String::toLowerCase)
		.log()
		.subscribe();
	}
}
