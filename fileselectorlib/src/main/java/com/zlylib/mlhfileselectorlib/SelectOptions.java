package com.zlylib.mlhfileselectorlib;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.zlylib.mlhfileselectorlib.adapter.FileListAdapter;
import com.zlylib.mlhfileselectorlib.bean.FileBean;
import com.zlylib.mlhfileselectorlib.fragment.ToolbarFragment;
import com.zlylib.mlhfileselectorlib.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * SelectOptions
 * Created by ZHANGLIYANG on 2020/6/20.
 * Updata by molihuan on 2022/6/5
 */

public class SelectOptions {


    public static final String defaultTargetPath = Environment.getExternalStorageDirectory() + "/";

    public String[] mFileTypes;
    public String mSortType;//排序类型
    public static boolean isSingle = false;//是否是单选
    public int maxCount = 10;//最多选的个数
    public Fragment toolbarFragment=null;//ToolbarFragment
    private boolean onlyShowFolder = false;//只显示文件夹
    private boolean needMoreOptions = false;//是否需要选项
    private boolean onlySelectFolder = false;//只选择文件夹
    public boolean onlyShowImages = false;//只显示图片
    public boolean onlyShowVideos = false;//只显示视频
    private String[] optionsName;
    private boolean[] optionsNeedCallBack;
    private IToolbarOptionsListener[] onOptionClicks;
    private IOnFileItemListener onFileItem;
    public int request_code;//返回码
    public String targetPath = defaultTargetPath;//设置默认目录

    private int titleBg = 0;//标题背景颜色
    private int titleColor = 0;//标题文字颜色
    private int titleLiftColor = 0;//标题左边颜色
    private int titleRightColor = 0;//标题右边颜色

    public IOnFileItemListener getOnFileItem() {
        return onFileItem;
    }

    public void setOnFileItem(IOnFileItemListener onFileItem) {
        this.onFileItem = onFileItem;
    }

    /**
     * 是否需要回调
     * @return
     */
    public boolean[] getOptionsNeedCallBack() {
        return optionsNeedCallBack;
    }

    public void setOptionsNeedCallBack(boolean[] optionsNeedCallBack) {
        this.optionsNeedCallBack = optionsNeedCallBack;
    }

    /**
     * 是否需要多个选项
     * @return
     */
    public boolean isNeedMoreOptions() {
        return needMoreOptions;
    }

    public void setNeedMoreOptions(boolean needMoreOptions) {
        this.needMoreOptions = needMoreOptions;
    }

    /**
     * 多个选项的名称
     * @return
     */
    public String[] getOptionsName() {
        return optionsName;
    }

    public void setOptionsName(String[] optionsName) {
        this.optionsName = optionsName;
    }

    /**
     * 选项回调监听
     * @return
     */
    public IToolbarOptionsListener[] getOnOptionClicks() {
        return onOptionClicks;
    }

    public void setOnOptionClicks(IToolbarOptionsListener[] onOptionClicks) {
        this.onOptionClicks = onOptionClicks;
    }

    /**
     * 获取自定义ToolbarFragment
     * @return
     */
    public Fragment getToolbarFragment() {
        if (toolbarFragment==null) {
            return new ToolbarFragment("请选择路径");
        }
        return toolbarFragment;
    }

    public void setToolbarFragment(Fragment toolbarFragment) {
        this.toolbarFragment = toolbarFragment;
    }

    /**
     * 文件类型
     * @return
     */
    public String[] getFileTypes() {
        if (mFileTypes == null || mFileTypes.length == 0) {
            return new String[]{};
        }
        return mFileTypes;
    }

    /**
     * 排序规则
     * @return
     */
    public int getSortType() {
        if (TextUtils.isEmpty(mSortType)) {
            return FileUtils.BY_NAME_ASC;
        }
        return Integer.valueOf(mSortType);
    }

    public void setSortType(int sortType) {
        mSortType = String.valueOf(sortType);
    }

    /**
     *默认路径
     * @return
     */
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

    /**
     * 只对于文件夹
     * @return
     */
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

    /**
     * 标题相关
     * @return
     */
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

    /**
     * 获取一个实例
     * @return
     */
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

    /**
     *回调接口
     */
    public interface IToolbarOptionsListener {
        /**
         *
         * @param context
         * @param position 选择的option
         * @param currentPath 当前路径
         * @param selectedFileList 选择的文件列表
         * @param selectedFilePathList 选择的文件路径列表
         * @param adapter 文件列表适配器
         */
        void onOptionClick(Context context, int position, String currentPath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter);
    }
    public interface IOnFileItemListener {
        /**
         *
         * @param context
         * @param position 选择的option
         * @param fileAbsolutePath 文件路径
         * @param selectedFileList 选择的文件列表
         * @param selectedFilePathList 选择的文件路径列表
         * @param adapter 文件列表适配器
         */
        void onFileItemClick(Context context, int position, String fileAbsolutePath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter);
        void onLongFileItemClick(Context context, int position, String fileAbsolutePath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter);
    }

    private static final class InstanceHolder {
        private static final SelectOptions INSTANCE = new SelectOptions();
    }

}
