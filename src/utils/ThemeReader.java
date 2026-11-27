package utils;

import itumulator.world.Location;
import models.utils.ThemeReaderModel;
import org.junit.platform.engine.support.discovery.SelectorResolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThemeReader {
    private final int worldSize;
    private final List<ThemeReaderModel> models;

    private static final Pattern RANGE_PATTERN = Pattern.compile("(\\d+)-(\\d+)");
    private static final Pattern LOCATION_PATTERN = Pattern.compile("\\((\\d+),(\\d+)\\)");


    public ThemeReader(String path) throws IOException {
        List<ThemeReaderModel> parsedModels = new ArrayList<>();
        Random random = new Random();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(path))) {
            String firstLine = reader.readLine();

            if (firstLine == null) {
                throw new IOException("The input file is empty: " + path);
            }

            try {
                this.worldSize = Integer.parseInt(firstLine.trim());
            } catch (NumberFormatException e) {
                throw new IOException("Invalid world size: " + e);
            }

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                try {
                    ThemeReaderModel model = this.parseModel(line, random);
                    parsedModels.add(model);
                } catch (IllegalArgumentException e) {
                    String error = String.format("Invalid entry on line %d: %s", lineNumber, e);
                    throw new IOException(error);
                }
            }
        }

        this.models = parsedModels;
    }

    private ThemeReaderModel parseModel(String line, Random random) {
        String[] parts = line.split("\\s+");

        if (parts.length < 2 || parts.length > 4) {
            throw new IllegalArgumentException("Invalid model format");
        }

        String name = parts[0].toLowerCase();
        int count = this.parseCount(parts[1], random);

        if (name.equals("bear") && parts.length == 3) {
            Location location = this.parseLocation(parts[2]);
            return new ThemeReaderModel(name, count, location);
        }

        return new ThemeReaderModel(name, count);
    }

    private int parseCount(String value, Random random) {
        Matcher matcher = RANGE_PATTERN.matcher(value);

        if (!matcher.matches()) { // not a range
            return Integer.parseInt(value);
        }

        int lowerBound = Integer.parseInt(matcher.group(1));
        int upperBound = Integer.parseInt(matcher.group(2));

        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("Invalid range: " + value);
        }

        return random.nextInt(lowerBound, upperBound);
    }

    private Location parseLocation(String value) {
        Matcher matcher = LOCATION_PATTERN.matcher(value);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid location: " + value);
        }

        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));

        return new Location(x, y);
    }

    public int getWorldSize() {
        return worldSize;
    }

    public List<ThemeReaderModel> getModels() {
        return List.copyOf(this.models);
    }

    @Override
    public String toString() {
        return "ThemeReader{" +
                "worldSize=" + worldSize +
                ", models=" + models +
                '}';
    }
}
