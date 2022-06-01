package com.zlylib.mlhfileselectorlib;

import android.os.Environment;
import android.text.TextUtils;

import com.zlylib.mlhfileselectorlib.utils.FileUtils;

import java.io.File;

/**
 * SelectOptions
 * Created by ZHANGLIYANG on 2020/6/20.
 */

public class SelectOptions {


    public static final String defaultTargetPath = Environment.getExternalStorageDirectory() + "/";

    public String[] mFileTypes;
    public String mSortType;//排序类型
    public static boolean isSingle = false;//是否是单选
    public int maxCount = 10;//最多选的个数
    private boolean onlyShowFolder = false;//只显示文件夹
    private boolean onlySelectFolder = false;//只选择文件夹
    //public boolean onlyShowImages = false;//只显示图片
    //public boolean onlyShowVideos = false;//只显示视频
    public int request_code;//返回码
    public String targetPath = defaultTargetPath;//设置默认目录

    private int titleBg = 0;//标题背景颜色
    private int titleColor = 0;//标题文字颜色
    private int titleLiftColor = 0;//标题左边颜色
    private int titleRightColor = 0;//标题右边颜色


    public String[] getFileTypes() {
        if (mFileTypes == null || mFileTypes.length == 0) {
            return new String[]{};
        }
        return mFileTypes;
    }

    public int getSortType() {
        if (TextUtils.isEmpty(mSortType)) {
            return FileUtils.BY_NAME_ASC;
        }
        return Integer.valueOf(mSortType);
    }

    public void setSortType(int sortType) {
        mSortType = String.valueOf(sortType);
    }

    public String getTargetPath() {
        if (!new File(targetPath).exists()) {
            File file = new File(defaultTargetPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            return defaultTargetPath;
        }
        return targetPath;
    }

    public boolean isOnlyShowFolder() {
        return onlyShowFolder;
    }

    public void setOnlyShowFolder(boolean onlyShowFolder) {
        this.onlyShowFolder = onlyShowFolder;
    }

    public boolean isOnlySelectFolder() {
        return onlySelectFolder;
    }

    public void setOnlySelectFolder(boolean onlySelectFolder) {
        this.onlySelectFolder = onlySelectFolder;
    }

    public int getTitleBg() {
        return titleBg;
    }

    public void setTitleBg(int titleBg) {
        this.titleBg = titleBg;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public int getTitleLiftColor() {
        return titleLiftColor;
    }

    public void setTitleLiftColor(int titleLiftColor) {
        this.titleLiftColor = titleLiftColor;
    }

    public int getTitleRightColor() {
        return titleRightColor;
    }

    public void setTitleRightColor(int titleRightColor) {
        this.titleRightColor = titleRightColor;
    }

    public static SelectOptions getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static SelectOptions getCleanInstance() {
        SelectOptions options = getInstance();
        options.reset();
        return options;
    }

    private void reset() {
        mFileTypes = new String[]{};
        mSortType = String.valueOf(FileUtils.BY_NAME_ASC);
        isSingle = false;
        maxCount = 10;
        onlyShowFolder = false;
        onlySelectFolder = false;
        targetPath = defaultTargetPath;
        titleBg = 0;//标题背景颜色
        titleColor = 0;//标题文字颜色
        titleLiftColor = 0;//标题左边颜色
        titleRightColor = 0;//标题右边颜色

    }

    private static final class InstanceHolder {
        private static final SelectOptions INSTANCE = new SelectOptions();
    }

}
