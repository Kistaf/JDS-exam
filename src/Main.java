import java.awt.*;
import java.io.FileNotFoundException;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import models.animals.Rabbit;
import models.plants.Grass;
import models.structures.Burrow;
import models.utils.ThemeReaderModel;
import utils.LocationUtils;
import utils.ThemeReader;

public class Main {

    public static void main(String[] args) {
        try {
            ThemeReader reader = new ThemeReader("resources/tema/tema1/week-1/t1-2cde.txt");

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
                } else {
                    for (int i = 0; i < model.getCount(); i++) {
                        Location randomLoc = LocationUtils.randomFreeTile(w);
                        if (randomLoc == null) continue;

                        if (model.getName().equalsIgnoreCase("bear")) {
                            Location predefinedLoc = model.getLoc();
                            if (predefinedLoc == null) {
                                // use randomLoc
                            } else {
                                // use predefinedLoc
                            }
                        } else if (model.getName().equalsIgnoreCase("rabbit")) {
                            Rabbit rabbit = new Rabbit();
                            w.setTile(randomLoc, rabbit);
                        } else if (model.getName().equalsIgnoreCase("grass")) {
                            Grass grass = new Grass(5);
                            w.setTile(randomLoc, grass);
                        } else if (model.getName().equalsIgnoreCase("berry")) {
                            // probably randomly decide if it should be a blue- or raspberry bush

                        } else if (model.getName().equalsIgnoreCase("burrow")) {
                            Burrow burrow = new Burrow(randomLoc);
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
