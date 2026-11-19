package models.animals;

import interfaces.Consumable;
import itumulator.world.Location;
import itumulator.world.World;
import models.structures.Borrow;
import utils.Helpers;
import utils.LocationUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Rabbit extends Animal {
    private Borrow borrow;
    private Location sleepingSpot;

    private final int REPRODUCTION_ENERGY_MIN = 30;

    public Rabbit() {
    }

    public Rabbit(int startEnergy) {
        super(startEnergy);
    }

    @Override
    protected void performDayTasks(World world) {
        if (!world.isOnTile(this)) {
            this.wakeUp(world);
            return;
        }

        this.performRandomMove(world);
        this.attemptEating(world);
        this.attemptReproduction(world, world.getCurrentLocation());
    }

    @Override
    protected void performNightTasks(World world) {
        Location currentLoc = world.getCurrentLocation();
        if (!world.isOnTile(this)) return;
        if (world.containsNonBlocking(currentLoc) && world.getTile(currentLoc) instanceof Borrow) {
            this.performSleep(world, currentLoc);
            return;
        }

        if (this.borrow == null && !world.containsNonBlocking(currentLoc)) {
            this.attemptBorrowDigging(world);
            return;
        }

        Location borrowLocation = this.locateBorrow(world);
        if (borrowLocation == null) this.performRandomMove(world);
        // calculate the next tile in the straightest path to the borrow
        // move to that tile
        //
    }

    private void attemptEating(World world) {
        if (this.energy == this.maxEnergy()) return;

        Location currentLoc = world.getCurrentLocation();
        Object nonBlockingObj = world.getNonBlocking(currentLoc);
        if (nonBlockingObj == null) return;
        if (nonBlockingObj instanceof Consumable) {
            // TODO: change out for consume method on interface
            this.increaseEnergy(((Consumable) nonBlockingObj).getEnergyValue());
            world.delete(nonBlockingObj);
        }

    }


    private void attemptBorrowDigging(World world) {
        Location currentLoc = world.getCurrentLocation();
        Random random = new Random();
        boolean success = random.nextInt(1, 10) == 5;
        if (!success) return;

        Borrow borrow = new Borrow();
        world.setTile(currentLoc, borrow);

        this.borrow = borrow;
        this.performSleep(world, currentLoc);
    }

    private void attemptReproduction(World world, Location location) {
        if (this.energy < REPRODUCTION_ENERGY_MIN) return;
        Set<Animal> closeByRabbits = this.sameSpeciesNearby(world, location);
        Rabbit mate = (Rabbit) Helpers.randomSetElement(closeByRabbits);
        if (mate == null || mate.energy < REPRODUCTION_ENERGY_MIN) return;

        Rabbit baby = new Rabbit(10);
        Location babyLoc = LocationUtils.randomSurroundingFreeTile(world);
        world.setTile(babyLoc, baby);

        this.decreaseEnergy(10);
        mate.decreaseEnergy(10);
    }

    private Location locateBorrow(World world) {
        if (this.borrow == null) return null;
        return world.getLocation(this.borrow);
    }


    private void wakeUp(World world) {
        Location sleepingSpot = this.sleepingSpot;
        // priority:
        // 1: Borrow where they went to sleep
        // 2: Any other borrow on the map (tunnel)
        // 3: Random tile as last resort.
        // 4: Wait for next daily cycle.

        // handle if borrow spot exists & currently has no blocking entity on it
        if (sleepingSpot != null && world.isTileEmpty(sleepingSpot)) {
            this.performWakeUp(world, sleepingSpot);
            return;
        }

        Set<Location> mapBorrows = world.getEntities().entrySet().stream()
                .filter(entity -> entity.getKey().getClass() == Borrow.class)
                .map(world::getLocation)
                .collect(Collectors.toSet());

        if (!mapBorrows.isEmpty()) {
            // spawn at any other borrow on the spawn
            Set<Location> triedLocs = new HashSet<>();
            while (triedLocs.size() != mapBorrows.size()) {
                mapBorrows.removeIf(triedLocs::contains);
                Location randomLoc = Helpers.randomSetElement(mapBorrows);
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

    private void performSleep(World world, Location sleepingSpot) {
        this.sleepingSpot = sleepingSpot;
        world.remove(this);
    }

    private void performWakeUp(World world, Location location) {
        world.setCurrentLocation(location);
        world.setTile(location, this);
        this.increaseEnergy(5);
        this.sleepingSpot = null;
    }
}
