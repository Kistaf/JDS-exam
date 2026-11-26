import java.awt.*;
import java.io.FileNotFoundException;

import Registry.Registry;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import models.animals.Rabbit;
import models.animals.Wolf;
import models.animals.WolfPack;
import models.plants.Grass;
import models.plants.berries.BlueberryBush;
import models.plants.berries.RaspberryBush;
import models.structures.RabbitHole;
import models.structures.Carcass;
import models.structures.WolfDen;
import models.utils.ThemeReaderModel;
import utils.LocationUtils;
import utils.NumberUtils;
import utils.ThemeReader;

public class Main {

    public static void main(String[] args) {
        try {
            ThemeReader reader = new ThemeReader("resources/tema/tema2/week-2/t2-1ab.txt");

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


            Registry.load(w, reader.getModels());

            p.show();
            for (int i = 0; i < 1000; i++) {
                p.simulate();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
