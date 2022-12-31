package com.molihuan.pathselector.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * @ClassName MToast
 * @Description 简单调试工具类
 * @Author molihuan
 * @Date 2022/6/25 20:44
 */
public class Mtools {

    private static boolean debug = true;

    private static final int LOG_TYPE_E = 0;
    private static final int LOG_TYPE_D = 1;
    private static final int LOG_TYPE_I = 2;

    public static void setDebug(boolean debug) {
        Mtools.debug = debug;
    }

    /**
     * 吐司工具
     *
     * @param context
     * @param text
     */
    public static void toast(Context context, String text) {
        toast(context, text, Toast.LENGTH_SHORT);
    }

    public static void toast(CharSequence text) {
        toast(text, Toast.LENGTH_SHORT);
    }
    
    public static void toast(CharSequence text, int time) {
        toast(ReflectTools.getApplicationByReflect(), text, time);
    }

    public static void toast(Context context, CharSequence text, int time) {
//        if (!debug) {
//            return;
//        }
        Toast.makeText(context, text, time).show();
    }

    /**
     * 日志工具
     *
     * @param text
     */
    public static void log(String text) {
        log(text, LOG_TYPE_E);
    }

    public static void log(Object obj) {
        String s = null;
        try {
            s = obj.toString();
        } catch (Exception e1) {
            Log.e("Mtools--->log--E", "未重写toString方法");
            try {
                s = String.valueOf(obj);
            } catch (Exception e2) {
                Log.e("Mtools--->log--E", "无法打印");
            }
        }
        log(s, LOG_TYPE_E);
    }

    public static void log(String text, int type) {
        if (!debug) {
            return;
        }
        switch (type) {
            case LOG_TYPE_E:
                Log.e("Mtools--->log--E", text);
                break;
            case LOG_TYPE_D:
                Log.d("Mtools--->log--d", text);
                break;
            default:
                Log.i("Mtools--->log--i", text);
        }
    }


}
