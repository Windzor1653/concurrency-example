package ru.tinkoff.edu.parallel_work;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class ExecutorErrorHandling {

    private final ExecutorService executor;

    public ExecutorErrorHandling(ExecutorService executor) {
        this.executor = executor;
    }

    public void doWork(Runnable action) {
        try {
            executor.submit(action).get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if(cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw new RuntimeException("Unexpected error type", e.getCause());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
