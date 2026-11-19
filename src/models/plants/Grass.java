package models.plants;

import interfaces.Consumable;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;



import java.util.Random;
import java.util.random.RandomGenerator;

public class Grass implements Actor, Consumable, NonBlocking {
    private int energy = 10;
    private Location location;

    public Grass(int initialEnergy, Location location) {
        this.energy = initialEnergy;
        this.location = location;
    }

    @Override
    public void act(World world) {

    }

    private void spread(World world) {

        Random random = new Random();
        int x = random.nextInt(world.getSize());
        int y = random.nextInt(world.getSize());

        Location location = new Location(x, y);
        while (!world.isTileEmpty(location) || world.containsNonBlocking(location)) {
            x = random.nextInt(world.getSize());
            y = random.nextInt(world.getSize());
            location = new Location(x, y);
        }
        if (world.isDay()) {
            if (world.isTileEmpty(location)) {
                world.setTile(location, new Grass(10, location));
            }
        }
    }
        @Override
        public int getEnergyValue() {
            return this.energy;
        }
    }

