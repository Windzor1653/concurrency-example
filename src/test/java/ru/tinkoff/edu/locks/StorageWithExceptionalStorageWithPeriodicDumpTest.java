package ru.tinkoff.edu.locks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StorageWithExceptionalStorageWithPeriodicDumpTest {

    @Test
    void dumpWithImproperLocks() throws InterruptedException {
        // given
        var dump = new ExceptionalStorageWithPeriodicDump((map) -> {
            throw new RuntimeException();
        });
        // when
        TimeUnit.MILLISECONDS.sleep(100);
        assertThrows(RuntimeException.class, () -> dump.write("key", "value"));
        // then
        // deadlock here
        var anotherThread = new Thread(() -> {
            dump.read("key");
        });
        anotherThread.start();
        anotherThread.join();
    }

    @Test
    void dumpWithProperLocks() throws InterruptedException {
        // given
        var dump = new StorageWithPeriodicDump((map) -> {
            throw new RuntimeException();
        });
        // when
        TimeUnit.MILLISECONDS.sleep(100);
        assertThrows(RuntimeException.class, () -> dump.write("key", "value"));
        // then
        var anotherThread = new Thread(() -> {
            dump.read("key");
        });
        anotherThread.start();
        anotherThread.join();
    }

}