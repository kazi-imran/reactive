package aziz.springreactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.chrono.ThaiBuddhistEra;
import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

@Slf4j
public class FluxandMonoTest {

    @Test
    public void fluxTest() {

        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                //    .concatWith(Flux.error(new RuntimeException("Exception will occur!")))
                .concatWith(Flux.just("Will not print"))
                .log();

        stringFlux.subscribe(s -> log.info(s), (e) -> log.error(e.getMessage()), () -> log.info("Completed"));
    }

    @Test
    public void fluxTestElementsNoError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring").log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();

    }

    @Test
    public void fluxTestElementsWitErrors() {

        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception will occur!")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void transFromFlatMap()
    {
        Flux<String> log = Flux.fromIterable(Arrays.asList("A", "A", "A", "A"))
                .flatMap(s -> returnFlux(s))
                .log();
        StepVerifier.create(log).expectNextCount(4).verifyComplete();

    }

    @Test
    public void transFromFlatMapParallel()
    {
        Flux<String> log = Flux.fromIterable(Arrays.asList("A", "B", "C", "D","E","F","G","H"))
                .window(2)
                .flatMap((s) -> s.map(this::returnList).subscribeOn(parallel()))
                .flatMap(s->Flux.fromIterable(s))
                .log();
        StepVerifier.create(log).expectNextCount(8).verifyComplete();

    }

    @Test
    public void transFromFlatMapParallelMaintainOrder()
    {
        Flux<String> log = Flux.fromIterable(Arrays.asList("A", "B", "C", "D","E","F","G","H"))
                .window(2)
                .flatMapSequential((s) -> s.map(this::returnList).subscribeOn(parallel()))
                .flatMap(s->Flux.fromIterable(s))
                .log();
        StepVerifier.create(log).expectNextCount(8).verifyComplete();

    }

    private List<String> returnList(String s)
    {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s);

    }




    private Flux<String> returnFlux(String s)
    {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Flux.just(s);

    }


}
