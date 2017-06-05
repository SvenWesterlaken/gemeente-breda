package com.svenwesterlaken.gemeentebreda.logic.util;

/**
 * Created by Sven Westerlaken on 5-6-2017.
 */

public class DoubleUtil {

    public static boolean isZero(double value, double threshold){
        return value >= -threshold && value <= threshold;
    }
}
