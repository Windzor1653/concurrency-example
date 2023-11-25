package ru.tinkoff.edu.synchronize;

import java.util.*;

public class SubscriptionServiceSingleSynchronizedBlock implements SubscriptionService  {
    private final Map<UUID, List<UUID>> userSubscriptions = new HashMap<>();
    private final Map<UUID, List<UUID>> subscriptionUsers = new HashMap<>();
    private final List<Subscription> subscriptionList = new ArrayList<>();

    public void createSubscription(UUID subscriptionId, long fee) {
        synchronized (this) {
            subscriptionList.add(new Subscription(subscriptionId, fee));
            subscriptionUsers.put(subscriptionId, new ArrayList<>());
        }
    }

    public void subscribeUser(UUID subscriptionId, UUID userId) {
        synchronized (this) {
            Optional.ofNullable(subscriptionUsers.get(subscriptionId))
                    .ifPresent(it -> it.add(userId));
        }
    }

    public void deleteSubscription(UUID subscriptionId) {
        synchronized (this) {
            subscriptionList.removeIf(it -> it.uuid.equals(subscriptionId));

            for (UUID userId : Optional.ofNullable(subscriptionUsers.get(subscriptionId)).orElseGet(Collections::emptyList)) {
                Optional.ofNullable(userSubscriptions.get(userId))
                        .ifPresent(it -> it.remove(subscriptionId));
            }

            subscriptionUsers.remove(subscriptionId);
        }
    }

    public long calculateTotalFee(UUID userId) {
        synchronized (this) {
            List<UUID> subscriptionIds = Optional.ofNullable(userSubscriptions.get(userId)).orElseGet(Collections::emptyList);
            return subscriptionList.stream().filter(sub -> subscriptionIds.contains(sub.uuid))
                    .mapToLong(it -> it.fee)
                    .sum();
        }
    }

    record Subscription(UUID uuid, long fee) {
    }
}
