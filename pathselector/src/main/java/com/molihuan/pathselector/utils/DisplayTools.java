package com.molihuan.pathselector.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 显示工具类
 */
public class DisplayTools {
    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        context.getDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
