package utils;

import java.util.Arrays;

public class ArrayUtils {
    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static int[] concatAll(int[] first, int[]... rest) {
        int totalLength = first.length;
        for (int[] array : rest) {
            totalLength += array.length;
        }
        int[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (int[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static int[] subsection(int[] array, int start, int end) {
        int[] ret = new int[end-start];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = array[i+start];
        }
        return ret;
    }

    public static double[] subsection(double[] array, int start, int end) {
        double[] ret = new double[end-start];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = array[i+start];
        }
        return ret;
    }
}
