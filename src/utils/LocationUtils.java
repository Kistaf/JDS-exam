package utils;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.Random;
import java.util.Set;

public class LocationUtils {


    public static Location randomSurroundingFreeTile(World world) {
        Location currentLoc = world.getCurrentLocation();
        Set<Location> emptySurroundings = world.getEmptySurroundingTiles(currentLoc);
        return Helpers.randomSetElement(emptySurroundings);
    }

    public static Location randomFreeTile(World world) {
        int worldSize = world.getSize();
        Random random = new Random();

        int x = random.nextInt(worldSize);
        int y = random.nextInt(worldSize);

        Location location = new Location(x, y);
        while (!world.isTileEmpty(location) || world.containsNonBlocking(location)) {
            x = random.nextInt(worldSize);
            y = random.nextInt(worldSize);
            location = new Location(x, y);
        }
        return location;
    }
}
