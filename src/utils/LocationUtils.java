package utils;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationUtils {


    public static Location randomSurroundingFreeTile(World world) {
        Location currentLoc = world.getCurrentLocation();
        Set<Location> emptySurroundings = world.getEmptySurroundingTiles(currentLoc);
        return CollectionUtils.randomSetElement(emptySurroundings);
    }

    public static Location randomSurroundingFreeTile(World world, Location location) {
        world.setCurrentLocation(location);
        Set<Location> emptySurroundings = world.getEmptySurroundingTiles(world.getCurrentLocation());
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

    public static Location slowRandomFreeTile(World world){
        List<Location> possibleLocations = new ArrayList<>();
        int worldSize = world.getSize();

        for(int x = 0; x < worldSize; x++){
            for(int y = 0; y < worldSize; y++){
                possibleLocations.add(new Location(x, y));
            }
        }

        for(Location location : world.getEntities().values()){
            possibleLocations.remove(location);
        }

        return  CollectionUtils.randomListElement(possibleLocations);
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
