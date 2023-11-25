package ru.tinkoff.edu.non_blocking;

public class RateLimiter {

    private long tokens;

    public RateLimiter(long initialTokens) {
        this.tokens = initialTokens;
    }

    public boolean doWork() {
        if(tokens-- > 0) {
            return true;
        } else {
            tokens++;
            return false;
        }
    }

    public void addTokens(int tokens) {
        this.tokens += tokens;
    }

}
