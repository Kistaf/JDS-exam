package interfaces;

import itumulator.world.World;

import java.util.List;

public interface Consumable {
    double getEnergyValue();
    void consume(World world);
    List<Object> consumableBy();
}
