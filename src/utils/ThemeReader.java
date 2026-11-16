package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ThemeReader {

    private int worldSize;
    private HashMap<String, Integer> actors;
    private Scanner scanner;


    public ThemeReader(String path) throws FileNotFoundException {
        this.scanner = new Scanner(new File(path));
        this.worldSize = 0;
        this.actors = new HashMap<>();
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
                int count;

                if (line.contains("-")) {
                    Random random = new Random();
                    String[] bounds = parts[1].split("-");
                    count = random.nextInt(Integer.parseInt(bounds[0]), Integer.parseInt(bounds[1]));
                } else {
                    count = Integer.parseInt(parts[1]);
                }

                this.actors.put(parts[0].toLowerCase(), count);
            }

            counter++;
        }

        this.scanner.close();

    }

    public int getWorldSize() {
        return worldSize;
    }

    public HashMap<String, Integer> getActors() {
        return actors;
    }

    @Override
    public String toString() {
        return "ThemeReader{" +
                "actors=" + actors +
                ", worldSize=" + worldSize +
                '}';
    }
}
