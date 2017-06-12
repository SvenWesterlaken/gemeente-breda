package com.svenwesterlaken.gemeentebreda.logic.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Sven Westerlaken on 11-6-2017.
 */

public class SizeUtil {

    private SizeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static int convertDPtoPX(Context c, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }
}
