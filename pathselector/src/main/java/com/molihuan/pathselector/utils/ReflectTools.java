package com.molihuan.pathselector.utils;

import android.app.Application;
import android.content.Context;
import android.os.storage.StorageManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @ClassName ReflectTool
 * @Description 反射工具 不能被混淆
 * @Author molihuan
 * @Date 2022/7/1 14:50
 */
public class ReflectTools {

    /**
     * 通过反射的方式得到所有的存储路径（内部存储+外部存储）
     *
     * @param context context
     * @return 路径列表 没有则返回null
     */
    public static List<String> getAllStoragePath(Context context) {
        Method mMethodGetPaths = null;
        String[] paths = null;
        //通过调用类的实例mStorageManager的getClass()获取StorageManager类对应的Class对象
        //getMethod("getVolumePaths")返回StorageManager类对应的Class对象的getVolumePaths方法，这里不带参数
        StorageManager mStorageManager = (StorageManager) context.getSystemService(context.STORAGE_SERVICE);
        try {
            mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (paths != null) {
            return CommonTools.asArrayList(paths);
        }
        return null;
    }

    /**
     * 通过反射获取Application
     *
     * @return
     */
    public static Application getApplicationByReflect() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object thread = getActivityThread();
            if (thread == null) {
                return null;
            } else {
                Object app = activityThreadClass.getMethod("getApplication").invoke(thread);
                return app == null ? null : (Application) app;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    private static Object getActivityThread() {
        Object activityThread = getActivityThreadInActivityThreadStaticField();
        return activityThread != null ? activityThread : getActivityThreadInActivityThreadStaticMethod();
    }

    private static Object getActivityThreadInActivityThreadStaticField() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            return sCurrentActivityThreadField.get((Object) null);
        } catch (Exception var3) {
            Log.e("UtilsActivityLifecycle", "getActivityThreadInActivityThreadStaticField: " + var3.getMessage());
            return null;
        }
    }

    private static Object getActivityThreadInActivityThreadStaticMethod() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            return activityThreadClass.getMethod("currentActivityThread").invoke((Object) null);
        } catch (Exception var2) {
            Log.e("UtilsActivityLifecycle", "getActivityThreadInActivityThreadStaticMethod: " + var2.getMessage());
            return null;
        }
    }


}
