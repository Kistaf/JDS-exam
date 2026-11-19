package utils;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class CollectionUtils {
    public static <T> T randomSetElement(Set<T> set) {
        if (set == null || set.isEmpty()) return null;
        Random random = new Random();
        int nextLocIdx = random.nextInt(set.size());
        return set.stream()
                .skip(nextLocIdx)
                .findFirst()
                .orElse(null);
    }

    public static <T> T randomListElement(List<T> elements) {
        if (elements == null || elements.isEmpty()) return null;
        Random random = new Random();
        int nextLocIdx = random.nextInt(elements.size());
        return elements.stream()
                .skip(nextLocIdx)
                .findFirst()
                .orElse(null);
    }
}
