package aziz.springreactor.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@Slf4j
public class FluxAndMonoController {

    @GetMapping("/flux")
    public Flux<Integer> returnFlux() {
        return Flux.range(1, 3).delayElements(Duration.ofSeconds(1)).log();


    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> returnFluxStream() {
        return Flux.interval(Duration.ofSeconds(1)).log();


    }

    @GetMapping("/mono")
    public Mono<Integer> returnMono() {
        return Mono.just(1).log();


    }
}
