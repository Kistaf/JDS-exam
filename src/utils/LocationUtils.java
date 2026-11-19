package utils;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.Random;
import java.util.Set;

public class LocationUtils {


    public static Location randomSurroundingFreeTile(World world) {
        Location currentLoc = world.getCurrentLocation();
        Set<Location> emptySurroundings = world.getEmptySurroundingTiles(currentLoc);
        return CollectionUtils.randomSetElement(emptySurroundings);
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

    // Gathered from conversation with AI on how best to approach this kind of problem.
    public static Location greedyBestNeighbour(World world, Location from, Location to) {
        Location best = null;
        double bestDist = Double.MAX_VALUE;

        for (Location loc : world.getEmptySurroundingTiles(from)) {
            double dist = dist2(loc, to);
            if (dist < bestDist) {
                bestDist = dist;
                best = loc;
            }
        }
        return best;
    }

    private static double dist2(Location from, Location to) {
        int dx = from.getX() - to.getX();
        int dy = from.getY() - to.getY();
        return dx * dx + dy * dy;
    }
}
