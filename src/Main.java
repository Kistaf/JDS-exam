import java.awt.*;
import java.io.FileNotFoundException;

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
import models.structures.Burrow;
import models.structures.Carcass;
import models.structures.WolfDen;
import models.utils.ThemeReaderModel;
import utils.LocationUtils;
import utils.NumberUtils;
import utils.ThemeReader;

public class Main {

    public static void main(String[] args) {
        try {
            ThemeReader reader = new ThemeReader("resources/tema/tema1/week-1/t1-1c.txt");

            Program p = new Program(reader.getWorldSize(), 800, 100);
            World w = p.getWorld();

            DisplayInformation d1 = new DisplayInformation(Color.green, "grass");
            DisplayInformation d2 = new DisplayInformation(Color.red, "rabbit-small");
            DisplayInformation d3 = new DisplayInformation(Color.blue, "hole-small");
            DisplayInformation d4 = new DisplayInformation(Color.gray, "wolf");
            DisplayInformation d5 = new DisplayInformation(Color.DARK_GRAY, "hole");
            DisplayInformation d6 = new DisplayInformation(Color.orange, "carcass");
            p.setDisplayInformation(Rabbit.class, d2);
            p.setDisplayInformation(Grass.class, d1);
            p.setDisplayInformation(Burrow.class, d3);
            p.setDisplayInformation(Wolf.class, d4);
            p.setDisplayInformation(WolfDen.class, d5);
            p.setDisplayInformation(Carcass.class, d6);


            for (ThemeReaderModel model : reader.getModels()) {
                if (model.getName().equalsIgnoreCase("wolf")) {
                    Location startLoc = LocationUtils.slowRandomFreeTile(w);
                    WolfPack wolfPack = new WolfPack();
                    for (int i = 0; i < model.getCount(); i++) {
                        Location randomLoc = LocationUtils.randomSurroundingFreeTile(w, startLoc);
                        while(randomLoc == null){
                            randomLoc = LocationUtils.slowRandomFreeTile(w);
                        }
                        startLoc = randomLoc;
                        w.setTile(randomLoc, new Wolf(wolfPack));// handle pack spawning
                    }
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
                        else if (model.getName().equalsIgnoreCase("rabbit")) {
                            System.out.println("Rabbit");
                            Rabbit rabbit = new Rabbit();
                            w.setTile(randomLoc, rabbit);
                            continue;
                        }
                        else if (model.getName().equalsIgnoreCase("grass")) {
                            System.out.println("Grass");
                            Grass grass = new Grass(2);
                            w.setTile(randomLoc, grass);
                            continue;
                        }
                        else if (model.getName().equalsIgnoreCase("berry")) {
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
                        else if (model.getName().equalsIgnoreCase("burrow")) {
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
