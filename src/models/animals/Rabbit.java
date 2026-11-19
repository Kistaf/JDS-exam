package models.animals;

import interfaces.Consumable;
import itumulator.world.Location;
import itumulator.world.World;
import models.plants.Grass;
import models.structures.Burrow;
import utils.CollectionUtils;
import utils.LocationUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Rabbit extends Animal {
    private Burrow burrow;
    private Location sleepingSpot;
    private final double MIN_ENERGY = 10;
    private final double BASE_ENERGY = 20;
    private final double MAX_AGE = 50;
    private final double REPRODUCTION_ENERGY_MIN = 10;

    public Rabbit() {
        this.energy = BASE_ENERGY;
    }

    @Override
    public void act(World world) {
        if (!world.contains(this)) return;
        if (!world.isOnTile(this) && this.sleepingSpot != null) {
            if (world.isDay()) this.attemptWakeUp(world);
            return;
        }
        super.act(world);
    }

    @Override
    protected void performDayTasks(World world) {
        Location currentLoc = world.getCurrentLocation();

        Random random = new Random();
        int roll = random.nextInt(0, 10);

        if (roll % 2 == 0) {
            Location target = this.computeRandomTargetLocation(world, List.of(Rabbit.class));
            if (target == null) return;
            Location nextMove = LocationUtils.greedyBest(world, currentLoc, target);
            if (nextMove == null) return;
            this.performMoveTowardsTarget(world, nextMove);
        } else if (roll % 3 == 0) {
            Location grassTarget = this.computeRandomTargetLocation(world, List.of(Grass.class));
            if (grassTarget == null) this.performRandomMove(world);
            else this.performMoveTowardsTarget(world, grassTarget);
        } else this.performRandomMove(world);

        this.attemptReproduction(world, currentLoc);
        this.performEat(world);
    }

    @Override
    protected void performNightTasks(World world) {
        if (!world.isOnTile(this)) return;
        Location currentLoc = world.getCurrentLocation();

        // borrow another rabbits burrow for the night
        if (world.containsNonBlocking(currentLoc) && world.getNonBlocking(currentLoc) instanceof Burrow) {
            this.performSleep(world, currentLoc);
            return;
        }

        // walk towards own burrow
        Random random = new Random();
        boolean success = random.nextInt(0, 2) == 1;
        if (this.burrow != null && success) {
            Location burrowLocation = this.locateBurrow(world);
            Location bestNextTile = LocationUtils.greedyBest(world, currentLoc, burrowLocation);
            if (bestNextTile == null || !world.isTileEmpty(bestNextTile)) return;
            world.move(this, bestNextTile);
            world.setCurrentLocation(bestNextTile);
        } else {
            this.performRandomMove(world);
        }

        if (this.burrow == null) this.attemptBurrowDigging(world);

    }

    @Override
    public void performEat(World world) {
        if (this.energy >= this.maxEnergy()) return;

        Location currentLoc = world.getCurrentLocation();
        Object nonBlockingObj = world.getNonBlocking(currentLoc);
        if (nonBlockingObj == null) return;
        if (nonBlockingObj instanceof Consumable food) {
            int energyGain = food.getEnergyValue();
            if (!food.consumableBy().contains(Rabbit.class)) return;
            food.consume(world);
            this.increaseEnergy(energyGain);
        }
    }

    @Override
    protected double maxEnergy() {
        return Math.max(this.MIN_ENERGY, this.BASE_ENERGY - this.age);
    }

    @Override
    protected double maxAge() {
        return this.MAX_AGE;
    }


    private void attemptBurrowDigging(World world) {
        Location currentLoc = world.getCurrentLocation();
        if (world.containsNonBlocking(currentLoc)) return;
        Random random = new Random();
        boolean success = random.nextInt(1, 12) == 5;
        if (!success) return;
        this.performBurrowDigging(world, currentLoc);
    }

    public void performBurrowDigging(World world, Location currentLoc) {
        Burrow burrow = new Burrow();
        world.setTile(currentLoc, burrow);
        this.burrow = burrow;
        this.performSleep(world, currentLoc);
    }

    public Location locateBurrow(World world) {
        if (this.burrow == null) return null;
        return world.getLocation(this.burrow);
    }

    public void setSleepingSpot(Location location) {
        this.sleepingSpot = location;
    }


    private void attemptWakeUp(World world) {
        Location sleepingSpot = this.sleepingSpot;
        // priority:
        // 1: burrow where they went to sleep
        // 2: Any other burrow on the map (tunnel)
        // 3: Random tile as last resort.
        // 4: Wait for next daily cycle.

        // handle if burrow spot exists & currently has no blocking entity on it
        if (world.isTileEmpty(sleepingSpot)) {
            this.performWakeUp(world, sleepingSpot);
            return;
        }

        Set<Location> mapBurrows = world.getEntities().keySet().stream()
                .filter(entity -> entity.getClass() == Burrow.class)
                .map(world::getLocation)
                .collect(Collectors.toSet());

        if (!mapBurrows.isEmpty()) {
            // spawn at any other burrow on the spawn
            Set<Location> triedLocs = new HashSet<>();
            while (triedLocs.size() != mapBurrows.size()) {
                mapBurrows.removeIf(triedLocs::contains);
                Location randomLoc = CollectionUtils.randomSetElement(mapBurrows);
                if (randomLoc == null) continue;
                triedLocs.add(randomLoc);

                if (world.isTileEmpty(randomLoc)) {
                    this.performWakeUp(world, randomLoc);
                    return;
                }
            }
        }

        // last resort; spawn at a random free tile
        Location lastResortLocation = LocationUtils.randomFreeTile(world);
        // map is full; rabbit must wait till next daily cycle
        if (lastResortLocation == null) return;
        this.performWakeUp(world, lastResortLocation);
    }

    public void attemptReproduction(World world, Location location) {
        Random random = new Random();
        if (energy < 0.5 * this.maxEnergy() || random.nextDouble() >= 0.10 || this.energy < this.REPRODUCTION_ENERGY_MIN) return;

        Set<Animal> closeByRabbits = this.sameSpeciesNearby(world, location);
        Rabbit mate = (Rabbit) CollectionUtils.randomSetElement(closeByRabbits);
        if (mate == null || mate == this || mate.energy < this.REPRODUCTION_ENERGY_MIN) return;

        Rabbit baby = new Rabbit();
        Location babyLoc = LocationUtils.randomFreeTile(world);
        world.setTile(babyLoc, baby);

        this.decreaseEnergy(5);
        mate.decreaseEnergy(5);
    }


    public void performSleep(World world, Location sleepingSpot) {
        this.setSleepingSpot(sleepingSpot);
        world.remove(this);
    }

    public void performWakeUp(World world, Location location) {
        world.setCurrentLocation(location);
        world.setTile(location, this);
        this.increaseEnergy(5);
        this.setSleepingSpot(null);
    }
}
