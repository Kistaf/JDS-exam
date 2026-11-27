package models.utils;

import itumulator.world.Location;

public class ThemeReaderEntity {
    private String name;
    private Integer count;
    private Location location;
    private String variant;

    public ThemeReaderEntity(String name, Integer count, Location location, String variant) {
        this.name = name;
        this.count = count;
        this.location = location;
        this.variant = variant;
    }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }

    public Location getLocation() {
        return location;
    }

    public String getVariant() {
        return variant;
    }

    @Override
    public String toString() {
        return "ThemeReaderEntity{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", location=" + location +
                ", variant='" + variant + '\'' +
                '}';
    }
}
