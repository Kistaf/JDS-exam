package models.structures;

import java.util.ArrayList;
import java.util.List;

public class Tunnel {
    private List<RabbitHole> rabbitHoles;

    public Tunnel(){
        rabbitHoles = new ArrayList<>();
    }

    public List<RabbitHole> getBurrows() {
        return rabbitHoles;
    }

    public void addBorrow(RabbitHole rabbitHole){
        rabbitHoles.add(rabbitHole);
        rabbitHole.setTunnel(this);
    }

    public void removeBurrow(RabbitHole rabbitHole){
        if(!rabbitHoles.contains(rabbitHole)) throw new IllegalArgumentException("*** Tunnel does not contain burrow ***");
        rabbitHoles.remove(rabbitHole);
        rabbitHole.setTunnel(null);
    }
}
