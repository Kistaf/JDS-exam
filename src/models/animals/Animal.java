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

    protected Animal() {}

    @Override
    public void act(World world) {
        if (world.isDay()) this.performDayTasks(world);
        else this.performNightTasks(world);

        age += 0.25;
        this.decreaseEnergy(0.25);
        if (!world.contains(this)) return;
        if (age >= this.maxAge() || energy <= 0) this.onDeath(world);
    }

    protected abstract void performDayTasks(World world);

    protected abstract void performNightTasks(World world);

    protected abstract void performEat(World world);

    protected void performRandomMove(World world) {
        Location newLocation = LocationUtils.randomSurroundingFreeTile(world);
        world.move(this, newLocation);
        world.setCurrentLocation(newLocation);
    }

    protected void decreaseEnergy(double decreaseBy) {
        this.energy -= Math.max(0, this.energy - decreaseBy);
    }

    protected void increaseEnergy(double increaseBy) {
        this.energy += Math.min(this.maxEnergy(), this.energy + increaseBy);
    }

    protected abstract double maxEnergy();

    protected abstract double maxAge();


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
