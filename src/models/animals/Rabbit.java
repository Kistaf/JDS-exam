package models.animals;

import itumulator.world.Location;
import itumulator.world.World;
import models.structures.Borrow;

public class Rabbit extends Animal {
    private Borrow borrow;

    public Rabbit(int startEnergy) {
        super(startEnergy);
    }

    @Override
    protected void actDay(World world) {
        this.performRandomMove(world);
    }

    @Override
    protected void actNight(World world) {

    }

    private void sleep(World world) {
        Location sleepingSpot = this.locateSleepingSpot(world);
        if (sleepingSpot == null) return;
    }

    private void attemptBorrowDigging(World world, Location location) {}

    private void attemptReproduction(World world, Location location) {}

    private Location locateSleepingSpot(World world) {
        if (this.borrow == null) return null;
        return world.getLocation(this.borrow);
    }
}
