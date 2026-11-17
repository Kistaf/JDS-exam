package models.animals;

import itumulator.world.Location;
import itumulator.world.World;
import models.entities.LivingEntity;
import interfaces.Consumable;
import interfaces.Reproducible;

public abstract class Animal extends LivingEntity implements Consumable {
    public Animal(Location location) {
        super(location);
    }
    public abstract void eat(Consumable food);
    public abstract void sleep();
}
