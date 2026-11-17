package models.entities;

import itumulator.world.Location;

public class WorldEntity {
    protected Location location;

    public WorldEntity(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
