package entity;

import itumulator.simulator.Actor;
import itumulator.world.World;

public class Rabbit implements Actor {
    private int age;
    private Borrow borrow;

    @Override
    public void act(World world) {

    }

    public void digBorrow(World world) {}

    public void reproduce() {}

    public void eatGrass(Grass grass) {}

    public void enterBorrow(Borrow borrow) {}


}
