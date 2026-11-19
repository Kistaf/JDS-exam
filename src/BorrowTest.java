import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import models.structures.Borrow;
import utils.ThemeReader;

public class BorrowTest {

    public static void main(String[] args) {
        try {
            ThemeReader reader = new ThemeReader("resources/tema/tema1/week-1/borrowTest.txt");
            System.out.println(reader);

            Program p = new Program(reader.getWorldSize(), 800, 75);
            World w = p.getWorld();
            Random random = new Random();

            DisplayInformation holeDI = new DisplayInformation(Color.red, "hole-small");
            p.setDisplayInformation(Borrow.class, holeDI);

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

                    if(model.equalsIgnoreCase("borrow")){
                        w.setTile(location, new Borrow());
                    }
                }
            }

            p.show();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
