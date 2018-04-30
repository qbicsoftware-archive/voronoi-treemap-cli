package life.qbic.voronoi.util;

import java.util.List;

public class NumberUtil {

    private NumberUtil() {
        throw new AssertionError("Instantiating the NumberUtil class is not allowed");
    }

    /**
     * checks if a string is numeric and return result as boolean.
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * returns the minimum value of the given list of doubles
     *
     * @param values
     * @return minimum value of the list
     */
    public static double getMin(List<Double> values) {
        double min = Double.MAX_VALUE;

        for (double d : values) {
            if (d < min)
                min = d;
        }
        return min;
    }

    /**
     * returns the maximum value of the given list of doubles
     *
     * @param values
     * @return maximum value of the list
     */
    public static double getMax(List<Double> values) {
        double max = -Double.MAX_VALUE;

        for (double d : values) {
            if (d > max)
                max = d;
        }
        return max;
    }

    /**
     * normalize a single double value into the range [left, right]
     * given a minimum and maximum and return the normalized value.
     *
     * @param left
     * @param right
     * @return normalize double value
     */
    public static double normalize(double d, double min, double max, double left, double right) {
        return (((d - min) / (max - min)) * (right - left)) + left;
    }

}
