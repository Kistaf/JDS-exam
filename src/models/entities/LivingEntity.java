package models.entities;

import itumulator.simulator.Actor;
import itumulator.world.Location;

public abstract class LivingEntity extends WorldEntity implements Actor {
    protected int age;
    protected int energy;
    protected boolean isAlive;

    public LivingEntity(Location location) {
        super(location);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getAge() {
        return age;
    }

    public abstract void setAge();

    public int getEnergy() {
        return energy;
    }

    public abstract void setEnergy(int energyAddition);

    public void die() {
        this.isAlive = false;
    }
}
