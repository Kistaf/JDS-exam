package entity;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.List;

public class Borrow implements Actor, NonBlocking {
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Rabbit> getRabbit() {
        return rabbit;
    }

    public void setRabbit(List<Rabbit> rabbit) {
        this.rabbit = rabbit;
    }

    private Location location;
    private List<Rabbit> rabbit;

    @Override
    public void act(World world) {

    }

}
