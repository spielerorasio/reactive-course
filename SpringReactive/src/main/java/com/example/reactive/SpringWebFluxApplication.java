package com.example.reactive;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.reactive.examples.FluxReactiveExamples;
import com.example.reactive.examples.MonoReactiveExamples;
import com.example.reactive.examples.parallel.ConnectableReactiveExamples;
import com.example.reactive.examples.parallel.ParallelReactiveExamples;
import com.example.reactive.webFlux.HobbitFlux;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringWebFluxApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx= SpringApplication.run(SpringWebFluxApplication.class, args);
		runExamples(ctx);
	}
	
	private static void runExamples(ConfigurableApplicationContext ctx) {
		// Mono Examples:
		MonoReactiveExamples mex=ctx.getBean(MonoReactiveExamples.class);
		mono(mex);
			
		// Flux Examples:		
		FluxReactiveExamples fex=ctx.getBean(FluxReactiveExamples.class);
		flux(fex);
		 
		// Mono to Flux Examples:
		monoToFlux(mex);
		 
		// Mono to Flux Examples:
		fluxToMono(fex);
				
		// Parallel Example:		
		ParallelReactiveExamples pex=new ParallelReactiveExamples();
		parallel(pex);	
				
		// Connectable Example:		
		ConnectableReactiveExamples cex=new ConnectableReactiveExamples();
		connectable(cex);	
	}

	public static void mono(MonoReactiveExamples ex) {
		System.out.println("\nMONO EXAMPLES\n");
		System.out.println("\nMono Create: "+ex.monoCreate("David").block());
//		System.out.println("\nMono Create: "+ex.monoCreate("David12345").block());
		System.out.println("\nMono Defer: "+ex.monoDefer("Eve").block());
		System.out.println("\nMono Delay: "+ex.monoDelay(1).block());
		System.out.println("\nMono Empty: "+ex.monoEmpty().block());
//		System.out.println("\nMono Error: "+ex.monoError("Some Error").block());
		System.out.println("\nMono First: "+ex.monoFirst().block());
		System.out.println("\nMono Just: "+ex.monoJust("Just..").block());
		System.out.println("\nMono Zip: "+ex.monoZip("a","bb","ccc").block());
		System.out.println("\nMono DoOnNext: ");
		System.out.print("             ");ex.monoDoOnNext().block();
		System.out.println("\nMono Delay Element: ");
		System.out.print("                     ");ex.monoDelayElement().block();
		System.out.println("\n\nMono On Terminate: ");
		System.out.println("                    "+ex.monoDoOnTerminate().block());
		System.out.println("\nMono Filter(passes test): "+ex.monoFilter(20).block());
		System.out.println("\nMono Filter(fails test): "+ex.monoFilter(13).block());
		System.out.println("\nMono FlatMap: "+ex.monoFlatMap("Hello Reactive !").block());
		System.out.println("\nMono Or: "+ex.monoOr(1,2).block());
//		System.out.println("Mono Retry");
//		System.out.println(ex.monoRetry(3).block());
		
	}
	
	public static void flux(FluxReactiveExamples ex) {
		System.out.println("\n\nFLUX EXAMPLES\n");
		System.out.print("\nFlux Create: \n");
		Flux<Double> f=ex.fluxCreate();
		f.subscribe(System.out::println);
		f.subscribe(System.out::println);
		System.out.print("\nFlux From: ");
		ex.fluxFrom().subscribe(System.out::print);
		System.out.print("\n\nFlux Concat: ");
		ex.fluxConcat("1","2","3").subscribe(System.out::print);
		System.out.print("\n\nFlux Defer: ");
		ex.fluxDefer().subscribe(System.out::print);
		System.out.print("\n\nFlux Interval: ");
		ex.fluxInterval(500,10).subscribe(System.out::print);
		try {Thread.sleep(2000);}catch(Exception e) {}
		System.out.print("\n\nFlux Merge: ");
		ex.fluxMerge().subscribe(System.out::print);
		try {Thread.sleep(4000);}catch(Exception e) {}
		System.out.print("\n\nFlux Merge Ordered: ");
		ex.fluxMergeOrdered().subscribe(System.out::print);
		try {Thread.sleep(6000);}catch(Exception e) {}
		System.out.print("\n\nFlux Merge Sequential: ");
		ex.fluxMergeSequential().subscribe(System.out::print);
		try {Thread.sleep(4000);}catch(Exception e) {}
		System.out.print("\n\nFlux Range: ");
		ex.fluxRange().subscribe(System.out::print);
		System.out.print("\n\nFlux Zip: \n");
		ex.fluxZip().subscribe(System.out::println);
		System.out.print("\n\nFlux As: "+ ex.fluxAs(1,2,3));
		System.out.print("\n\nFlux Buffer: \n");
		ex.fluxBuffer().subscribe(System.out::println);
		System.out.print("\nFlux Buffer Max Size: \n");
		ex.fluxBufferMaxSize().subscribe(System.out::print);
		System.out.print("\n\nFlux Buffer Until: \n");
		Flux<List<Integer>> fl=ex.fluxBufferUntil();
		fl.subscribe(System.out::print);
		System.out.println("\n\nFlux Count: "+fl.count().block());
		System.out.print("\n\nFlux Delay Elements: \n");
		ex.fluxDelay().subscribe(System.out::print);
		try {Thread.sleep(2000);}catch(Exception e) {}
		System.out.print("\n\nFlux Distinct: \n");
		ex.fluxDistinct().subscribe(System.out::print);
		System.out.print("\n\nFlux Do On Each: \n");
		ex.fluxDoOnEach().subscribe(System.out::print);
		System.out.print("\n\nFlux Scan: \n");
		ex.fluxScan().subscribe(System.out::println);
		System.out.print("\n\nFlux Single: \n");
		ex.fluxSingle().subscribe(System.out::println);
		ex.fluxSingle(50).subscribe(System.out::println);
		System.out.print("\n\nFlux Skip: \n");
		ex.fluxSkip(5).subscribe(System.out::print);
		System.out.print("\n\nFlux Dispose Subscription: \n");
		ex.fluxDispose();
		try {Thread.sleep(4000);}catch(Exception e) {}
		System.out.print("\n\nFlux Take: \n");
		ex.fluxTake(5).subscribe(System.out::print);
		System.out.print("\n\nFlux Then Many: \n");
		ex.fluxThenMany().subscribe(System.out::print);
		System.out.print("\n\nFlux Window: \n");
		ex.fluxWindow().subscribe(flux->flux.subscribe(System.out::print));
		System.out.print("\n\nFlux Zip With: \n");
		ex.fluxZipWith().subscribe(tuple2->System.out.println(tuple2.getT1()+"-"+tuple2.getT2()));
		System.out.println("\nFlux Share (No Cache) ");
		ex.fluxShare();
		System.out.println("\nFlux Cache - All Elements");
		ex.fluxCacheFull();
		System.out.println("\nFlux Cache - Last 2 Elements");
		ex.FluxCacheLast();
	}
	
	public static void monoToFlux(MonoReactiveExamples ex) {
		System.out.print("\n\nMono to Flux - Concat: \n");
		ex.monoConcat().subscribe(System.out::print);
		System.out.print("\n\nMono to Flux - Expand: \n");
		ex.monoExpand(3).subscribe(System.out::print);
		System.out.print("\n\nMono to Flux - Expand Deep: \n");
		ex.monoExpandDeep(3).subscribe(System.out::print);
		System.out.print("\n\nMono to Flux - Flat Map Iterable: \n");
		ex.monoFlatMapIterable(5).subscribe(System.out::print);
		System.out.print("\n\nMono to Flux - Merge With: \n");
		ex.monoMergeWith().subscribe(System.out::print);
		System.out.print("\n\nMono to Flux - Repeat: \n");
		ex.monoRepeat(3).subscribe(System.out::print);
		System.out.print("\n\nMono to Flux - Repeat Predicate: \n");
		ex.monoRepeatPredicate(3).subscribe(System.out::print);
	}
	
	public static void fluxToMono(FluxReactiveExamples ex) {
		System.out.print("\n\nFlux to Mono - All: \n");
		ex.fluxAll(1,5,6).subscribe(System.out::print);
		System.out.print("\n\nFlux to Mono - Any: \n");
		ex.fluxAny(1,4,5).subscribe(System.out::print);
		System.out.print("\n\nFlux to Mono - Collect List: \n");
		ex.fluxCollectList(1,2,3).subscribe(System.out::print);
		System.out.print("\n\nFlux to Mono - Collect Map: \n");
		ex.fluxCollectMap("a","bb","ccc").subscribe(System.out::print);
		System.out.print("\n\nFlux to Mono - Count: \n");
		ex.fluxCount("a","bb","ccc").subscribe(System.out::print);
		System.out.print("\n\nFlux to Mono - Element At: \n");
		ex.fluxElementAt(2).subscribe(System.out::print);
		System.out.print("\n\nFlux to Mono - Has Element: \n");
		ex.fluxHasElement("zzz").subscribe(System.out::print);
		System.out.print("\n\nFlux to Mono - Last: \n");
		ex.fluxLast().subscribe(System.out::print);
		System.out.print("\n\nFlux to Mono - Next: \n");
		ex.fluxNext().subscribe(System.out::print);
		System.out.print("\n\nFlux to Mono - Reduce: \n");
		ex.fluxReduce("a","bb","ccc").subscribe(System.out::print);
		System.out.print("\n\nFlux to Mono - Then: \n");
		ex.fluxThen().subscribe(System.out::print);
	}
	
	public static void parallel(ParallelReactiveExamples pex) {
		System.out.print("\n\nParallel: Handling all elements in seperate thread: \n");
		pex.handlingElementsInSeperateThread();
		try {Thread.sleep(2000);}catch(Exception e) {}
		System.out.print("\n\nParallel: Handling each element in separate thread: \n");
		pex.handlingEachElementInSeparateThread();
		try {Thread.sleep(4000);}catch(Exception e) {}
		System.out.print("\n\nParallel: ParallelFlux: \n");
		pex.parallelFlux();
		try {Thread.sleep(4000);}catch(Exception e) {}
		System.out.print("\n\nParallel: ParallelFluxSequental: \n");
		pex.parallelFluxSequential();
		try {Thread.sleep(4000);}catch(Exception e) {}
	}
	
	private static void connectable(ConnectableReactiveExamples cex) {
		System.out.print("\n\nConnectable: ConnectableFlux: \n");
		cex.connectableExample();
		try {Thread.sleep(6000);}catch(Exception e) {}
		System.out.print("\n\nConnectable: ConnectableFlux Replay: \n");
		cex.connectableReplayExample();
		
	}

}

