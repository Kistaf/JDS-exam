package models.plants.berries;

import enums.Berry;
import interfaces.Consumable;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;
import models.animals.Bear;

import java.awt.*;
import java.util.List;

public abstract class BerryBush implements Actor, Consumable, DynamicDisplayInformationProvider {
    protected Berry stage = Berry.DORMANT;
    protected int ripeCooldown = 20;
    protected double energy;

    public BerryBush(double energy) {
        this.energy = energy;
    }

    @Override
    public void act(World world) {
        if (this.stage == Berry.DORMANT && world.isDay()) {
            if (this.ripeCooldown == 0) this.stage = Berry.RIPE;
            else this.ripeCooldown = Math.max(0, this.ripeCooldown - 1);
        }
    }



    @Override
    public double getEnergyValue() {
        return this.energy;
    }

    @Override
    public void consume(World world) {
        this.stage = Berry.DORMANT;
        this.ripeCooldown = 20;
    }

    @Override
    public List<Object> consumableBy() {
        return List.of(Bear.class);
    }

    @Override
    public DisplayInformation getInformation() {
        StringBuilder builder = new StringBuilder("bush");
        if (this.stage == Berry.RIPE) builder.append("-berries");
        return new DisplayInformation(Color.GREEN, builder.toString());
    }
}
