package aziz.springreactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@Slf4j
public class ReactiveErrorTest {


    @Test
    public void fluxErrorHandling() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("exception")))
                .concatWith(Flux.just("D"))
                .onErrorResume((e) -> {
                    log.error("Ex :" + e);
                    return Flux.just("default");
                });

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                //expectError(Exception.class)
                .expectNext("default")
                .verifyComplete();

    }

    @Test
    public void fluxOnErrorReturn() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("exception")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default");


        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default")
                .verifyComplete();

    }

    @Test
    public void fluxOnErrorMap() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("exception")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e-> new CustomException(e));


        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();

    }

    @Test
    public void fluxOnErrorMapWithRetry() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("exception")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e-> new CustomException(e))
                .retry(2);


        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")//regular scenario
                .expectNext("A", "B", "C")//1st retry
                .expectNext("A", "B", "C")//2nd retry
                .expectError(CustomException.class)
                .verify();

    }

    @Test
    public void fluxOnErrorMapWithRetryWithBackOff() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("exception")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e-> new CustomException(e))
                .retryBackoff(2, Duration.ofSeconds(2));


        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")//regular scenario
                .expectNext("A", "B", "C")//1st retry
                .expectNext("A", "B", "C")//2nd retry
                .expectError(IllegalStateException.class)
                .verify();

    }

}
