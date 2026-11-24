package models.structures;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import models.animals.Rabbit;
import itumulator.world.NonBlocking;

import java.util.ArrayList;
import java.util.List;

public class RabbitHole implements Actor, NonBlocking {
    private List<Rabbit> rabbits;

    public RabbitHole() {
        this.rabbits = new ArrayList<>();
    }

    @Override
    public void act(World world) {
        if (world.isDay()) {
            this.wakeUpRabbits(world);
        }
    }


    public void enter(Rabbit rabbit) {
        this.rabbits.add(rabbit);
    }

    public void exit(Rabbit rabbit) {
        this.rabbits.remove(rabbit);
    }

    private void wakeUpRabbits(World world) {
        Location currentLoc = world.getLocation(this);
        for (Rabbit rabbit : new ArrayList<>(this.rabbits)) {
            if (world.isTileEmpty(currentLoc)) {
                rabbit.exitRabbitHole(world, currentLoc);
                this.exit(rabbit);
            }
        }
    }
}
