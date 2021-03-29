package utils;

import java.util.Random;

public class NumUtils {
    private static Random randGen = new Random();

    public static double getRandomRange(double min, double max) {
        return randGen.nextDouble() * (max-min) + min;
    }
}
