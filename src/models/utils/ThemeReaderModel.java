package models.utils;

import itumulator.world.Location;

public class ThemeReaderModel {
    private String name;
    private int count;
    private Location loc;


    public ThemeReaderModel(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public ThemeReaderModel(String name, int count,Location loc) {
        this.loc = loc;
        this.count = count;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public Location getLoc() {
        return loc;
    }
}
