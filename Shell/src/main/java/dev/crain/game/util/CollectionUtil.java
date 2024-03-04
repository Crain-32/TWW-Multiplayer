package dev.crain.game.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtil {

    /**
     * @return Returns an immutable copy of the original map, with keys as values, and values as keys.
     */
    public static <V, K> Map<V, K> swapMap(Map<K, V> originalMap) {
        Map<V, K> swappedMap = new HashMap<>();
        for (var entry : originalMap.entrySet()) {
            if (!swappedMap.containsKey(entry.getValue())) {
                swappedMap.put(entry.getValue(), entry.getKey());
            }
        }
        return Collections.unmodifiableMap(swappedMap);
    }

    public static <K, V, S> S extractFromMap(Map<K, V> map, K key, Function<V, S> extractor) {
        if (map == null || map.isEmpty()) return null;
        var value = map.get(key);
        return value == null ? null : extractor.apply(value);
    }

    public static <E> E findInList(List<E> list, Predicate<E> predicate) {
        if (list == null || list.isEmpty()) return null;
        return list.stream().filter(predicate).findFirst().orElse(null);
    }

    public static <E, V> V extractInList(List<E> list, Predicate<E> predicate, Function<E, V> extractor) {
        var value = findInList(list, predicate);
        return value == null ? null : extractor.apply(value);
    }
}
