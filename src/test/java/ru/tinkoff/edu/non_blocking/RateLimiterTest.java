package ru.tinkoff.edu.non_blocking;

import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateLimiterTest {

    @RepeatedTest(100)
    void doWorkInParallel() throws InterruptedException {
        // given
        int threadCount = 10;
        var executor = Executors.newFixedThreadPool(threadCount);
        int limitPerSecond = 10000;
        RateLimiter rateLimiter = new RateLimiter(limitPerSecond);
//        AtomicRateLimiter rateLimiter = new AtomicRateLimiter(limitPerSecond);
        // when
        var tasks = IntStream.range(0, 12000)
                .mapToObj(i -> (Callable<Object>) () -> rateLimiter.doWork())
                .collect(Collectors.toList());
        var succeededThreads = executor.invokeAll(tasks)
                .stream()
                .map(objectFuture -> {
                    try {
                        return objectFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Predicate.isEqual(true))
                .count();
        // then
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        assertEquals(limitPerSecond, succeededThreads);
    }
}