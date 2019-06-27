package aziz.springreactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class FluxAndMonoMethodTests {

    List<String> names=Arrays.asList("adam","susan");

    @Test
    public void fluxIterable()
    {
        Flux<String> stringFlux = Flux.fromIterable(names).log();


    }


}
