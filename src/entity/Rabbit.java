package entity;

import itumulator.simulator.Actor;
import itumulator.world.World;

public class Rabbit implements Actor {
    private int age;
    private Borrow borrow;

    @Override
    public void act(World world) {

    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Borrow getBorrow() {
        return borrow;
    }

    public void setBorrow(Borrow borrow) {
        this.borrow = borrow;
    }

    public void returnToBorrow() {
        // Determine if the rabbit has a borrow assigned
        // Calculate vector between rabbit position and borrow. Store this
        // Each tick after follow this vector to the borrow.
        // Randomly decide if they make it or not to the borrow
    }

    public void digBorrow(World world) {}

    public void reproduce() {}

    public void eatGrass(Grass grass) {}

    public void enterBorrow(Borrow borrow) {}


}
