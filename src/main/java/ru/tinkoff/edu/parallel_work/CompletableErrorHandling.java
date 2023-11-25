package ru.tinkoff.edu.parallel_work;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class CompletableErrorHandling {

    private final ExecutorService executor;

    public CompletableErrorHandling(ExecutorService executor) {
        this.executor = executor;
    }
    
    public void doWorkHandle(Runnable action) {
        CompletableFuture.runAsync(action, executor)
                .handle((x, error) -> {
                    if(error != null) {
                        throw new RuntimeException(error);
                    } else {
                        return x;
                    }
                });
    }

    public void doWorkWhenComplete(Runnable action) {
        CompletableFuture.runAsync(action, executor)
                .whenComplete((x, error) -> {
                    if(error != null) {
                        throw new RuntimeException(error);
                    }
                });
    }

    public void doWorkExceptionally(Runnable action) {
        CompletableFuture.runAsync(action, executor)
                .exceptionally(e -> {
                    if(e instanceof IllegalStateException) {
                        return null;
                    } else {
                        throw new RuntimeException("Unexpected error", e);
                    }
                });
    }

}
