package interfaces;

import itumulator.world.World;

import java.util.List;

public interface Consumable {
    int getEnergyValue();
    void consume(World world);
    List<Object> consumableBy();
}
