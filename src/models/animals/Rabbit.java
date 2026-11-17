package models.animals;

import interfaces.Consumable;
import interfaces.Reproducible;
import itumulator.world.Location;
import itumulator.world.World;
import models.structures.Borrow;

public class Rabbit extends Animal implements Reproducible {
    private Borrow borrow;

    public Rabbit(Location location) {
        super(location);
    }



    @Override
    public void act(World world) {

    }

    @Override
    public void setAge() {

    }

    @Override
    public void setEnergy(int energyAddition) {

    }

    public void seekBorrow(World world) {}

    public void createBorrow(World world) {}

    public void enterBorrow(World world, Borrow borrow) {}

    public void eat(Consumable food){
        if (food.isConsumable() && !food.equals(this)) { // make sure we don't consume ourselves
            this.energy += food.getEnergyValue();
            food.beConsumed();
        }
    }

    @Override
    public void sleep() {

    }

    @Override
    public boolean isConsumable() {
        return false;
    }

    @Override
    public void beConsumed() {

    }

    @Override
    public int getEnergyValue() {
        return 0;
    }

    @Override
    public void reproduce(World world) {

    }
}
