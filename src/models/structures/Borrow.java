package models.structures;

import models.animals.Rabbit;
import itumulator.world.NonBlocking;

import java.util.ArrayList;
import java.util.List;

public class Borrow implements NonBlocking {
    private final List<Rabbit> rabbits;

    public Borrow() {
        this.rabbits = new ArrayList<>();
    }

    public List<Rabbit> getRabbits() {
        return rabbits;
    }

    public void addRabbit(Rabbit rabbit) {}

    public void removeRabbit(Rabbit rabbit) {}

}
