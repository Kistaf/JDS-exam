package models.structures;

import itumulator.world.World;
import models.animals.Rabbit;
import itumulator.world.NonBlocking;

import java.util.List;

public class Borrow extends Structure implements NonBlocking {
    private List<Rabbit> rabbits;

    public List<Rabbit> getRabbit() {
        return rabbits;
    }

    public void setRabbits(List<Rabbit> rabbits) {
        this.rabbits = rabbits;
    }


}
