package models.structures;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import models.animals.Rabbit;
import itumulator.world.NonBlocking;

import java.util.ArrayList;
import java.util.List;

public class Burrow implements NonBlocking {
    private Location location;
    private List<Rabbit> boundRabbits;
    private Tunnel tunnel;

    public Burrow(Location location) {
        this.location = location;
        boundRabbits = new ArrayList<>();
        tunnel = null;
    }

    public Location getLocation() { return location; }

    public List<Rabbit> getRabbits() { return boundRabbits; }

    public void addRabbit(Rabbit rabbit) {
        if(boundRabbits.contains(rabbit)) throw new IllegalArgumentException("*** Already contains rabbit ***");
        boundRabbits.add(rabbit);
    }

    public void removeRabbit(Rabbit rabbit) {
        if(!boundRabbits.contains(rabbit)) throw new IllegalArgumentException("*** Does not contain rabbit ***");
        boundRabbits.remove(rabbit);
    }

    public Tunnel getTunnel() {
        return tunnel;
    }

    public void setTunnel(Tunnel tunnel) {
        this.tunnel = tunnel;
    }

}
