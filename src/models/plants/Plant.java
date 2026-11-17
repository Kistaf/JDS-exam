package models.plants;

import itumulator.world.Location;
import itumulator.world.World;
import models.entities.LivingEntity;
import itumulator.world.NonBlocking;

public abstract class Plant extends LivingEntity implements NonBlocking {
    public Plant(Location location) {
        super(location);
    }
    public abstract void spread(World world);
}
