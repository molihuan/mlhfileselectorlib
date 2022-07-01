package com.zlylib.mlhfileselectorlib;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zlylib.mlhfileselectorlib.activity.FileSelectorActivity;
import com.zlylib.mlhfileselectorlib.adapter.FileListAdapter;
import com.zlylib.mlhfileselectorlib.bean.FileBean;
import com.zlylib.mlhfileselectorlib.fragment.MoreChooseFragment;
import com.zlylib.mlhfileselectorlib.fragment.ToolbarFragment;
import com.zlylib.mlhfileselectorlib.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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
    public Fragment moreChooseFragment=null;//MoreChooseFragment
    private boolean onlyShowFolder = false;//只显示文件夹
    private boolean needMoreOptions = false;//是否需要选项
    private boolean needMoreChoose = false;//是否需要选项
    private boolean onlySelectFolder = false;//只选择文件夹
    public boolean onlyShowImages = false;//只显示图片
    public boolean onlyShowVideos = false;//只显示视频
    private String[] optionsName;
    private String[] MoreChooseItemName;
    private boolean[] optionsNeedCallBack;
    private boolean[] moreChooseItemNeedCallBack;
    private IToolbarOptionsListener[] onOptionClicks;
    private IMoreChooseItemsListener[] onItemClicks;
    private IOnFileItemListener onFileItem;
    public int request_code;//返回码
    public String targetPath;//设置默认目录
    public String toolbarTitle;//默认Title
    private Context context;

    private int toolbarColor;//标题背景颜色
    private int titleColor;//标题文字颜色
    private int OneOptionColor;//一个选项颜色
    private int OneOptionSize;//一个选项大小

    public int getOneOptionColor() {
        return OneOptionColor;
    }

    public void setOneOptionColor(int oneOptionColor) {
        OneOptionColor = oneOptionColor;
    }

    public int getOneOptionSize() {
        return OneOptionSize;
    }

    public void setOneOptionSize(int oneOptionSize) {
        OneOptionSize = oneOptionSize;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 设置标题
     * @return
     */
    public String getToolbarTitle() {
        return toolbarTitle;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
    }

    /**
     * FileItem监听器
     * @return
     */

    public IOnFileItemListener getOnFileItem() {
        return onFileItem;
    }

    public void setOnFileItem(IOnFileItemListener onFileItem) {
        this.onFileItem = onFileItem;
    }



    /**
     * option是否需要回调
     * @return
     */
    public boolean[] getOptionsNeedCallBack() {
        return optionsNeedCallBack;
    }

    public void setOptionsNeedCallBack(boolean[] optionsNeedCallBack) {
        this.optionsNeedCallBack = optionsNeedCallBack;
    }
    /**
     * 多选是否需要回调
     * @return
     */
    public boolean[] getMoreChooseItemNeedCallBack() {
        return moreChooseItemNeedCallBack;
    }

    public void setMoreChooseItemNeedCallBack(boolean[] moreChooseItemNeedCallBack) {
        this.moreChooseItemNeedCallBack = moreChooseItemNeedCallBack;
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
     * 是否需要多选
     * @return
     */
    public boolean isNeedMoreChoose() {
        return needMoreChoose;
    }

    public void setNeedMoreChoose(boolean needMoreChoose) {
        this.needMoreChoose = needMoreChoose;
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
     * 多选item的名称
     * @return
     */
    public String[] getMoreChooseItemName() {
        return MoreChooseItemName;
    }

    public void setMoreChooseItemName(String[] MoreChooseItemName) {
        this.MoreChooseItemName = MoreChooseItemName;
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
     * 多选item选项回调监听
     * @return
     */
    public IMoreChooseItemsListener[] getOnItemClicks() {
        return onItemClicks;
    }

    public void setOnItemClicks(IMoreChooseItemsListener[] onItemClicks) {
        this.onItemClicks = onItemClicks;
    }

    /**
     * 获取自定义ToolbarFragment
     * @return
     */
    public Fragment getToolbarFragment() {
        if (toolbarFragment==null) {
            return new ToolbarFragment(toolbarTitle)
                        .setToolbarColor(toolbarColor)
                        .setMainTitleColor(titleColor)
                        .setTextRColor(OneOptionColor)
                        .setTextRSize(OneOptionSize)
                    ;
        }
        return toolbarFragment;
    }




    public void setToolbarFragment(Fragment toolbarFragment) {
        this.toolbarFragment = toolbarFragment;
    }
    /**
     * 获取自定义MoreChooseFragment
     * @return
     */
    public Fragment getMoreChooseFragment() {
        if (moreChooseFragment ==null) {
            return (new MoreChooseFragment(Arrays.asList(MoreChooseItemName)));
        }
        return moreChooseFragment;
    }

    public void setMoreChooseFragment(Fragment moreChooseFragment) {
        this.moreChooseFragment = moreChooseFragment;
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
    public int getToolbarColor() {
        return toolbarColor;
    }

    public void setToolbarColor(int titleBg) {
        this.toolbarColor = titleBg;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    /**
     * 获取一个实例
     * @return
     */
    public static SelectOptions getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static SelectOptions getCleanInstance(Context context) {
        SelectOptions options = getInstance();
        options.setContext(context);
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
        toolbarColor =ContextCompat.getColor(context,R.color.Themecolors_orange);//标题背景颜色
        titleColor = Color.WHITE ;;//标题文字颜色
        toolbarFragment=null;//ToolbarFragment
        needMoreOptions = false;//是否需要选项
        onlyShowImages = false;//只显示图片
        onlyShowVideos = false;//只显示视频
        optionsName=null;
        optionsNeedCallBack=null;
        onOptionClicks=null;
        onFileItem=null;
        toolbarTitle = "请选择路径";//默认Title
        OneOptionColor=Color.WHITE;//一个选项颜色
        OneOptionSize=18;//一个选项大小
    }

    /**
     *更多选项回调接口
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
        void onOptionClick(View view,Context context, int position, String currentPath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter);
    }
    /**
     *多选回调接口
     */
    public interface IMoreChooseItemsListener {
        /**
         *@param view 点击的控件
         * @param activity
         * @param position 选择的option
         * @param currentPath 当前路径
         * @param selectedFileList 选择的文件列表
         * @param selectedFilePathList 选择的文件路径列表
         * @param adapter 文件列表适配器
         */
        void onItemsClick(View view, FileSelectorActivity activity, int position, String currentPath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter);
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
        void onFileItemClick(View view,Context context, int position, String fileAbsolutePath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter);
        void onLongFileItemClick(View view,Context context, int position, String fileAbsolutePath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter);
    }

    private static final class InstanceHolder {
        private static final SelectOptions INSTANCE = new SelectOptions();
    }

}
