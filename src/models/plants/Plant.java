package models.plants;

import itumulator.world.World;
import models.Entity;
import itumulator.world.NonBlocking;

public abstract class Plant extends Entity implements NonBlocking {
    public abstract void grow();
    public abstract void spread(World world);
}
