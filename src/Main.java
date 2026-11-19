import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import models.animals.Rabbit;
import models.plants.Grass;
import models.structures.Borrow;
import utils.ThemeReader;

public class Main {

    public static void main(String[] args) {
        try {
            ThemeReader reader = new ThemeReader("resources/tema/tema1/week-1/t1-2cde.txt");
            System.out.println(reader);

            Program p = new Program(reader.getWorldSize(), 800, 75);
            World w = p.getWorld();
            Random random = new Random();

            DisplayInformation d1 = new DisplayInformation(Color.green, "grass");
            DisplayInformation d2 = new DisplayInformation(Color.red, "rabbit-small");
            p.setDisplayInformation(Grass.class, d1);
            p.setDisplayInformation(Rabbit.class, d2);

            for (String model : reader.getModelAmounts().keySet()) {
                for (int i = 0; i < reader.getModelAmounts().get(model); i++) {
                    int x = random.nextInt(reader.getWorldSize());
                    int y = random.nextInt(reader.getWorldSize());

                    Location location = new Location(x, y);
                    while (!w.isTileEmpty(location) || w.containsNonBlocking(location)) {
                        x = random.nextInt(reader.getWorldSize());
                        y = random.nextInt(reader.getWorldSize());
                        location = new Location(x, y);
                    }
                    if (model.equalsIgnoreCase("rabbit")) {
                        Rabbit rabbit = new Rabbit(20);
                        w.setTile(location, rabbit);
                    } else if (model.equalsIgnoreCase("grass")) {
                        Grass grass = new Grass(10,location);
                        w.setTile(location, grass);
                    }
                }
            }

            p.show();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
