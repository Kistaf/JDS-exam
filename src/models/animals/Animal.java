package models.animals;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import models.structures.Carcass;
import utils.CollectionUtils;
import utils.LocationUtils;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Animal implements Actor {
    protected double age = 0;
    protected double energy;
    protected String imageKey;

    @Override
    public void act(World world) {
        if (!world.contains(this)) return;

        if (world.isDay()) this.performDayTasks(world);
        else this.performNightTasks(world);

        age += 0.25;
        this.decreaseEnergy(0.35);
        if (age >= this.maxAge() || energy <= 0) this.die(world);
    }

    protected abstract void performDayTasks(World world);

    protected abstract void performNightTasks(World world);

    protected abstract void performEat(World world);

    protected void performRandomMove(World world) {
        Location newLocation = LocationUtils.randomSurroundingFreeTile(world);
        if (newLocation == null) return;
        world.move(this, newLocation);
        world.setCurrentLocation(newLocation);
    }


    public void performMoveTowardsTarget(World world, Location targetLoc) {
        Location nextMove = LocationUtils.greedyBest(world, world.getCurrentLocation(), targetLoc);
        if (nextMove == null || !world.isTileEmpty(nextMove)) return;
        world.move(this, nextMove);
        world.setCurrentLocation(nextMove);
    }

    protected Location computeRandomTargetLocation(World world, List<Object> targets) {
        if (world.getEntities().isEmpty()) return null;
        Set<Location> targetTiles = world.getEntities().keySet().stream()
                .filter(entity -> targets.contains(entity.getClass()) && world.isOnTile(entity))
                .map(world::getLocation)
                .collect(Collectors.toSet());

        return CollectionUtils.randomSetElement(targetTiles);
    }

    protected void decreaseEnergy(double decreaseBy) {
        this.energy = Math.max(0, this.energy - decreaseBy);
    }

    protected void increaseEnergy(double increaseBy) {
        this.energy = Math.min(this.maxEnergy(), this.energy + increaseBy);
    }

    protected abstract double maxEnergy();

    protected abstract double maxAge();


    protected abstract double adultAge();

    protected void die(World world) {
        Location location = null;
        if ((world.isOnTile(this))){
            world.setCurrentLocation(world.getLocation(this));
            location = world.getCurrentLocation();
        }
        world.delete(this);
        spawnCarcass(world, location);
    }

    protected void spawnCarcass(World world, Location location){
        if(location == null) return;
        Carcass carcass = new Carcass(50);
        world.setTile(location, carcass);
    }

    protected Set<Animal> sameSpeciesNearby(World world, Location location) {
        Set<Animal> same = new HashSet<>();
        for (Animal animal : world.getAll(Animal.class, world.getSurroundingTiles(location))) {
            if (animal != this && animal.getClass() == getClass()) same.add(animal);
        }
        return same;
    }

    protected Set<Animal> sameSpeciesNearby(World world, Location location, int radius) {
        Set<Animal> same = new HashSet<>();
        for (Animal animal : world.getAll(Animal.class, world.getSurroundingTiles(location, radius))) {
            if (animal != this && animal.getClass() == getClass()) same.add(animal);
        }
        return same;
    }




}
