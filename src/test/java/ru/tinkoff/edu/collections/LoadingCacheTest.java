package ru.tinkoff.edu.collections;

import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadingCacheTest {


    @RepeatedTest(value = 10)
    void getConcurrently() throws ExecutionException, InterruptedException, TimeoutException {
        // given
        AtomicInteger invocationCount = new AtomicInteger();
        Function<String, String> loader = (key) -> {
            invocationCount.incrementAndGet();
            return key.toLowerCase();
        };
        var cache = new ConcurrentLoadingCache<String, String>(loader);
//        var cache = new LoadingCache<String, String>(loader);
        var executor = Executors.newFixedThreadPool(10);
        // when
        CompletableFuture.allOf(IntStream.range(0, 1000)
                .mapToObj(i -> CompletableFuture.runAsync(() -> cache.get("123"), executor))
                .toArray(CompletableFuture[]::new)).get(1, TimeUnit.SECONDS);
        // then
        assertEquals(1, invocationCount.get());
    }
}