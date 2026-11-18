package models.animals;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import utils.LocationUtils;

import java.util.HashSet;
import java.util.Set;

public abstract class Animal implements Actor {
    protected int age = 0;
    protected int energy;
    protected boolean sleeping = false;

    private final int MIN_ENERGY = 1;
    private final int MAX_AGE = 50;

    protected Animal(int startEnergy) {
        this.energy = startEnergy;
    }

    @Override
    public void act(World world) {
        age++;
        if (world.isNight()) this.actNight(world);
        else this.actDay(world);

        energy -= 1;
        if (!world.contains(this)) return;
        if (age > this.maxAge() || energy <= 0) this.onDeath(world);
    }

    protected abstract void actDay(World world);

    protected abstract void actNight(World world);

    protected void performRandomMove(World world) {
        Location newLocation = LocationUtils.randomSurroundingFreeTile(world);
        world.move(this, newLocation);
        world.setCurrentLocation(newLocation);
    }

    protected int maxEnergy() {
        return Math.min(this.MIN_ENERGY, this.energy - this.age);
    }

    protected int maxAge() {
        return this.MAX_AGE;
    }

    protected void onDeath(World world) {
        world.delete(this);
    }

    protected Set<Animal> sameSpeciesNearby(World world, Location location) {
        Set<Animal> animals = new HashSet<>();
        for (Animal animal : world.getAll(Animal.class, world.getSurroundingTiles())) {
            if (animal.getClass() == this.getClass() && animal != this) {
                animals.add(animal);
            }
        }
        return animals;
    }

}
