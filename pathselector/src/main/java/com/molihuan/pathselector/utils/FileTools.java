package com.molihuan.pathselector.utils;

import androidx.documentfile.provider.DocumentFile;

import com.blankj.molihuan.utilcode.util.ConvertUtils;
import com.blankj.molihuan.utilcode.util.FileUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: FileTools
 * @Author: molihuan
 * @Date: 2022/12/03/10:15
 * @Description:
 */
public class FileTools {

    public static final String ERROR_GETTING_FILE_SIZE = "-1b";


    /**
     * 只计算文件的大小(不能计算文件夹)
     *
     * @return
     */
    public static String computeFileSize(File file) {
        if (!file.exists() || file.isDirectory()) {
            return ERROR_GETTING_FILE_SIZE;
        }
        //根据byte计算文件大小-->B  KB  MB  GB
        return ConvertUtils.byte2FitMemorySize(file.length(), 2);
    }

    /**
     * 根据绝对路径获取文件拓展名
     *
     * @param file
     * @return
     */
    public static String getFileExtension(File file) {
        return file == null ? "" : getFileExtension(file.getPath());
    }

    public static String getFileExtension(String filePath) {
        if (stringIsEmpty(filePath)) {
            return "";
        } else {
            int lastPoi = filePath.lastIndexOf(46);
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastPoi != -1 && lastSep < lastPoi ? filePath.substring(lastPoi + 1) : "";
        }
    }

    /**
     * 根据绝对路径获取上一级绝对路径
     *
     * @param file
     * @return
     */
    public static String getParentPath(File file) {
        return file == null ? "" : getParentPath(file.getAbsolutePath());
    }

    public static String getParentPath(String filePath) {
        if (stringIsEmpty(filePath)) {
            return "";
        } else {
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastSep == -1 ? "" : filePath.substring(0, lastSep);
        }
    }

    /**
     * 是否需要使用uri
     *
     * @return
     */
    public static boolean needUseUri(String path) {
        return underAndroidDataUseUri(path) || underAndroidObbUseUri(path);
    }

    /**
     * 路径是否是Android/data或其子目录
     *
     * @param path
     * @return
     */
    public static boolean underAndroidDataUseUri(String path) {
        //判断是否是安卓11以及以上
        if (!VersionTool.isAndroid11()) {
            return false;
        }
        //判断是否在Android/data目录下
        if (!isUnderDir(path, MConstants.TYPE_UNDERDIR_ANRROID_DATA)) {
            return false;
        }
        return true;
    }

    /**
     * 路径是否是Android/obb或其子目录
     *
     * @param path
     * @return
     */
    public static boolean underAndroidObbUseUri(String path) {
        //判断是否是安卓11以及以上
        if (!VersionTool.isAndroid11()) {
            return false;
        }
        //判断是否在Android/obb目录下
        if (!isUnderDir(path, MConstants.TYPE_UNDERDIR_ANRROID_OBB)) {
            return false;
        }
        return true;
    }

    /**
     * 是否在目录下
     * 是否在Android/data/和Android/obb/下
     *
     * @param targetPath
     * @return
     */
    private static boolean isUnderDir(String targetPath, String parentPath, int type) {
        switch (type) {
            case MConstants.TYPE_UNDERDIR_ANRROID_DATA:
                parentPath = MConstants.PATH_ANRROID_DATA;
                break;
            case MConstants.TYPE_UNDERDIR_ANRROID_OBB:
                parentPath = MConstants.PATH_ANRROID_OBB;
                break;
            default:
        }
        return targetPath.startsWith(parentPath);
    }

    public static boolean isUnderDir(String targetPath, String parentPath) {
        return isUnderDir(targetPath, parentPath, 0);
    }

    public static boolean isUnderDir(String targetPath, int type) {
        return isUnderDir(targetPath, null, type);
    }

    /**
     * 判断选择的是否符合要求
     *
     * @param selectType
     * @param selectFileTypes
     * @return
     */
    public static boolean selectTypeCompliance(String selectType, List<String> selectFileTypes) {
        if (selectFileTypes == null || selectFileTypes.size() == 0 || selectFileTypes.contains(selectType)) {
            return true;
        }
        return false;
    }

    /**
     * 获取子文件、文件夹数量
     *
     * @param path
     * @return numbers[0]为文件数量、numbers[1]为文件夹数量
     */
    public static int[] getChildrenNumber(String path) {
        File file = FileUtils.getFileByPath(path);
        Objects.requireNonNull(file, "FileTools.getChildrenNumber的file为null");
        return getChildrenNumber(file);
    }

    public static int[] getChildrenNumber(File file) {
        File[] files = file.listFiles();
        int[] numbers = new int[]{0, 0};
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    numbers[0]++;
                } else {
                    numbers[1]++;
                }
            }
        }
        return numbers;
    }

    public static int[] getChildrenNumber(DocumentFile file) {
        DocumentFile[] files = file.listFiles();
        int[] numbers = new int[]{0, 0};
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    numbers[0]++;
                } else {
                    numbers[1]++;
                }
            }
        }
        return numbers;
    }

    /**
     * 根据绝对路径获取文件名(有后缀名)
     *
     * @param file
     * @return
     */
    public static String getFileName(File file) {
        return file == null ? "" : getFileName(file.getAbsolutePath());
    }

    public static String getFileName(String filePath) {
        if (stringIsEmpty(filePath)) {
            return "";
        } else {
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
        }
    }

    public static boolean stringIsEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

}
