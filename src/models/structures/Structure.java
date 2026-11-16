package models.structures;

import itumulator.world.Location;

public abstract class Structure {
    protected Location location;

    public Location getLocation() {
        return location;
    }
}
