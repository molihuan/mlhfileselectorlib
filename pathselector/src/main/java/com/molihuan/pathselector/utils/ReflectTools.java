package com.molihuan.pathselector.utils;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
    public static List<String> getAllStoragePathByReflect(Context context) {
        Method mMethodGetPaths = null;
        String[] paths = null;
        //通过调用类的实例mStorageManager的getClass()获取StorageManager类对应的Class对象
        //getMethod("getVolumePaths")返回StorageManager类对应的Class对象的getVolumePaths方法，这里不带参数
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (paths != null) {
            return CommonTools.asArrayList(paths);
        }
        return new ArrayList<>();
    }

    /**
     * 获取所有的存储路径（内部存储+外部存储）
     *
     * @param context
     * @return
     */
    public static List<String> getAllStoragePath(Context context) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R) {
            //通过反射来获取
            return getAllStoragePathByReflect(context);
        }

        StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);

        List<StorageVolume> volume = sm.getStorageVolumes();
        List<String> resultPath = new ArrayList<>();

        for (int i = 0; i < volume.size(); i++) {
            File directory = volume.get(i).getDirectory();
            if (directory == null) {
                //通过反射来获取
                return getAllStoragePathByReflect(context);
            }
            resultPath.add(directory.getAbsolutePath());
        }
        return resultPath;
    }

    /**
     * 通过反射获取StorageVolume
     *
     * @param context
     * @return
     */
    public static List<StorageVolume> getStorageVolumesByReflect(Context context) {
        Method mMethodGetPaths = null;
        StorageVolume[] paths = null;
        //通过调用类的实例mStorageManager的getClass()获取StorageManager类对应的Class对象
        //getMethod("getVolumePaths")返回StorageManager类对应的Class对象的getVolumePaths方法，这里不带参数
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumeList");
            paths = (StorageVolume[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (paths != null) {
            return CommonTools.asArrayList(paths);
        }
        return new ArrayList<>();
    }

    /**
     * 获取StorageVolume
     *
     * @param context
     * @return
     */
    public static List<StorageVolume> getStorageVolumes(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //通过反射来获取
            return getStorageVolumesByReflect(context);
        }

        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);

        return mStorageManager.getStorageVolumes();

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
