package com.example.andrewpalka.scrollingactivity;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by andrewpalka on 1/30/17.
 */

public class Utils {
    // dynamically spaces pixels
    public static int dpToPx(final float dp) {
        return Math.round(dp * (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}

