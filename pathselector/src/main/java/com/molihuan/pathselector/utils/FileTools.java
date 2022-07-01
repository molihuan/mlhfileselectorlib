package com.molihuan.pathselector.utils;

import android.content.Context;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName FileTools
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/24 13:27
 */
public class FileTools {
    /**
     * 根据文件路径获取文件
     * @param path 绝对路径
     * @return
     */
    public static File getFileByPath(String path){
        return StringTools.isEmpty(path) ? null : new File(path);
    }

    /**
     * 判断文件是否存在
     * @param file
     * @return
     */
    public static boolean isFileExists(File file) {
        if (file == null) {
            return false;
        } else {
            return file.exists() ? true : isFileExists(file.getAbsolutePath());
        }
    }

    public static boolean isFileExists(String filePath) {
        File file = getFileByPath(filePath);
        if (file == null) {
            return false;
        } else {
            return file.exists() ? true : false;
        }
    }

    /**
     * 重命名文件
     * @param filePath
     * @param newName
     * @return
     */
    public static boolean rename(String filePath, String newName) {
        return rename(getFileByPath(filePath), newName);
    }

    public static boolean rename(File file, String newName) {
        if (file == null) {
            return false;
        } else if (!file.exists()) {
            return false;
        } else if (StringTools.isEmpty(newName)) {
            return false;
        } else if (newName.equals(file.getName())) {
            return true;
        } else {
            File newFile = new File(file.getParent() + File.separator + newName);
            return !newFile.exists() && file.renameTo(newFile);
        }
    }

    /**
     * 获取子文件、文件夹数量
     * @param path
     * @return numbers[0]为文件数量、numbers[1]为文件夹数量
     */
    public static int[] getChildrenNumber(String path) {
        return getChildrenNumber(getFileByPath(path));
    }
    public static int[] getChildrenNumber(File file) {
        File[] files = file.listFiles();
        int [] numbers=new int[]{0,0};
        if (files!=null){
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    numbers[0]++;
                }else {
                    numbers[1]++;
                }
            }
        }
        return numbers;
    }

    /**
     *判断是否是目录
     * @param dirPath
     * @return
     */
    public static boolean isDir(String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    public static boolean isDir(File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    /**
     * 判断是否是文件
     * @param filePath
     * @return
     */
    public static boolean isFile(String filePath) {
        return isFile(getFileByPath(filePath));
    }

    public static boolean isFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    /**
     * 根据绝对路径获取上一级文件夹名
     * @param file
     * @return
     */
    public static String getDirName(File file) {
        return file == null ? "" : getDirName(file.getAbsolutePath());
    }

    public static String getDirName(String filePath) {
        if (StringTools.isEmpty(filePath)) {
            return "";
        } else {
            int lastSep1 = filePath.lastIndexOf(File.separator);
            if (lastSep1==-1){
                return "";
            }else {
                int lastSep2 = filePath.substring(0, lastSep1).lastIndexOf(File.separator);
                return filePath.substring(lastSep2+1, lastSep1);
            }
        }
    }

    /**
     * 根据绝对路径获取上一级绝对路径
     * @param file
     * @return
     */
    public static String getParentPath(File file) {
        return file == null ? "" : getParentPath(file.getAbsolutePath());
    }

    public static String getParentPath(String filePath) {
        if (StringTools.isEmpty(filePath)) {
            return "";
        } else {
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastSep == -1 ? "" : filePath.substring(0, lastSep);
        }
    }

    /**
     * 根据绝对路径获取文件名(有后缀名)
     * @param file
     * @return
     */
    public static String getFileName(File file) {
        return file == null ? "" : getFileName(file.getAbsolutePath());
    }

    public static String getFileName(String filePath) {
        if (StringTools.isEmpty(filePath)) {
            return "";
        } else {
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
        }
    }

    /**
     * 根据绝对路径获取文件名不带拓展名
     * @param file
     * @return
     */
    public static String getFileNameNoExtension(File file) {
        return file == null ? "" : getFileNameNoExtension(file.getPath());
    }

    public static String getFileNameNoExtension(String filePath) {
        if (StringTools.isEmpty(filePath)) {
            return "";
        } else {
            int lastPoi = filePath.lastIndexOf(46);
            int lastSep = filePath.lastIndexOf(File.separator);
            if (lastSep == -1) {
                return lastPoi == -1 ? filePath : filePath.substring(0, lastPoi);
            } else {
                return lastPoi != -1 && lastSep <= lastPoi ? filePath.substring(lastSep + 1, lastPoi) : filePath.substring(lastSep + 1);
            }
        }
    }

    /**
     * 根据绝对路径获取文件拓展名
     * @param file
     * @return
     */
    public static String getFileExtension(File file) {
        return file == null ? "" : getFileExtension(file.getPath());
    }

    public static String getFileExtension(String filePath) {
        if (StringTools.isEmpty(filePath)) {
            return "";
        } else {
            int lastPoi = filePath.lastIndexOf(46);
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastPoi != -1 && lastSep < lastPoi ? filePath.substring(lastPoi + 1) : "";
        }
    }

    /**
     * 获取文件最后修改的毫秒时间戳
     * @param filePath
     * @return
     */
    public static long getFileLastModified(String filePath) {
        return getFileLastModified(getFileByPath(filePath));
    }

    public static long getFileLastModified(File file) {
        return file == null ? -1L : file.lastModified();
    }

    /**
     * 获取获取文件或目录大小
     * @param filePath
     * @return
     */
    //*********************       开始获取获取文件或目录大小             *******************************//
    public static Long getSimpleSize(String filePath) {
        return getSimpleSize(getFileByPath(filePath));
    }
    public static Long getSimpleSize(File file) {
        return StringTools.getOnlyNumber(getSize(file));
    }

    public static String getSize(String filePath) {
        return getSize(getFileByPath(filePath));
    }

    public static String getSize(File file) {
        if (file == null) {
            return "";
        } else {
            return file.isDirectory() ? getDirSize(file) : getFileSize(file);
        }
    }

    private static String getDirSize(File dir) {
        long len = getDirLength(dir);
        return len == -1L ? "" : byte2FitMemorySize(len,3);
    }

    private static String getFileSize(File file) {
        long len = getFileLength(file);
        return len == -1L ? "" : byte2FitMemorySize(len,3);
    }

    private static long getFileLength(File file) {
        return !isFile(file) ? -1L : file.length();
    }

    private static long getDirLength(File dir) {
        if (!isDir(dir)) {
            return 0L;
        } else {
            long len = 0L;
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                File[] var4 = files;
                int var5 = files.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    File file = var4[var6];
                    if (file.isDirectory()) {
                        len += getDirLength(file);
                    } else {
                        len += file.length();
                    }
                }
            }

            return len;
        }
    }

    public static String byte2FitMemorySize(long byteSize, int precision) {
        if (precision < 0) {
            throw new IllegalArgumentException("precision shouldn't be less than zero!");
        } else if (byteSize < 0L) {
            throw new IllegalArgumentException("byteSize shouldn't be less than zero!");
        } else if (byteSize < 1024L) {
            return String.format("%." + precision + "fB", (double)byteSize);
        } else if (byteSize < 1048576L) {
            return String.format("%." + precision + "fKB", (double)byteSize / 1024.0D);
        } else {
            return byteSize < 1073741824L ? String.format("%." + precision + "fMB", (double)byteSize / 1048576.0D) : String.format("%." + precision + "fGB", (double)byteSize / 1.073741824E9D);
        }
    }

    //*********************       结束获取获取文件或目录大小             *******************************//

    /**
     *
     * @param file
     * @return
     */
    public static boolean createOrExistsDir(File file) {
        boolean var10000;
        label25: {
            if (file != null) {
                if (file.exists()) {
                    if (file.isDirectory()) {
                        break label25;
                    }
                } else if (file.mkdirs()) {
                    break label25;
                }
            }

            var10000 = false;
            return var10000;
        }

        var10000 = true;
        return var10000;
    }

    public static boolean createOrExistsFile(File file) {
        if (file == null) {
            return false;
        } else if (file.exists()) {
            return file.isFile();
        } else if (!createOrExistsDir(file.getParentFile())) {
            return false;
        } else {
            try {
                return file.createNewFile();
            } catch (IOException var2) {
                var2.printStackTrace();
                return false;
            }
        }
    }




    /**
     * 通过流的形式写入文件
     * @param file
     * @param is
     * @param append
     * @return
     */
    private static int sBufferSize = 524288;
    public static boolean writeFileFromIS(File file, InputStream is, boolean append) {
        if (is != null && createOrExistsFile(file)) {
            BufferedOutputStream os = null;

            boolean var6;
            try {
                os = new BufferedOutputStream(new FileOutputStream(file, append), sBufferSize);

                double totalSize = (double)is.available();
                int curSize = 0;
                byte[] data = new byte[sBufferSize];

                int len;
                while((len = is.read(data)) != -1) {
                    os.write(data, 0, len);
                    curSize += len;

                }

                boolean var25 = true;
                return var25;
            } catch (IOException var22) {
                var22.printStackTrace();
                var6 = false;
            } finally {
                try {
                    is.close();
                } catch (IOException var21) {
                    var21.printStackTrace();
                }

                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException var20) {
                    var20.printStackTrace();
                }

            }

            return var6;
        } else {
            Log.e("FileIOUtils", "create file <" + file + "> failed.");
            return false;
        }
    }

    /**
     * 判断是否在/storage/emulated/0/Android/data目录下
     * @param path
     * @return
     */
    public static boolean isAndroidDataPath(String path){
        return path.matches(Constants.PATH_ANRROID_DATA+"(.*)") ? true : false;
    }


    /**
     * 得到所有的存储路径（内部存储+外部存储）
     * 反射的方式
     * @param context context
     * @return 路径列表
     */
    public static List<String> getAllSdPaths(Context context) {
        Method mMethodGetPaths = null;
        String[] paths = null;
        //通过调用类的实例mStorageManager的getClass()获取StorageManager类对应的Class对象
        //getMethod("getVolumePaths")返回StorageManager类对应的Class对象的getVolumePaths方法，这里不带参数
        StorageManager mStorageManager = (StorageManager) context
                .getSystemService(context.STORAGE_SERVICE);//storage
        try {
            mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (paths != null) {
            return Arrays.asList(paths);
        }
        return new ArrayList<String>();
    }

}
