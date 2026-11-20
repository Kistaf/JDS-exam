package utils;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationUtils {


    public static Location randomSurroundingFreeTile(World world) {
        Location currentLoc = world.getCurrentLocation();
        Set<Location> emptySurroundings = world.getEmptySurroundingTiles(currentLoc);
        return CollectionUtils.randomSetElement(emptySurroundings);
    }

    public static Set<Location> completeFreeSurroundingTiles(World world) {
        Location currentLoc = world.getCurrentLocation();
        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLoc);
        return surroundingTiles.stream()
                .filter(tile -> world.isTileEmpty(tile) && !world.containsNonBlocking(tile))
                .collect(Collectors.toSet());
    }

    public static Location randomCompletelyFreeTile(World world) {
        int size = world.getSize();
        Random random = new Random();

        // random coordinates as a smaller scan
        for (int i = 0; i < size * size * 10; i++) {
            Location loc = new Location(random.nextInt(size), random.nextInt(size));
            if (world.isTileEmpty(loc) && !world.containsNonBlocking(loc)) return loc;
        }

        // full scan
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Location loc = new Location(x, y);
                if (world.isTileEmpty(loc) && !world.containsNonBlocking(loc)) return loc;
            }
        }
        return null;
    }

    // Gathered from conversation with AI on how best to approach this kind of problem.
    public static Location greedyBest(World world, Location from, Location to) {
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
