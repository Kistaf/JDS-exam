import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import models.animals.Rabbit;
import models.entities.LivingEntity;
import models.entities.WorldEntity;
import models.plants.Grass;
import utils.ThemeReader;

public class Main {

    public static void main(String[] args) {
        try {
            ThemeReader reader = new ThemeReader("resources/tema/tema1/week-1/t1-1a.txt");
            System.out.println(reader);

            Program p = new Program(reader.getWorldSize(), 800, 75);
            World w = p.getWorld();
            Random random = new Random();

            DisplayInformation di = new DisplayInformation(Color.green, "grass");
            DisplayInformation dj = new DisplayInformation(Color.red, "rabbit-small");
            p.setDisplayInformation(Grass.class, di);
            p.setDisplayInformation(Rabbit.class, dj);

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
                }
            }

            p.show();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
