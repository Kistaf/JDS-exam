package models.plants;

import interfaces.Consumable;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import models.animals.Rabbit;
import models.plants.berries.BerryBush;
import utils.LocationUtils;
import utils.NumberUtils;

import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public class Grass implements Actor, Consumable, NonBlocking {
    private double energy;

    public Grass(int initialEnergy) {
        this.energy = initialEnergy;
    }

    @Override
    public void act(World world) {
        if (world.isDay()) {
            attemptSpread(world);
        }
    }

    private void attemptSpread(World world) {
        // save some memory by limiting how much grass the world can hold.
        double currentGrassAmount = world.getEntities().keySet().stream()
                .filter(e -> e.getClass() == Grass.class)
                .count();

        int worldSize = world.getSize();
        double grassTakingUpPercentage = (currentGrassAmount / (worldSize * worldSize));
        if (grassTakingUpPercentage >= 0.25) return;
        boolean success = NumberUtils.chance(5);
        if (success) spread(world);
    }

    public void spread(World world) {
        Location spreadLoc = LocationUtils.randomSurroundingFreeTile(world);
        if (spreadLoc == null) return;
        if (world.isTileEmpty(spreadLoc) && !world.containsNonBlocking(spreadLoc)) {
            world.setTile(spreadLoc, new Grass(2));
        }
    }

    @Override
    public double getEnergyValue() {
        return this.energy;
    }

    @Override
    public void consume(World world) {
        world.delete(this);
    }

    @Override
    public List<Object> consumableBy() {
        return List.of(Rabbit.class);
    }
}

