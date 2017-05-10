package com.svenwesterlaken.gemeentebreda.logic.util;

/**
 * Created by Sven Westerlaken on 10-5-2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class PixelSizeConverter {

    //This functions is used because some methods need pixels as variables (and don't accept density pixels), but the layout of android use density pixels.
    //In order to keep this compatible, this static method will convert the DP into pixels to use in those methods.
    public static int convertDPtoPX(Context c, int pd) {
        Resources r = c.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                pd,
                r.getDisplayMetrics()
        );

        return px;
    }

}
