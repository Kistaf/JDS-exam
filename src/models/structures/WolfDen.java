package models.structures;

import itumulator.world.NonBlocking;
import models.animals.Wolf;
import models.animals.WolfPack;

import java.util.ArrayList;
import java.util.List;

public class WolfDen implements NonBlocking {
    private List<Wolf> occupyingWolfs;

    public WolfDen(){
        occupyingWolfs = new ArrayList<>();
    }

    public List<Wolf> getOccupyingWolfs() {
        return occupyingWolfs;
    }

    public void enter(Wolf wolf){
        occupyingWolfs.add(wolf);
    }

    public void exit(Wolf wolf){
        occupyingWolfs.remove(wolf);
    }


}
