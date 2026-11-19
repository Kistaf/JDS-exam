package utils;

import itumulator.world.Location;
import models.utils.ThemeReaderModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThemeReader {

    private int worldSize;
    private List<ThemeReaderModel> models;
    private Scanner scanner;


    public ThemeReader(String path) throws FileNotFoundException {
        this.scanner = new Scanner(new File(path));
        this.worldSize = 0;
        this.models = new ArrayList<>();
        read();
    }

    public void read() {
        int counter = 0;

        while (this.scanner.hasNextLine()) {
            String line = this.scanner.nextLine();
            if (line.isEmpty()) break;

            if (counter == 0) {
                this.worldSize = Integer.parseInt(line);
            } else {
                String[] parts = line.split(" ");
                String name = parts[0].toLowerCase();

                ThemeReaderModel model = null;
                if (line.contains("-")) {
                    Random random = new Random();
                    String[] bounds = parts[1].split("-");
                    int lowerBound = Integer.parseInt(bounds[0]);
                    int upperBound = Integer.parseInt(bounds[1]);
                    int count = random.nextInt(lowerBound, upperBound);
                    model = new ThemeReaderModel(name, count);
                } else {
                    int count = Integer.parseInt(parts[1]);
                    if (name.equalsIgnoreCase("bear") && parts.length == 3) {
                        Matcher matcher = Pattern.compile("\\((\\d+),(\\d+)\\)").matcher(parts[2]);
                        if (matcher.matches()) {
                            int x = Integer.parseInt(matcher.group(1));
                            int y = Integer.parseInt(matcher.group(2));
                            Location loc = new Location(x, y);
                            model = new ThemeReaderModel(name, count, loc);
                        } else model = new ThemeReaderModel(name, count);

                    } else model = new ThemeReaderModel(name, count);
                }

                this.models.add(model);
            }

            counter++;
        }

        this.scanner.close();

    }

    public int getWorldSize() {
        return worldSize;
    }

    public List<ThemeReaderModel> getModels() {
        return models;
    }

    @Override
    public String toString() {
        return "ThemeReader{" +
                "worldSize=" + worldSize +
                ", models=" + models +
                ", scanner=" + scanner +
                '}';
    }
}
