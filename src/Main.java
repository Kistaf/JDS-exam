import java.awt.Color;
import java.io.FileNotFoundException;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import utils.ThemeReader;

public class Main {

    public static void main(String[] args) {
        int size = 5;
        Program p = new Program(size, 800, 75);

        World w = p.getWorld();

        // w.setTile(new Location(0, 0), new <MyClass>());

        // p.setDisplayInformation(<MyClass>.class, new DisplayInformation(<Color>, "<ImageName>"));

        // p.show();

        try {
            ThemeReader reader = new ThemeReader("resources/tema/tema1/week-1/t1-2cde.txt");
            System.out.println(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}