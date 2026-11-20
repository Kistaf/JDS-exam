package models.animals;

import itumulator.world.World;

public class Wolf extends Animal {
    public Wolf(String imageKey) {
        super(imageKey);
    }

    @Override
    protected void performDayTasks(World world) {

    }

    @Override
    protected void performNightTasks(World world) {

    }

    @Override
    protected void performEat(World world) {

    }

    @Override
    protected double maxEnergy() {
        return 0;
    }

    @Override
    protected double maxAge() {
        return 0;
    }

    @Override
    protected double babyAge() {
        return 0;
    }

    @Override
    protected double adultAge() {
        return 0;
    }
}
