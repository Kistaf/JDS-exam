package models.animals;

import itumulator.world.Location;
import itumulator.world.World;
import utils.Helpers;
import utils.LocationUtils;

import java.util.Set;

public class Wolf extends Animal{
    private static final double REPRODUCTION_ENERGY_MIN = 30;
    private WolfPack wolfPack;
    private int atkDmg;

    public Wolf(WolfPack wolfPack){
        this.wolfPack = wolfPack;
        atkDmg = 1;
    }

    @Override
    protected void performDayTasks(World world) {

    }

    @Override
    protected void performNightTasks(World world) {

    }

    private void hunt(){

    }

    private void attack(){

    }

    private void attemptReproduction(World world, Location location) {
        if (this.energy < REPRODUCTION_ENERGY_MIN) return;
        Set<Animal> closeByRabbits = this.sameSpeciesNearby(world, location);
        Rabbit mate = (Rabbit) Helpers.randomSetElement(closeByRabbits);
        if (mate == null || mate.energy < REPRODUCTION_ENERGY_MIN) return;

        Rabbit baby = new Rabbit(10);
        Location babyLoc = LocationUtils.randomSurroundingFreeTile(world);
        world.setTile(babyLoc, baby);

        this.decreaseEnergy(10);
        mate.decreaseEnergy(10);
    }
}
