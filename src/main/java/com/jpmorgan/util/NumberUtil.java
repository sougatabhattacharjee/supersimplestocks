package com.jpmorgan.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Sougata Bhattacharjee
 * On 11.03.18
 */
public class NumberUtil {

    /**
     * Round a double value upto 2 decimal places
     * @param input - a double value
     * @return double value
     */
    public static double roundOffTo2DecPlaces(final double input) {
        final DecimalFormat format = new DecimalFormat("##.00");
        return Double.valueOf(format.format(input));
    }

    /**
     * Round a double value up to input scale
     * @param input double value
     * @param scale scale precision
     * @return the round double
     */
    public static double round(final double input, final int scale) {
        return round(input, scale, BigDecimal.ROUND_HALF_UP);
    }

    private static double round(final double input, final int scale, final int roundingMethod) {
        try {
            return (new BigDecimal
                    (Double.toString(input))
                    .setScale(scale, roundingMethod))
                    .doubleValue();
        } catch (final NumberFormatException ex) {
            if (Double.isInfinite(input)) {
                return input;
            } else {
                return Double.NaN;
            }
        }
    }

    /**
     * Generating Random integer between 10 to 100
     *
     * @return an integer
     */
    public static int randInt() {
        final int max = 100, min = 10;
        final Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Generating Random double between 10.00 to 100.00
     *
     * @return a double
     */
    public static double randDouble() {
        return roundOffTo2DecPlaces(ThreadLocalRandom.current().nextDouble(10d, 100d));
    }

}
