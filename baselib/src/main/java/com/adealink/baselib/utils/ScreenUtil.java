package com.adealink.baselib.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Xuefu_Du on 2018/6/24.
 */
public class ScreenUtil {

    private static int screenW;
    private static int screenH;
    private static float screenDensity;

    public static int getScreenW(Context context) {
        if (screenW == 0) {
            initScreen(context);
        }
        return screenW;
    }

    public static int getScreenH(Context context) {
        if (screenH == 0) {
            initScreen(context);
        }
        return screenH;
    }

    private static float getScreenDensity(Context context) {
        if (screenDensity == 0) {
            initScreen(context);
        }
        return screenDensity;
    }

    private static void initScreen(Context context) {
        try {
            DisplayMetrics metric = context.getResources().getDisplayMetrics();
            screenW = metric.widthPixels;
            screenH = metric.heightPixels;
            screenDensity = metric.density;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
