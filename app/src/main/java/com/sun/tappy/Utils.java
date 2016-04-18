package com.sun.tappy;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by sunje on 2016-02-22.
 */
public class Utils {
    public static int dp2px(Context c, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }
}
