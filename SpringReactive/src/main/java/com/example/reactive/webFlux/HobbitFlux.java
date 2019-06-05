package com.example.reactive.webFlux;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.reactive.mongo.Line;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

@Component
public class HobbitFlux {

	public Flux<Line> flux;
	public ConnectableFlux<Line> last3; 
	
	private HobbitFlux() {
		try (BufferedReader in=new BufferedReader(
			                   new FileReader("./src/main/resources/static/Hobbit.txt"))){	                           
			List<Line> data=new ArrayList<>();
			Line line=new Line();
			long index=1;
			line.setIndex(index);
			line.setContent(in.readLine());
			while(line.getContent()!=null) {
				data.add(line);
				line=new Line();
				index++;
				line.setIndex(index);
				line.setContent(in.readLine());
			}
			flux=Flux.fromIterable(data);
			last3=flux.delayElements(Duration.ofSeconds(2)).replay(3);
			last3.connect();
			
			return;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
