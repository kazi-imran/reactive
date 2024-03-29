package aziz.springreactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class VirtualTimeTest {

    @Test
    public void testingWithoutVirtualTime()
    {
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1)).take(3).log();

        StepVerifier.create(longFlux)
                    .expectSubscription()
                    .expectNext(0L,1L,2L)
                    .verifyComplete();
    }

    @Test
    public void testingWithVirtualTime()
    {
        VirtualTimeScheduler.getOrSet();
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1)).take(3).log();

        StepVerifier.withVirtualTime(()->longFlux)
                    .expectSubscription()
                    .thenAwait(Duration.ofSeconds(1L))
                    .expectNext(0L,1L,2L)
                    .verifyComplete()
        ;


    }
}
