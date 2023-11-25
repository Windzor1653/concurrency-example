package ru.tinkoff.edu.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LoadingCache<K, V> {

    private final Map<K, V> cache = new HashMap<>();
    private final Function<K, V> loader;

    public LoadingCache(Function<K, V> loader) {
        this.loader = loader;
    }

    public V get(K key) {
        return cache.computeIfAbsent(key, loader::apply);
    }
}
