package models.structures;

import itumulator.world.Location;
import models.entities.StructureEntity;
import models.animals.Rabbit;
import itumulator.world.NonBlocking;

import java.util.ArrayList;
import java.util.List;

public class Borrow extends StructureEntity implements NonBlocking {
    private List<Rabbit> rabbits;

    public Borrow(Location location) {
        super(location);
        this.rabbits = new ArrayList<>();
    }

    public List<Rabbit> getRabbits() {
        return rabbits;
    }

    public void addRabbit() {}

}
