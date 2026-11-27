package registry;

import itumulator.world.Location;
import itumulator.world.World;
import models.animals.Rabbit;
import models.animals.Wolf;
import models.animals.WolfPack;
import models.plants.Grass;
import models.plants.berries.BlueberryBush;
import models.plants.berries.RaspberryBush;
import models.structures.RabbitHole;
import models.utils.ThemeReaderEntity;
import utils.LocationUtils;
import utils.NumberUtils;

import java.util.List;

public class Registry {

    // Registry manages no state so best approach to just use a static load method
    public static void load(World world, List<ThemeReaderEntity> entities) {
        for (ThemeReaderEntity entity : entities) {
            if (entity.getName().equalsIgnoreCase("wolf")) {
                Location startLoc = LocationUtils.randomCompletelyFreeTile(world);
                WolfPack wolfPack = new WolfPack();
                for (int i = 0; i < entity.getCount(); i++) {
                    Location randomLoc = LocationUtils.randomSurroundingFreeTile(world, startLoc);
                    while(randomLoc == null){
                        randomLoc = LocationUtils.slowRandomFreeTile(world);
                    }
                    startLoc = randomLoc;
                    world.setTile(randomLoc, new Wolf(wolfPack));// handle pack spawning
                }
            } else {
                for (int i = 0; i < entity.getCount(); i++) {
                    Location randomLoc = LocationUtils.randomCompletelyFreeTile(world);

                    if (randomLoc == null) continue;

                    if (entity.getName().equalsIgnoreCase("bear")) {
                        Location predefinedLoc = entity.getLocation();
                        if (predefinedLoc == null) {
                            // use randomLoc
                        } else {
                            // use predefinedLoc
                        }
                    }
                    else if (entity.getName().equalsIgnoreCase("rabbit")) {
                        Rabbit rabbit = new Rabbit();
                        world.setTile(randomLoc, rabbit);
                    }
                    else if (entity.getName().equalsIgnoreCase("grass")) {
                        Grass grass = new Grass(2);
                        world.setTile(randomLoc, grass);
                    }
                    else if (entity.getName().equalsIgnoreCase("berry")) {
                        boolean ordinaryBush = NumberUtils.chancePercentage(60);
                        if (ordinaryBush) {
                            BlueberryBush blueberryBush = new BlueberryBush(5);
                            world.setTile(randomLoc, blueberryBush);
                        } else {
                            RaspberryBush raspberryBush = new RaspberryBush(6);
                            world.setTile(randomLoc, raspberryBush);
                        }
                    }
                    else if (entity.getName().equalsIgnoreCase("burrow")) {
                        RabbitHole rabbitHole = new RabbitHole();
                        world.setTile(randomLoc, rabbitHole);
                    }
                }
            }
        }
    }
}
