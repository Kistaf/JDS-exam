package models.structures;

import java.util.ArrayList;
import java.util.List;

public class Tunnel {
    private List<Burrow> burrows;

    public Tunnel(){
        burrows = new ArrayList<>();
    }

    public List<Burrow> getBurrows() {
        return burrows;
    }

    public void addBorrow(Burrow burrow){
        burrows.add(burrow);
        burrow.setTunnel(this);
    }

    public void removeBurrow(Burrow burrow){
        if(!burrows.contains(burrow)) throw new IllegalArgumentException("*** Tunnel does not contain burrow ***");
        burrows.remove(burrow);
        burrow.setTunnel(null);
    }
}
