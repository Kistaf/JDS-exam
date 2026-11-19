package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CollectionUtils {
    public static <T> T randomSetElement(Set<T> set) {
        if (set == null || set.isEmpty()) return null;
        Random random = new Random();

        List<T> list = new ArrayList<>(set);
        return list.get(random.nextInt(list.size()));
    }

}
