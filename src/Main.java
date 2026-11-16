import java.io.FileNotFoundException;

import itumulator.executable.Program;
import itumulator.world.World;
import utils.ThemeReader;

public class Main {

    public static void main(String[] args) {
        try {
            ThemeReader reader = new ThemeReader("resources/tema/tema1/week-1/t1-2cde.txt");

            Program p = new Program(reader.getWorldSize(), 800, 75);
            World w = p.getWorld();

            p.show();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
