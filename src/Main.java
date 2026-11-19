import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import models.animals.Rabbit;
import models.plants.Grass;
import models.structures.Burrow;
import utils.LocationUtils;
import utils.ThemeReader;

public class Main {

    public static void main(String[] args) {
        try {
            ThemeReader reader = new ThemeReader("resources/tema/tema1/week-1/t1-1a.txt");

            Program p = new Program(reader.getWorldSize(), 800, 400);
            World w = p.getWorld();

            DisplayInformation d1 = new DisplayInformation(Color.green, "grass");
            DisplayInformation d2 = new DisplayInformation(Color.red, "rabbit-small");
            DisplayInformation d3 = new DisplayInformation(Color.blue, "hole");
            p.setDisplayInformation(Rabbit.class, d2);
            p.setDisplayInformation(Grass.class, d1);
            p.setDisplayInformation(Burrow.class, d3);

            for (String model : reader.getModelAmounts().keySet()) {
                for (int i = 0; i < reader.getModelAmounts().get(model); i++) {
                    Location location = LocationUtils.randomFreeTile(w);
                    if (location == null) continue;

                    if (model.equalsIgnoreCase("rabbit")) {
                        Rabbit rabbit = new Rabbit(10);
                        w.setTile(location, rabbit);
                    } else if (model.equalsIgnoreCase("grass")) {
                        Grass grass = new Grass(10,location);
                        w.setTile(location, grass);
                    }else if (model.equalsIgnoreCase("burrow")) {
                        Burrow burrow = new Burrow(location);
                        w.setTile(location, burrow);
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
