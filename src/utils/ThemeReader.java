package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ThemeReader {

    private int worldSize;
    private HashMap<String, Integer> modelAmounts;
    private Scanner scanner;


    public ThemeReader(String path) throws FileNotFoundException {
        this.scanner = new Scanner(new File(path));
        this.worldSize = 0;
        this.modelAmounts = new HashMap<>();
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
                String model = parts[0].toLowerCase();

                int count;
                if (line.contains("-")) {
                    Random random = new Random();
                    String[] bounds = parts[1].split("-");
                    int lowerBound = Integer.parseInt(bounds[0]);
                    int upperBound = Integer.parseInt(bounds[1]);
                    count = random.nextInt(lowerBound, upperBound);
                } else count = Integer.parseInt(parts[1]);

                this.modelAmounts.put(model, count);
            }

            counter++;
        }

        this.scanner.close();

    }

    public int getWorldSize() {
        return worldSize;
    }

    public HashMap<String, Integer> getModelAmounts() {
        return modelAmounts;
    }

    @Override
    public String toString() {
        return "ThemeReader{" +
                "actors=" + modelAmounts +
                ", worldSize=" + worldSize +
                '}';
    }
}
