package models.plants;

import interfaces.Consumable;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.Random;

public class Grass extends Plant implements Consumable {

    public Grass(Location location) {
        super(location);
        this.location = location;
    }

    @Override
    public void act(World world) {

    }

    @Override
    public void setAge() {
    }

    @Override
    public void setEnergy(int energyAddition) {
        int maxEnergy = this.energy * this.age;
        if (energy + energyAddition >= maxEnergy) return;
        this.energy += energyAddition;
    }


    @Override
    public void spread(World world) {
    }


    @Override
    public boolean isConsumable() {
        return true;
    }

    @Override
    public void beConsumed() {
    }

    @Override
    public int getEnergyValue() {
        return this.energy;
    }
}
