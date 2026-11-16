package models;

import itumulator.simulator.Actor;
import itumulator.world.Location;

public abstract class Entity implements Actor {
    protected int age;
    protected int energy;
    protected Location location;
    protected boolean isAlive;

    public boolean isAlive() {
        return isAlive;
    }

    public Location getLocation() {
        return location;
    }

    public int getAge() {
        return age;
    }

    public int getEnergy() {
        return energy;
    }
}
