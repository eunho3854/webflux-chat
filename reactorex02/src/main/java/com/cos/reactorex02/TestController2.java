package com.cos.reactorex02;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@CrossOrigin
@RestController
public class TestController2 {

	Sinks.Many<String> sink;
	
	// multicast() 새로 들어온 데이터만 응답받음 hot (시퀀스 = 스트림)
	// reply() 기존 데이터 + 새로운 데이터 응답 cold 시퀀스 

	public TestController2() {
		this.sink = Sinks.many().multicast().onBackpressureBuffer();
	}
	
	@GetMapping(value = "/")
	public Flux<Integer> findAll() {
		return Flux.just(1).log();
	}
	
	@CrossOrigin
	@PostMapping("/send")
	public Mono<Chat> send(@RequestBody Chat chat) {
		// DB에 HelloWorld 저장
		sink.tryEmitNext(chat.getName() + " : " + chat.getContent());
		return Mono.just(chat);
	}

	// data : 실제값\n\n
	@GetMapping(value = "/sse")
	public Flux<ServerSentEvent<String>> sse() { // ServerSentEvent의 ContentType은 text event stream
		return sink.asFlux().map(e -> ServerSentEvent.builder(e).build()).doOnCancel(() -> {
			System.out.println("SSE 종료됨...");
			sink.asFlux().blockLast();
		}); // 구독
	}
}
