package models.animals;

import itumulator.simulator.Actor;
import itumulator.world.World;
import models.structures.WolfDen;

import java.util.ArrayList;
import java.util.List;

public class WolfPack {
    private List<Wolf> wolfs;
    private WolfDen wolfDen;
    private Animal prey;

    public WolfPack() {
        wolfs = new ArrayList<>();
        wolfDen = null;
        prey = null;
    }

    public void checkPrayStatus(World world){
        if (prey != null && !world.getEntities().containsKey(prey)){
            setPrey(null);
        }
    }

    public void addWolf(Wolf wolf){
        wolfs.add(wolf);
    }

    public void removeWolf(Wolf wolf){
        wolfs.remove(wolf);
    }

    public List<Wolf> getWolfs() {
        return wolfs;
    }

    public WolfDen getWolfDen() {
        return wolfDen;
    }

    public void setWolfDen(WolfDen wolfDen) {
        this.wolfDen = wolfDen;
    }

    public Animal getPrey() {
        return prey;
    }

    public void setPrey(Animal prey) {
        this.prey = prey;
    }

}
