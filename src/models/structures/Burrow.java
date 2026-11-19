package models.structures;

import itumulator.world.Location;
import models.animals.Rabbit;
import itumulator.world.NonBlocking;

import java.util.ArrayList;
import java.util.List;

public class Burrow implements NonBlocking {
    private List<Rabbit> boundRabbits;
    private Tunnel tunnel;

    public Burrow() {
        boundRabbits = new ArrayList<>();
        tunnel = null;
    }

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
