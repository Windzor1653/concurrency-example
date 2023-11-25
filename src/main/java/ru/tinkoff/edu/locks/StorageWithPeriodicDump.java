package ru.tinkoff.edu.locks;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class StorageWithPeriodicDump {

    private final Map<String, String> storage = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock rLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock wLock = rwLock.writeLock();
    private final Duration dumpPeriod = Duration.of(10, ChronoUnit.MILLIS);
    private volatile Instant previousDumpMoment = Instant.now();
    private final Consumer<Map<String, String>> dumpStorage;

    public StorageWithPeriodicDump(Consumer<Map<String, String>> dumpStorage) {
        this.dumpStorage = dumpStorage;
    }

    public void write(String key, String value) {
        rLock.lock();
        try {
            storage.put(key, value);
        } finally {
            rLock.unlock();
        }
        tryDump();
    }

    public String read(String key) {
        rLock.lock();
        try {
            return storage.get(key);
        } finally {
            rLock.unlock();
        }
    }

    private void tryDump() {
        if(dumpRequired()) {
            wLock.lock();
            try {
                if(dumpRequired()) {
                    dumpStorage.accept(storage);
                    previousDumpMoment = Instant.now();
                }
            } finally {
                wLock.unlock();
            }
        }
    }

    private boolean dumpRequired() {
        return Duration.between(previousDumpMoment, Instant.now()).compareTo(dumpPeriod) > 0;
    }
}
