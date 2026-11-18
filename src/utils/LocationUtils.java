package utils;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.Random;
import java.util.Set;

public class LocationUtils {


    public static Location randomSurroundingFreeTile(World world) {
        Random random = new Random();
        Location currentLoc = world.getCurrentLocation();
        Set<Location> emptySurroundings = world.getEmptySurroundingTiles(currentLoc);
        if (emptySurroundings.isEmpty()) return null;

        int nextLocIdx = random.nextInt(emptySurroundings.size());
        return emptySurroundings.stream()
                .skip(nextLocIdx)
                .findFirst()
                .orElse(null);
    }
}
