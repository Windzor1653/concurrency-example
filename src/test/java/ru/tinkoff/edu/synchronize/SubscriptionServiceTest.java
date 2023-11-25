package ru.tinkoff.edu.synchronize;

import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.synchronize.SubscriptionServiceOrderedSynchronizedBlock;

import java.util.UUID;
import java.util.stream.IntStream;

class SubscriptionServiceTest {


    @Test
    void unsubscribe() throws InterruptedException {
        // given
//        var userService = new SubscriptionServiceDeadlock();
//        var userService = new SubscriptionServiceSingleSynchronizedBlock();
        var userService = new SubscriptionServiceOrderedSynchronizedBlock();
        var subscriptionId = UUID.randomUUID();
        var users = IntStream.range(0, 10000).mapToObj(i -> UUID.randomUUID()).toList();
        userService.createSubscription(subscriptionId, 100l);
        users.forEach(userId -> userService.subscribeUser(subscriptionId, userId));
        // when
        var calculateFeeThread = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                users.forEach(userId -> userService.calculateTotalFee(userId));
            }
        }, "calculateFeeThread");
        calculateFeeThread.start();
        var removeSubscriptionThread = new Thread(() -> {
            userService.deleteSubscription(subscriptionId);
        }, "removeSubscriptionThread");
        removeSubscriptionThread.start();
        // then
        removeSubscriptionThread.join();
        calculateFeeThread.interrupt();
    }

}