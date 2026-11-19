package models.animals;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import utils.LocationUtils;

import java.util.HashSet;
import java.util.Set;

public abstract class Animal implements Actor {
    protected double age = 0;
    protected double energy;

    private final double MAX_AGE = 30;
    private final double BASE_ENERGY = 15;
    private final double MIN_ENERGY = 0;

    protected Animal() {}

    protected Animal(int startEnergy) {
        this.energy = startEnergy;
    }

    @Override
    public void act(World world) {
        if (world.isNight()) this.performNightTasks(world);
        else if (world.isDay()) this.performDayTasks(world);

        age += 0.1;
        this.energy -= 0.1;
        if (!world.contains(this)) return;
        if (age >= this.maxAge() || energy <= 0) this.onDeath(world);
    }

    protected abstract void performDayTasks(World world);

    protected abstract void performNightTasks(World world);

    protected void performRandomMove(World world) {
        Location newLocation = LocationUtils.randomSurroundingFreeTile(world);
        world.move(this, newLocation);
        world.setCurrentLocation(newLocation);
    }

    protected void decreaseEnergy(double decreaseBy) {
        this.energy -= Math.min(0, this.energy - decreaseBy);
    }

    protected void increaseEnergy(double increaseBy) {
        this.energy -= Math.max(this.maxEnergy(), this.energy + increaseBy);
    }

    protected double maxEnergy() {
        return Math.min(this.MIN_ENERGY, this.BASE_ENERGY - this.age);
    }

    protected double maxAge() {
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
