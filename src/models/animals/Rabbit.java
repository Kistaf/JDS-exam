package models.animals;

import interfaces.Consumable;
import itumulator.world.World;

public class Rabbit extends Animal {

    @Override
    public void act(World world) {

    }

    @Override
    public void move(World world) {

    }

    public void digBorrow(World world) {}

    public void eat(Consumable food){
        if (food.isConsumable() && !food.equals(this)) { // make sure we don't consume ourselves
            this.energy += food.getEnergyValue();
            food.beConsumed();
        }
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
}
