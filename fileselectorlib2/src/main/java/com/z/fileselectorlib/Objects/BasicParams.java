package com.z.fileselectorlib.Objects;

import static com.z.fileselectorlib.Objects.FileInfo.FileType.Audio;
import static com.z.fileselectorlib.Objects.FileInfo.FileType.Folder;
import static com.z.fileselectorlib.Objects.FileInfo.FileType.Image;
import static com.z.fileselectorlib.Objects.FileInfo.FileType.Unknown;
import static com.z.fileselectorlib.Objects.FileInfo.FileType.Video;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasicParams {
    public static final String BasicPath=Environment.getExternalStorageDirectory().getAbsolutePath();
    private String RootPath;
    private int MaxSelectNum;
    private String tips;
    private String color;
    private FileInfo.FileType[] selectableFileTypes;
    private boolean needMoreOptions;
    private String[] OptionsName;
    private OnOptionClick[] onOptionClicks;
    private String[] fileTypeFilter;
    private boolean useFilter;
    private Map<String, Bitmap> customIcon = new HashMap<>();


    public String getRootPath() {
        return RootPath;
    }

    public void setRootPath(String rootPath) {
        RootPath = rootPath;
    }

    public int getMaxSelectNum() {
        return MaxSelectNum;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        MaxSelectNum = maxSelectNum;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSelectableFileTypes(FileInfo.FileType... selectableFileTypes) {
        this.selectableFileTypes = selectableFileTypes;
    }

    public FileInfo.FileType[] getSelectableFileTypes() {
        return selectableFileTypes;
    }

    public boolean isNeedMoreOptions() {
        return needMoreOptions;
    }

    public void setNeedMoreOptions(boolean needMoreOptions) {
        this.needMoreOptions = needMoreOptions;
    }

    public String[] getOptionsName() {
        return OptionsName;
    }

    public void setOptionsName(String[] optionsName) {
        OptionsName = optionsName;
    }

    public OnOptionClick[] getOnOptionClicks() {
        return onOptionClicks;
    }

    public void setOnOptionClicks(OnOptionClick[] onOptionClicks) {
        this.onOptionClicks = onOptionClicks;
    }

    public static String getBasicPath() {
        return BasicPath;
    }

    public String[] getFileTypeFilter() {
        return fileTypeFilter;
    }

    public void setFileTypeFilter(String[] fileTypeFilter) {
        this.fileTypeFilter = fileTypeFilter;
    }

    public boolean isUseFilter() {
        return useFilter;
    }

    public void setUseFilter(boolean useFilter) {
        this.useFilter = useFilter;
    }

    public Map<String, Bitmap> getCustomIcon() {
        return customIcon;
    }

    public void addCustomIcon(String extension,Bitmap icon) {
        customIcon.put(extension,icon);
    }

    public static BasicParams getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static BasicParams getInitInstance() {
        BasicParams params= InstanceHolder.INSTANCE;
        params.setRootPath(BasicPath);
        params.setMaxSelectNum(1);
        params.setTips("请选择文件");
        params.setColor("#1E90FF");
        params.setSelectableFileTypes(Folder,Video,Audio,Image,Unknown);
        params.setNeedMoreOptions(false);
        params.setUseFilter(false);
        return params;
    }

    private static final class InstanceHolder {
        private static final BasicParams INSTANCE = new BasicParams();
    }

    public interface OnOptionClick{
        void onclick(Activity activity, int position, String currentPath, ArrayList<String> FilePathSelected);
    }
}
