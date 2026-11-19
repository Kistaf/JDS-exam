package models.plants;

import interfaces.Consumable;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.Random;

public class Grass implements Actor, Consumable, NonBlocking {
    private int energy = 10;

    public Grass() {
    }

    public Grass(int initialEnergy) {
        this.energy = initialEnergy;
    }

    @Override
    public void act(World world) {

    }

    private void spread(World world) {}

    @Override
    public int getEnergyValue() {
        return this.energy;
    }

}
