package com.zlylib.mlhfileselectorlib.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayTools {
    public static int getScreenWidth(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        context.getDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
