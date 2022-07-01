package com.molihuan.pathselector.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * @ClassName MToast
 * @Description TODO 调试工具类
 * @Author molihuan
 * @Date 2022/6/25 20:44
 */
public class Mtools {
    /**
     * 吐司工具
     * @param context
     * @param text
     */
    public static void toast(Context context,String text){
        toast(context,text, Toast.LENGTH_SHORT);
    }
    public static void toast(Context context,String text,int time){
        Toast.makeText(context,text,time).show();
    }

    public static void log(String text){
        log(text,0);
    }
    public static void log(Object text){
        log(String.valueOf(text),0);
    }

    public static void log(String text,int type){
        switch (type){
            case 0:
                Log.e("Mtools--->log--E",text);
                break;
        }
    }



}
