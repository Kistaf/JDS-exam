package models.plants;

import itumulator.world.World;

import java.util.Random;

public class Grass extends Plant {

    public Grass() {
    }

    @Override
    public void act(World world) {
        // example of use of act() on grass
        // random growth & only during day
        Random random = new Random();
        int chance = random.nextInt(0, 9);
        if (chance % 3 == 0 && world.isDay()) this.grow();
    }

    @Override
    public void grow() {

    }

    @Override
    public void spread(World world) {

    }

}
