package models.structures;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import models.animals.Rabbit;
import itumulator.world.NonBlocking;
import utils.CollectionUtils;
import utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RabbitHole implements Actor, NonBlocking {
    private List<Rabbit> rabbits;

    public RabbitHole() {
        this.rabbits = new ArrayList<>();
    }

    @Override
    public void act(World world) {
        if (world.isDay()) {
            this.wakeUpRabbits(world);
        } else if (world.isNight()) {
            this.makeRabbitsSleep();
            this.reproduceRabbits(world);
        }
    }


    public void enter(Rabbit rabbit) {
        this.rabbits.add(rabbit);
    }

    public void exit(Rabbit rabbit) {
        this.rabbits.remove(rabbit);
    }

    private void makeRabbitsSleep() {
        for (Rabbit rabbit : this.rabbits) {
            rabbit.sleepRecovery();
        }
    }

    private void reproduceRabbits(World world) {
        Set<Rabbit> reproducibleRabbits = this.rabbits.stream()
                .filter(Rabbit::canReproduce)
                .collect(Collectors.toSet());
        if (reproducibleRabbits.size() <= 1) return;
        int reproductionCount = (int) Math.floor((double) reproducibleRabbits.size() / 2);
        for (int i = 0; i < reproductionCount; i++) {
            Rabbit rabbit = new Rabbit();
            world.add(rabbit);
            this.enter(rabbit);
        }

        reproducibleRabbits.forEach(Rabbit::reproduce);
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
