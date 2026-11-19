package interfaces;

import itumulator.world.World;

import java.util.List;

public interface Consumable {
    int getEnergyValue();
    int consume(World world);
    List<Object> consumableBy();
}
