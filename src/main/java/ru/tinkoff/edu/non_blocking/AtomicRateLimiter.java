package ru.tinkoff.edu.non_blocking;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicRateLimiter {

    private AtomicInteger tokens;

    public AtomicRateLimiter(int initialTokens) {
        this.tokens = new AtomicInteger(initialTokens);
    }

    public boolean doWork() {
        if(tokens.decrementAndGet() >= 0) {
            return true;
        } else {
            tokens.incrementAndGet();
            return false;
        }
    }

    public void addTokens(int tokens) {
        this.tokens.addAndGet(tokens);
    }

}
