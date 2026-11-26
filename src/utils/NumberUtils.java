package utils;

import java.util.Random;

public class NumberUtils {
    public static boolean chancePercentage(int percentage) {
        Random random = new Random();
        return random.nextDouble() <= percentage / 100.0;
    }
}
