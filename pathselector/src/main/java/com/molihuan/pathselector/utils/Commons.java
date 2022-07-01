package com.molihuan.pathselector.utils;

import android.app.Application;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @ClassName Common
 * @Description TODO 公共的工具
 * @Author molihuan
 * @Date 2022/7/1 14:50
 */
public class Commons {
    //可能有版本问题

    /**
     * 通过反射获取Application(简单,但不推荐)
     * @return
     */
    public static Application getApplicationByReflectSimple() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }

    /**
     * 通过反射获取Application(推荐)
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
                return app == null ? null : (Application)app;
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
            return sCurrentActivityThreadField.get((Object)null);
        } catch (Exception var3) {
            Log.e("UtilsActivityLifecycle", "getActivityThreadInActivityThreadStaticField: " + var3.getMessage());
            return null;
        }
    }

    private static Object getActivityThreadInActivityThreadStaticMethod() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            return activityThreadClass.getMethod("currentActivityThread").invoke((Object)null);
        } catch (Exception var2) {
            Log.e("UtilsActivityLifecycle", "getActivityThreadInActivityThreadStaticMethod: " + var2.getMessage());
            return null;
        }
    }


}
