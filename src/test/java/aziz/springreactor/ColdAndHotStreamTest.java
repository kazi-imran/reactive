package aziz.springreactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class ColdAndHotStreamTest {


    @Test
    public void coldPublisherTest() throws InterruptedException {

        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                                       .delayElements(Duration.ofSeconds(1)) ;

        stringFlux.subscribe(s->log.info("Subsriber A : {}",s )); //emits the value from beginning

        Thread.sleep(2000);

        stringFlux.subscribe(s->log.info("Subsriber B : {}",s )); //emits the value from beginning

        Thread.sleep(2000);
    }

    @Test
    public void hotPublisherTest() throws InterruptedException {

        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1)) ;

        ConnectableFlux<String> connectableFlux = stringFlux.publish();

        connectableFlux.connect();

        connectableFlux.subscribe(s->log.info("Subsriber A : {}",s ));

        Thread.sleep(3000);

        connectableFlux.subscribe(s->log.info("Subsriber B : {}",s )); //doesn't emit the value from beginning

        Thread.sleep(4000);

    }
}
