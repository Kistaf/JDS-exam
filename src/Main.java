import java.awt.*;
import java.io.FileNotFoundException;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import models.animals.Rabbit;
import models.plants.Grass;
import models.plants.berries.BlueberryBush;
import models.plants.berries.RaspberryBush;
import models.structures.Burrow;
import models.utils.ThemeReaderModel;
import utils.LocationUtils;
import utils.NumberUtils;
import utils.ThemeReader;

public class Main {

    public static void main(String[] args) {
        try {
            ThemeReader reader = new ThemeReader("resources/tema/tema1/week-1/t1-1c.txt");

            Program p = new Program(reader.getWorldSize(), 800, 1000);
            World w = p.getWorld();

            DisplayInformation d1 = new DisplayInformation(Color.green, "grass");
            DisplayInformation d2 = new DisplayInformation(Color.red, "rabbit-small");
            DisplayInformation d3 = new DisplayInformation(Color.blue, "hole");
            p.setDisplayInformation(Rabbit.class, d2);
            p.setDisplayInformation(Grass.class, d1);
            p.setDisplayInformation(Burrow.class, d3);

            for (ThemeReaderModel model : reader.getModels()) {
                if (model.getName().equalsIgnoreCase("wolf")) {
                    for (int i = 0; i < model.getCount(); i++) {
                        // handle pack spawning
                    }
                    continue;
                } else {
                    for (int i = 0; i < model.getCount(); i++) {
                        Location randomLoc = LocationUtils.randomCompletelyFreeTile(w);
                        if (randomLoc == null) continue;

                        if (model.getName().equalsIgnoreCase("bear")) {
                            Location predefinedLoc = model.getLoc();
                            if (predefinedLoc == null) {
                                // use randomLoc
                            } else {
                                // use predefinedLoc
                            }
                            continue;
                        }

                        if (model.getName().equalsIgnoreCase("rabbit")) {
                            Rabbit rabbit = new Rabbit("rabbit");
                            w.setTile(randomLoc, rabbit);
                            continue;
                        }

                        if (model.getName().equalsIgnoreCase("grass")) {
                            Grass grass = new Grass(2);
                            w.setTile(randomLoc, grass);
                            continue;
                        }

                        if (model.getName().equalsIgnoreCase("berry")) {
                            boolean ordinaryBush = NumberUtils.chance(60);
                            if (ordinaryBush) {
                                BlueberryBush blueberryBush = new BlueberryBush(5);
                                w.setTile(randomLoc, blueberryBush);
                            } else {
                                RaspberryBush raspberryBush = new RaspberryBush(6);
                                w.setTile(randomLoc, raspberryBush);
                            }
                            continue;
                        }

                        if (model.getName().equalsIgnoreCase("burrow")) {
                            Burrow burrow = new Burrow();
                            w.setTile(randomLoc, burrow);
                        }
                    }
                }
            }

            p.show();
            for (int i = 0; i < 1000; i++) {
                p.simulate();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
