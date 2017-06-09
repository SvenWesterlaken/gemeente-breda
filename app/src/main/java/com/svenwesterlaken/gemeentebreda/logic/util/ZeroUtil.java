package com.svenwesterlaken.gemeentebreda.logic.util;

/**
 * Created by Sven Westerlaken on 5-6-2017.
 */

public class ZeroUtil {

    private ZeroUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isZero(double value, double threshold){
        return value >= -threshold && value <= threshold;
    }

    public static boolean isZero(float value, float threshold) {
        return value >= -threshold && value <= threshold;
    }
}
