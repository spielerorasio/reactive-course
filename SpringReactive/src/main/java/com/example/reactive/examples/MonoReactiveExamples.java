package com.example.reactive.examples;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MonoReactiveExamples {

	public Mono<String> monoCreate(String value){
		return Mono.create(sink->{
			if(value.length()<10) {
				String response="Hello "+value+" !";
				sink.success(response);
			}else
				sink.error(new Exception("value ("+value+") is too long"));
		});
	}
	
	public Mono<String> monoDefer(String value){
		return Mono.defer(()->monoCreate("- "+value));
	}
	
	public Mono<Long> monoDelay(int sec){
		return Mono.delay(Duration.ofSeconds(sec));
	}
	
	public Mono<?> monoEmpty(){
		return Mono.empty();
	}

	public Mono<Throwable> monoError(String errMsg){
		return Mono.error(new Exception(errMsg));
	}
	
	public Mono<?> monoFirst(){
		return Mono.first(monoDelay(1),monoCreate("Create"),monoJust("Just"));
	}
	
	public Mono<String> monoJust(String data){
		return Mono.just(data);
	}
	
	//reduces to total words length
	public Mono<Integer> monoZip(String ...data){
		List<Mono<String>> monos=new ArrayList<>();
		for(int x=0;x<data.length;x++) {
			monos.add(Mono.just(data[x]));
		}
		return Mono.zip(monos,(results)->{
			    int total=0;
			    for(Object r:results) {
			    	total+=((String)r).length();
			    }
			    return total;
		});
	}
	
	public Mono<String> monoDoOnNext(){
		return Mono.just("1").doOnNext(System.out::print).doOnNext(s->System.out.println(" --("+s+")--"));
	}
	
	public Mono<String> monoDelayElement(){
		return Mono.just("Hi ! ").doOnNext(System.out::print)
				  .delayElement(Duration.ofSeconds(2)).doOnNext(System.out::print)
				  .delayElement(Duration.ofSeconds(2)).doOnNext(System.out::print);
	}
	
	public Mono<String> monoDoOnTerminate(){
		return Mono.just("Done ! ").doOnTerminate(()->System.out.println("On Terminate"));
	}
	
	public Mono<Integer> monoFilter(int value){
		return Mono.just(value).filter(i->i%2==0);
	}
	
	public Mono<Integer> monoFlatMap(String value){
		return Mono.just(value).flatMap(s->Mono.just(s.length()));
	}
	
	public Mono<String> monoOr(int delay1, int delay2){
		Mono<String> first=Mono.just("First").delayElement(Duration.ofSeconds(delay1));
		Mono<String> last=Mono.just("Second").delayElement(Duration.ofSeconds(delay2));
		return first.or(last);
	}
	
	public Mono<?> monoRetry(int numOfTries){
		return Mono.error(new Exception()).doOnEach(e->System.out.print(" %%%%")).retry(numOfTries);
	}
	
	//  _________________________________________________
	//  MONO TO FLUX - Mono methods which results in Flux 
	
	public Flux<String> monoConcat(){
		return Mono.just("Hello").concatWith(Flux.just(" ","Reactive"," ","World","!"));
	}
	
	public Flux<Integer> monoExpand(int value){
		return Mono.just(value).expand(val->{
			List<Integer> data=new ArrayList<>();
			for(int i=0;i<val;i++){
				data.add(i);
			}
			System.out.println(data);
			return Flux.fromIterable(data);
		});
	}
	
	public Flux<Integer> monoExpandDeep(int value){
		return Mono.just(value).expandDeep(val->{
			List<Integer> data=new ArrayList<>();
			for(int i=0;i<val;i++){
				data.add(i);
			}
			System.out.println(data);
			return Flux.fromIterable(data);
		});
	}
	
	public Flux<Character> monoFlatMapIterable(int value){
		return Mono.just(value).flatMapIterable(val->{
			List<Character> data=new ArrayList<>();
			for(int i=0;i<val;i++){
				char curr=(char)('a'+i);
				data.add(curr);
			}
			return data;
		});
	}
	
	public Flux<String> monoMergeWith(){
		return Mono.just("Hello").mergeWith(Flux.just(" Reactive"," !!!"));
	}
	
	public Flux<String> monoRepeat(int numOfRepeats){
		return Mono.just("repeat ").repeat(numOfRepeats);
	}
	
	public Flux<Integer> monoRepeatPredicate(int numOfRepeats){
		int value=(int)(Math.random()*10);
		System.out.print("("+value+") ");
		return Mono.just(value).repeat(numOfRepeats,()->value%2==0);
	}
	
	
}
