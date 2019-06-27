package aziz.springreactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
public class FluxAndMonoBackpressureTest {

    @Test
    public void backPressureTest() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenRequest(1)
                .expectNext(3)
                .thenCancel()
                .verify();

    }

    @Test
    public void backPressureFiniteFlux() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(e -> log.info("Element : {}", e),
                ex -> log.error("Exception : {}", ex),
                () -> log.info("Done!"),
                subscrption -> subscrption.request(2));
    }

    @Test
    public void backPressureFiniteFluxCancel() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(e -> log.info("Element : {}", e),
                ex -> log.error("Exception : {}", ex),
                () -> log.info("Done!"),
                subscrption -> subscrption.cancel());
    }

    @Test
    public void customized_backPressure() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                log.info("Element : {}", value);
                if (value == 4) {
                    cancel();
                }
            }
        });

    }
}
