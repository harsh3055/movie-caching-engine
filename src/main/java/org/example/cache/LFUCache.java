package org.example.cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LFUCache<K, V> {
    private final int capacity;
    private int minFreq;
    private final Map<K, V> cache;
    private final Map<K, Integer> keyFreq;
    private final Map<Integer, LinkedHashSet<K>> freqKeys;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.cache = new HashMap<>();
        this.keyFreq = new HashMap<>();
        this.freqKeys = new HashMap<>();
    }

    public V get(K key) {
        if (!cache.containsKey(key)) return null;
        updateFrequency(key);
        return cache.get(key);
    }

    public void put(K key, V value) {
        if (capacity <= 0) return;

        if (cache.containsKey(key)) {
            cache.put(key, value);
            updateFrequency(key);
            return;
        }

        if (cache.size() >= capacity) {
            K evictKey = freqKeys.get(minFreq).iterator().next();
            freqKeys.get(minFreq).remove(evictKey);
            cache.remove(evictKey);
            keyFreq.remove(evictKey);
        }

        cache.put(key, value);
        keyFreq.put(key, 1);
        freqKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFreq = 1;
    }

    private void updateFrequency(K key) {
        int freq = keyFreq.get(key);
        keyFreq.put(key, freq + 1);
        freqKeys.get(freq).remove(key);

        if (freq == minFreq && freqKeys.get(freq).isEmpty()) {
            minFreq++;
        }
        freqKeys.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(key);
    }

    public void clear() {
        cache.clear();
        keyFreq.clear();
        freqKeys.clear();
        minFreq = 0;
    }
}