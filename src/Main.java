import java.awt.*;
import java.io.IOException;

import registry.Registry;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;
import models.animals.Rabbit;
import models.animals.Wolf;
import models.plants.Grass;
import models.structures.RabbitHole;
import models.structures.Carcass;
import models.structures.WolfDen;
import utils.ThemeReader;

public class Main {

    public static void main(String[] args) {
        try {
            ThemeReader reader = new ThemeReader("resources/tema/tema3/week-3/tf3-1a.txt");

            Program p = new Program(reader.getWorldSize(), 800, 200);
            World w = p.getWorld();

            DisplayInformation d1 = new DisplayInformation(Color.green, "grass");
            DisplayInformation d2 = new DisplayInformation(Color.red, "rabbit-small");
            DisplayInformation d3 = new DisplayInformation(Color.blue, "hole-small");
            DisplayInformation d4 = new DisplayInformation(Color.gray, "wolf-small");
            DisplayInformation d5 = new DisplayInformation(Color.DARK_GRAY, "hole");
            DisplayInformation d6 = new DisplayInformation(Color.orange, "carcass");
            p.setDisplayInformation(Rabbit.class, d2);
            p.setDisplayInformation(Grass.class, d1);
            p.setDisplayInformation(RabbitHole.class, d3);
            p.setDisplayInformation(Wolf.class, d4);
            p.setDisplayInformation(WolfDen.class, d5);
            p.setDisplayInformation(Carcass.class, d6);


            Registry.load(w, reader.getEntities());

            p.show();
            for (int i = 0; i < 1000; i++) {
                p.simulate();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
