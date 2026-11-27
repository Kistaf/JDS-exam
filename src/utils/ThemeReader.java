package utils;

import itumulator.world.Location;
import models.utils.ThemeReaderEntity;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThemeReader {
    private final int worldSize;
    private final List<ThemeReaderEntity> entities;

    private static final Pattern RANGE_PATTERN = Pattern.compile("(\\d+)-(\\d+)");
    private static final Pattern LOCATION_PATTERN = Pattern.compile("\\((\\d+),(\\d+)\\)");

    private static final Set<String> ENTITIES = Set.of(
            "grass",
            "rabbit",
            "burrow",
            "carcass",
            "wolf",
            "bear",
            "berry"
    );

    private static final Set<String> VARIANTS = Set.of(
            "fungi",
            "cordyceps"
    );


    public ThemeReader(String path) throws IOException {
        List<ThemeReaderEntity> parsedEntities = new ArrayList<>();
        Random random = new Random();

        try (Scanner reader = new Scanner(new File(path))) {
            String firstLine = reader.nextLine();

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

            while (reader.hasNextLine()) {
                line = reader.nextLine().trim();
                lineNumber++;

                if (line.isEmpty()) {
                    continue;
                }

                try {
                    ThemeReaderEntity entity = this.parseEntity(line, random);
                    parsedEntities.add(entity);
                } catch (IllegalArgumentException e) {
                    String error = String.format("Invalid entry on line %d: %s", lineNumber, e);
                    throw new IOException(error);
                }
            }
        }

        this.entities = parsedEntities;
    }

    private ThemeReaderEntity parseEntity(String line, Random random) {
        String[] parts = line.split("\\s+");

        if (parts.length < 2 || parts.length > 4) {
            throw new IllegalArgumentException("Invalid model format");
        }

        String name;
        int count;
        Location location = null;
        String variant = null;

        int leadingEndIdx = parts.length;

        if (parts[leadingEndIdx - 1].startsWith("(")) { // line contains a location
            location = parseLocation(parts[leadingEndIdx - 1]);
            leadingEndIdx--;
        }

        if (leadingEndIdx < 2) {
            throw new IllegalStateException("Count is missing: " + line);
        }

        count = parseCount(parts[leadingEndIdx - 1], random);

        String[] nameAndVariant = Arrays.copyOfRange(parts, 0, leadingEndIdx - 1);


        // no variant
        if (nameAndVariant.length == 1) {
            name = nameAndVariant[0].toLowerCase();
            if (!ENTITIES.contains(name)) {
                throw new IllegalArgumentException("Entity is not in the register: " + name);
            }
        } else if (nameAndVariant.length == 2) { // variants also in input file
            String first = nameAndVariant[0].toLowerCase();
            String second = nameAndVariant[1].toLowerCase();

            boolean firstIsEntity = ENTITIES.contains(first) && VARIANTS.contains(second);
            boolean secondIsEntity = VARIANTS.contains(first) && ENTITIES.contains(second);

            if (firstIsEntity == secondIsEntity) { // something went wrong
                throw new IllegalArgumentException("Failed to interpret name & variant");
            }

            name = firstIsEntity ? first : second;
            variant = firstIsEntity ? second : first;
        } else {
            throw new IllegalArgumentException("Invalid line entry: " + line);
        }

        if (location != null && !name.equals("bear")) {
            throw new IllegalArgumentException("Only bears can have a pre-defined spawn location");
        }

        return new ThemeReaderEntity(name, count, location, variant);
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

    public List<ThemeReaderEntity> getEntities() {
        return List.copyOf(this.entities);
    }

    @Override
    public String toString() {
        return "ThemeReader{" +
                "worldSize=" + worldSize +
                ", entities=" + entities +
                '}';
    }
}
