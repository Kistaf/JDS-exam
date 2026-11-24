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

            Program p = new Program(reader.getWorldSize(), 800, 1200);
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


            for (ThemeReaderModel model : reader.getModels()) {
                if (model.getName().equalsIgnoreCase("wolf")) {
                    Location startLoc = LocationUtils.randomCompletelyFreeTile(w);
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
                        }
                        else if (model.getName().equalsIgnoreCase("rabbit")) {
                            Rabbit rabbit = new Rabbit();
                            w.setTile(randomLoc, rabbit);
                        }
                        else if (model.getName().equalsIgnoreCase("grass")) {
                            Grass grass = new Grass(2);
                            w.setTile(randomLoc, grass);
                        }
                        else if (model.getName().equalsIgnoreCase("berry")) {
                            boolean ordinaryBush = NumberUtils.chancePercentage(60);
                            if (ordinaryBush) {
                                BlueberryBush blueberryBush = new BlueberryBush(5);
                                w.setTile(randomLoc, blueberryBush);
                            } else {
                                RaspberryBush raspberryBush = new RaspberryBush(6);
                                w.setTile(randomLoc, raspberryBush);
                            }
                        }
                        else if (model.getName().equalsIgnoreCase("burrow")) {
                            RabbitHole rabbitHole = new RabbitHole();
                            w.setTile(randomLoc, rabbitHole);
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
