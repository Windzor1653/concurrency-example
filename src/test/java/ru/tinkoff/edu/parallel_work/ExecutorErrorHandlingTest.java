package ru.tinkoff.edu.parallel_work;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExecutorErrorHandlingTest {

    @Test
    void doWork() {
        // given

        // when
        var wrapperError = assertThrows(RuntimeException.class, () -> {
            new ExecutorErrorHandling(Executors.newFixedThreadPool(1)).doWork(() -> {
                throw new RuntimeException("test message");
            });
        });
        // then
        assertEquals("test message", wrapperError.getMessage());
    }
}