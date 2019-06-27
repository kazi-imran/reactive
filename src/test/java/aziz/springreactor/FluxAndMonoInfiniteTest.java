package aziz.springreactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@Slf4j
public class FluxAndMonoInfiniteTest {

    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(100)).log();

        interval.subscribe(e -> log.info("Value : {}", e));

        Thread.sleep(3000);

    }

    @Test
    public void infiniteSequenceMapTest() throws InterruptedException {

        Flux<Integer> interval = Flux.interval(Duration.ofMillis(100))
                .map(l -> Integer.parseInt(l.toString()))
                .take(3)
                .log();

        StepVerifier.create(interval)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceMapWithDelay() throws InterruptedException {

        Flux<Integer> interval = Flux.interval(Duration.ofMillis(100))
                .delayElements(Duration.ofSeconds(1))
                .map(l -> Integer.parseInt(l.toString()))
                .take(3)
                .log();

        StepVerifier.create(interval)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();



    }

}
