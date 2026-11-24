package utils;

import itumulator.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionUtils {
    public static <T> T randomSetElement(Set<T> set) {
        if (set == null || set.isEmpty()) return null;
        Random random = new Random();

        List<T> list = new ArrayList<>(set);
        return list.get(random.nextInt(list.size()));
    }

    public static <T> T randomListElement(List<T> list){
        if (list == null || list.isEmpty()) return null;

        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    public static <T> Set<T> allEntitiesOfType(World world, Class<T> type) {
        return world.getEntities().keySet().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toSet());
    }

}
