package models.structures;

import interfaces.Consumable;
import itumulator.simulator.Actor;
import itumulator.world.World;
import models.animals.Wolf;

import java.util.List;

public class Carcass implements Actor, Consumable {
    private int energy;
    private int spoilRate;
    private final int consumeEnergy = 10;

    public Carcass(int startEnergy){
        energy = startEnergy;
        spoilRate = 1;
    }

    public Carcass(int startEnergy, int spoilRate){
        energy = startEnergy;
        this.spoilRate = spoilRate;
    }

    @Override
    public void act(World world)
    {
        decreaseEnergy(spoilRate);
        consume(world);
    }

    private void decreaseEnergy(int decreaseBy){
        this.energy = Math.max(0, this.energy - decreaseBy);
    }

    @Override
    public int getEnergyValue() {
        int temp = energy;
        decreaseEnergy(consumeEnergy);
        return Math.min(temp, consumeEnergy);
    }

    @Override
    public void consume(World world) {
        if(energy <= 0){
            world.delete(this);
        }
    }

    @Override
    public List<Object> consumableBy() {
        return List.of(Wolf.class);
    }
}
