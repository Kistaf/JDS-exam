package models.animals;

import itumulator.world.World;
import models.Entity;
import interfaces.Consumable;
import interfaces.Reproducible;

public abstract class Animal extends Entity implements Consumable, Reproducible {
    public abstract void move(World world);
    public abstract void eat(Consumable food);
}
