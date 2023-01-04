package com.molihuan.pathselector.service.impl;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.molihuan.pathselector.controller.AbstractBuildController;
import com.molihuan.pathselector.controller.AbstractFileBeanController;
import com.molihuan.pathselector.controller.impl.ActivityController;
import com.molihuan.pathselector.controller.impl.DialogController;
import com.molihuan.pathselector.controller.impl.FragmentController;
import com.molihuan.pathselector.dao.SelectConfigData;
import com.molihuan.pathselector.entity.FontBean;
import com.molihuan.pathselector.fragment.AbstractFileShowFragment;
import com.molihuan.pathselector.fragment.AbstractHandleFragment;
import com.molihuan.pathselector.fragment.AbstractTabbarFragment;
import com.molihuan.pathselector.fragment.AbstractTitlebarFragment;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.listener.CommonItemListener;
import com.molihuan.pathselector.listener.FileItemListener;
import com.molihuan.pathselector.service.IConfigDataBuilder;
import com.molihuan.pathselector.utils.MConstants;

import java.io.File;


/**
 * @ClassName: ConfigDataBuilder
 * @Author: molihuan
 * @Date: 2022/11/22/23:28
 * @Description: 真实的配置数据建造者(配置数据的设置)
 */

public class ConfigDataBuilderImpl implements IConfigDataBuilder {

    private final SelectConfigData mConfigData = new SelectConfigData();

    public SelectConfigData getSelectConfigData() {
        return mConfigData;
    }

    protected void init(Context context) {
        mConfigData.initDefaultConfig(context);
    }

    @Override
    public IConfigDataBuilder setContext(Context context) {
        init(context);
        return this;
    }

    @Override
    public IConfigDataBuilder setBuildType(int buildType) {
        mConfigData.buildType = buildType;
        AbstractBuildController controller = mConfigData.buildController;
        switch (buildType) {
            case MConstants.BUILD_ACTIVITY:
                //如果实例是空或者实例类型不是ActivityController则创建新的实例，防止重复创建，下面的判断是简写
                if (controller == null || !(controller instanceof ActivityController)) {
                    mConfigData.buildController = new ActivityController();
                }
                break;
            case MConstants.BUILD_FRAGMENT:
                //TODO bug:当一个项目中使用了两种或两种以上的构建方式时可能出现布局重叠
                if (!(controller instanceof FragmentController)) {
                    mConfigData.buildController = new FragmentController();
                }
                break;
            case MConstants.BUILD_DIALOG:
                if (!(controller instanceof DialogController)) {
                    mConfigData.buildController = new DialogController();
                }
                break;
            default:
                throw new IllegalArgumentException("buildType is not a predetermined parameter and see the interface for this class for more details");
        }
        return this;
    }

    @Override
    public IConfigDataBuilder setRequestCode(int requestCode) {
        mConfigData.requestCode = requestCode;
        return this;
    }

    @Override
    public IConfigDataBuilder setFrameLayoutId(int frameLayoutId) {
        mConfigData.frameLayoutId = frameLayoutId;
        return this;
    }

    @Override
    public IConfigDataBuilder setSortType(int sortType) {
        mConfigData.sortType = sortType;
        return this;
    }

    @Override
    public IConfigDataBuilder setRadio() {
        mConfigData.radio = true;
        mConfigData.maxCount = 1;
        return this;
    }

    @Override
    public IConfigDataBuilder setMaxCount(Integer maxCount) {
        mConfigData.maxCount = maxCount;
        return this;
    }

    @Override
    public IConfigDataBuilder setRootPath(String path) {
        if (path.endsWith(File.separator)) {//判断是否是以/结束的
            mConfigData.rootPath = path.substring(0, path.length() - 1);//去除最后的/
        } else {
            mConfigData.rootPath = path;
        }
        return this;
    }

    @Override
    public IConfigDataBuilder setFragmentManager(FragmentManager fragmentManager) {
        mConfigData.fragmentManager = fragmentManager;
        return this;
    }

    @Override
    public IConfigDataBuilder setShowFileTypes(String... showFileTypes) {
        mConfigData.showFileTypes = showFileTypes;
        return this;
    }

    @Override
    public IConfigDataBuilder setSelectFileTypes(String... selectFileTypes) {
        mConfigData.selectFileTypes = selectFileTypes;
        return this;
    }

    @Override
    public IConfigDataBuilder setShowSelectStorageBtn(Boolean showSelectStorageBtn) {
        mConfigData.showSelectStorageBtn = showSelectStorageBtn;
        return this;
    }

    /******************   PathSelectDialog   **************************/

    @Override
    public IConfigDataBuilder setPathSelectDialogWidth(Integer width) {
        mConfigData.pathSelectDialogWidth = width;
        return this;
    }

    @Override
    public IConfigDataBuilder setPathSelectDialogHeight(Integer height) {
        mConfigData.pathSelectDialogHeight = height;
        return this;
    }

    /******************   TitlebarFragment   **************************/

    @Override
    public IConfigDataBuilder setTitlebarFragment(AbstractTitlebarFragment titlebarFragment) {

        mConfigData.titlebarFragment = titlebarFragment;
        return this;
    }

    @Override
    public IConfigDataBuilder setShowTitlebarFragment(Boolean showTitlebarFragment) {
        mConfigData.showTitlebarFragment = showTitlebarFragment;
        return this;
    }

    @Override
    public IConfigDataBuilder setTitlebarMainTitle(FontBean titlebarMainTitle) {
        mConfigData.titlebarMainTitle = titlebarMainTitle;
        return this;
    }

    @Override
    public IConfigDataBuilder setTitlebarSubtitleTitle(FontBean titlebarSubtitleTitle) {
        mConfigData.titlebarSubtitleTitle = titlebarSubtitleTitle;
        return this;
    }

    @Override
    public IConfigDataBuilder setTitlebarBG(Integer titlebarBG) {
        mConfigData.titlebarBG = titlebarBG;
        return this;
    }

    @Override
    public IConfigDataBuilder setMorePopupItemListeners(CommonItemListener... morePopupItemListeners) {
        mConfigData.morePopupItemListeners = morePopupItemListeners;
        return this;
    }

    /******************   TabbarFragment   **************************/

    @Override
    public IConfigDataBuilder setTabbarFragment(AbstractTabbarFragment tabbarFragment) {
        mConfigData.tabbarFragment = tabbarFragment;
        return this;
    }

    @Override
    public IConfigDataBuilder setShowTabbarFragment(Boolean showTabbarFragment) {
        mConfigData.showTabbarFragment = showTabbarFragment;
        return this;
    }

    /******************   FileShowFragment   **************************/

    @Override
    public IConfigDataBuilder setFileShowFragment(AbstractFileShowFragment fileShowFragment) {
        mConfigData.fileShowFragment = fileShowFragment;
        return this;
    }

    @Override
    public IConfigDataBuilder setOnlyShowImage() {
        mConfigData.showFileTypes = new String[]{"png", "jpg"};
        return this;
    }

    @Override
    public IConfigDataBuilder setOnlyShowVideo() {
        mConfigData.showFileTypes = new String[]{"mp4"};
        return this;
    }

    @Override
    public IConfigDataBuilder setFileItemListener(FileItemListener fileItemListener) {
        mConfigData.fileItemListener = fileItemListener;
        return this;
    }

    @Override
    public IConfigDataBuilder setFileBeanController(AbstractFileBeanController fileBeanController) {
        mConfigData.fileBeanController = fileBeanController;
        return this;
    }

    /******************   HandleFragment   **************************/

    @Override
    public IConfigDataBuilder setHandleFragment(AbstractHandleFragment handleFragment) {
        mConfigData.handleFragment = handleFragment;
        return this;
    }

    @Override
    public IConfigDataBuilder setShowHandleFragment(Boolean showHandleFragment) {
        mConfigData.showHandleFragment = showHandleFragment;
        return this;
    }

    @Override
    public IConfigDataBuilder setAlwaysShowHandleFragment(Boolean alwaysShowHandleFragment) {
        mConfigData.alwaysShowHandleFragment = alwaysShowHandleFragment;
        if (alwaysShowHandleFragment) {
            mConfigData.showHandleFragment = true;
        }
        return this;
    }

    @Override
    public IConfigDataBuilder setHandleItemListeners(CommonItemListener... handleItemListeners) {
        mConfigData.handleItemListeners = handleItemListeners;
        return this;
    }


    @Override
    public PathSelectFragment show() {

        try {
            //初始化各种fragment
            mConfigData.initAllFragment();
            //初始化控制器
            mConfigData.initController();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


        return mConfigData.buildController.show();
    }


    /**
     * 获取ConfigDataBuilder实例
     * 静态内部类保证单例和线程安全
     *
     * @return
     */
    public static ConfigDataBuilderImpl getInstance() {
        return ConfigDataBuilderImpl.ConfigDataBuilderHolder.instance;
    }

    public static class ConfigDataBuilderHolder {
        private static final ConfigDataBuilderImpl instance = new ConfigDataBuilderImpl();
    }

    private static String securityCode = "jofisdhgoergd54fgd65f4g";

    private ConfigDataBuilderImpl() {
        synchronized (ConfigDataBuilderImpl.class) {
            if (securityCode.equals("jofisdhgoergd54fgd65f4g")) {
                securityCode = "hkhd254if54shd52ufhs";
            } else {
                throw new RuntimeException("Do not use reflection to break the singleton pattern!");
            }
        }
    }
}
