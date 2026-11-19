package models.plants;

import interfaces.Consumable;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import models.animals.Rabbit;
import utils.LocationUtils;

import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public class Grass implements Actor, Consumable, NonBlocking {
    private int energy = 10;

    public Grass(int initialEnergy) {
        this.energy = initialEnergy;
    }

    @Override
    public void act(World world) {
        attemptSpread(world);
    }

    private void attemptSpread(World world) {
        Random random = new Random();
        boolean success = random.nextInt(0, 16) == 4;
        if (success) spread(world);
    }

    public void spread(World world) {
        Location spreadLoc = LocationUtils.randomSurroundingFreeTile(world);
        if (spreadLoc == null) return;
        if (world.isDay() && !world.containsNonBlocking(spreadLoc)) {
            world.setTile(spreadLoc, new Grass(2));
        }
    }

    @Override
    public int getEnergyValue() {
        return this.energy;
    }

    @Override
    public int consume(World world) {
        int providedEnergy = this.energy;
        world.delete(this);
        return providedEnergy;
    }

    @Override
    public List<Object> consumableBy() {
        return List.of(Rabbit.class);
    }
}

