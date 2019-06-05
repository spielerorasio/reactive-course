package com.example.reactive.examples;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Component
public class FluxReactiveExamples {

	public Flux<Double> fluxCreate(){
		return Flux.create(sink->sink.next(Math.random()));
	}
	
	public Flux<String> fluxFrom(){
		return Flux.fromStream(Stream.of("aaa","bbb","ccc"));
	}
	
	public Flux<String> fluxConcat(String...values){
		return fluxFrom().concatWithValues(values);
	}
	
	public Flux<Integer> fluxDefer(){
		return Flux.defer(()->Flux.fromStream(IntStream.range(1,6).mapToObj(i->new Integer(i))));
	}
	
	public Flux<Long> fluxInterval(int millis, int count){
		return Flux.interval(Duration.ofMillis(millis)).take(count);
	}
	
	public Flux<Object> fluxMerge(){
		return Flux.merge(fluxInterval(100, 10),fluxFrom().delayElements(Duration.ofMillis(100)));
	}
	
	public Flux<Integer> fluxMergeOrdered(){
		Integer [] random1=randomizeArray();
		Integer [] random2=randomizeArray();
		return Flux.mergeOrdered(Flux.just(random1).delayElements(Duration.ofMillis(200)),
				                 Flux.just(random2).delayElements(Duration.ofMillis(200)));		
	}
	
	public Flux<Object> fluxMergeSequential(){
		return Flux.mergeSequential(fluxInterval(100, 10),fluxFrom(),fluxDefer());
	}
	
	public Flux<Integer> fluxRange(){
		return Flux.range(0,10);
	}
	
	public Flux<String> fluxZip(){
		return  Flux.zip((values)->{
			String result="";
			for(Object o:values) {
				result+=" "+o.toString();
			}
			return result;
		},Flux.range(1,3),fluxFrom());
	}
	
	public Integer fluxAs(Integer...values){
		AtomicInteger i=new AtomicInteger(0);
		Flux.fromArray(values).as(flux->{
			flux.doOnNext(v->i.addAndGet(v)).subscribe();
			return 0;
		});
		return i.get();
	}
	
	public Flux<List<Integer>> fluxBuffer(){
		return Flux.range(1,9).buffer();
	}
	
	public Flux<List<Integer>> fluxBufferMaxSize(){
		return Flux.range(1,14).buffer(5);
	}
	
	public Flux<List<Integer>> fluxBufferUntil(){	
		return Flux.fromArray(randomizeArray()).bufferUntil(n->n==1||n==9	);
	}
	
	public Flux<Integer> fluxDelay(){
		return Flux.range(1,10).delayElements(Duration.ofMillis(100));
	}
	
	public Flux<Integer> fluxDistinct(){
		Flux<Integer> origin=Flux.fromArray(randomizeArray());
		System.out.print("Origin: ");
		origin.subscribe(System.out::print);
		System.out.println();
		return origin.distinct();
	}
	
	public Flux<Integer> fluxDoOnEach(){
		return Flux.range(1,10).doOnEach(signal->
		{	
			if(signal.isOnNext()) 
				System.out.print("_");
			if(signal.isOnComplete())
				System.out.print(" Done!");
		});
	}
	
	public Flux<Integer> fluxScan(){
		return Flux.range(0,11).scan((n1,n2)->n1+n2);
	}
	
	public Mono<Integer> fluxSingle(Integer...values){
		return Flux.fromArray(values).single(100);
	}
	
	public Flux<Integer> fluxSkip(int startFrom){
		return Flux.range(0,10).skip(startFrom);
	}
	
	public void fluxDispose(){
		Disposable d=Flux.interval(Duration.ofMillis(100)).subscribe(System.out::print);
		try {Thread.sleep(1000);}catch (Exception e) {}
		d.dispose();
	}
	
	public Flux<Integer> fluxTake(int endAt){
		return Flux.range(0,10).take(endAt);
	}
	
	public Flux<?> fluxThenMany(){
		return Flux.range(0,10).doOnNext(System.out::print).thenMany(fluxFrom());
	}
	
	public Flux<Flux<Integer>> fluxWindow(){
		return Flux.range(0,10).window(3).doOnNext(s->System.out.print(" window: "));
	}
	
	public Flux<Tuple2<Integer, String>> fluxZipWith(){
		return Flux.range(1,3).zipWith(fluxFrom());
	}
	
	
	
	private Integer[] randomizeArray() {
		Integer [] random=new Integer[20];
		for(int i=0;i<random.length;i++) {
			random[i]=(int)(Math.random()*10);
		}
		return random;
	}
	
	public void  fluxShare() {
		Flux<Long> startFlux = Flux.interval(Duration.ofSeconds(1)).take(10).share();
		Flux firstFlux = Flux.from(startFlux);
		firstFlux.subscribe(out -> System.out.println("firstFlux value: " + out));
		try {Thread.sleep(5000);}catch (Exception e) {}
		Flux secondFlux = Flux.from(startFlux);
		secondFlux.subscribe(out -> System.out.println("secondFlux value: " + out));
		try {Thread.sleep(5000);}catch (Exception e) {}
	}
	
	public void  fluxCacheFull() {
		Flux<Long> startFlux = Flux.interval(Duration.ofSeconds(1)).take(10).share().cache();
		Flux firstFlux = Flux.from(startFlux);
		firstFlux.subscribe(out -> System.out.println("firstFlux value: " + out));
		try {Thread.sleep(5000);}catch (Exception e) {}

		Flux secondFlux = Flux.from(startFlux);
		secondFlux.subscribe(out -> System.out.println("secondFlux value: " + out));
		try {Thread.sleep(5000);}catch (Exception e) {}
	}
	
	public void  FluxCacheLast() {
		Flux<Long> startFlux = Flux.interval(Duration.ofSeconds(1)).take(10).share().cache(2);
		Flux firstFlux = Flux.from(startFlux);
		firstFlux.subscribe(out -> System.out.println("firstFlux value: " + out));
		try {Thread.sleep(5000);}catch (Exception e) {}

		Flux secondFlux = Flux.from(startFlux);
		secondFlux.subscribe(out -> System.out.println("secondFlux value: " + out));
		try {Thread.sleep(5000);}catch (Exception e) {}
	}
	
	//  _________________________________________________
	//  FLUX TO MONO - Flux methods which results in Mono 
	
	
	public Mono<Boolean> fluxAll(Integer...numbers){
		return Flux.fromArray(numbers).all(num->num%2!=0);
	}
	
	public Mono<Boolean> fluxAny(Integer...numbers){
		return Flux.fromArray(numbers).any(num->num%2!=0);
	}
	
		public Mono<List<Integer>> fluxCollectList(Integer...numbers){
			return Flux.fromArray(numbers).collectList();
		}
	
	public Mono<Map<Integer,String>> fluxCollectMap(String ...values){
		return Flux.fromArray(values).collectMap(str->str.length());
	}
	
	public Mono<Long> fluxCount(String...values){
		return Flux.fromArray(values).count();
	}
	
	public Mono<String> fluxElementAt(int position){
		return fluxFrom().elementAt(position,"Not Found");
	}
	
	public Mono<Boolean> fluxHasElement(String value){
		return fluxFrom().hasElement(value);
	}
	
	public Mono<String> fluxLast(){
		return fluxFrom().last();
	}
	
	public Mono<String> fluxNext(){
		return fluxFrom().next();
	}
	
	public Mono<Integer> fluxReduce(String...values){
		return Flux.fromArray(values).reduce(0,(result,value)->result+=value.length());
	}
	
	public Mono<String> fluxThen(){
		return Flux.just(1,2,3,4,5).doOnNext(System.out::print).then(Mono.just(" Done"));
	}
}
