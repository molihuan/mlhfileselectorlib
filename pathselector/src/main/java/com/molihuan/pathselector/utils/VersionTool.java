package com.molihuan.pathselector.utils;

import android.os.Build;

/**
 * @ClassName VersionTool
 * @Description 安卓版本判断工具
 * @Author molihuan
 * @Date 2022/7/1 14:50
 */

public class VersionTool {

    /**
     * 是否是 Android 13 及以上版本
     */
    public static boolean isAndroid13() {
        return Build.VERSION.SDK_INT >= 33;
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
    }

    /**
     * 是否是 Android 12 及以上版本
     */
    public static boolean isAndroid12() {
        return Build.VERSION.SDK_INT >= 31;
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
    }

    /**
     * 是否是 Android 11 及以上版本
     */
    public static boolean isAndroid11() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

    /**
     * 是否是 Android 10 及以上版本
     */
    public static boolean isAndroid10() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }


    /**
     * 是否是 Android 6.0 及以上版本
     */
    public static boolean isAndroid6() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


}