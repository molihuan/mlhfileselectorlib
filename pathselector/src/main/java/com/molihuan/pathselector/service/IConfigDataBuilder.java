package com.molihuan.pathselector.service;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;

import com.molihuan.pathselector.controller.AbstractFileBeanController;
import com.molihuan.pathselector.entity.FontBean;
import com.molihuan.pathselector.fragment.AbstractFileShowFragment;
import com.molihuan.pathselector.fragment.AbstractHandleFragment;
import com.molihuan.pathselector.fragment.AbstractTabbarFragment;
import com.molihuan.pathselector.fragment.AbstractTitlebarFragment;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.listener.CommonItemListener;
import com.molihuan.pathselector.listener.FileItemListener;

public interface IConfigDataBuilder {


    IConfigDataBuilder setContext(Context context);

    /**
     * 设置构建模式: Activity、Fragment、Dialog 详细请看本类中{@link com.molihuan.pathselector.utils.MConstants} 传送门
     *
     * @param buildType
     * @return
     */
    IConfigDataBuilder setBuildType(int buildType);

    IConfigDataBuilder setRequestCode(int requestCode);

    IConfigDataBuilder setFrameLayoutId(@IdRes int frameLayoutId);

    IConfigDataBuilder setSortType(int sortType);

    IConfigDataBuilder setRadio();

    IConfigDataBuilder setMaxCount(Integer maxCount);

    IConfigDataBuilder setRootPath(String rootPath);

    IConfigDataBuilder setFragmentManager(FragmentManager fragmentManager);

    IConfigDataBuilder setShowFileTypes(String... showFileTypes);

    IConfigDataBuilder setSelectFileTypes(String... selectFileTypes);

    IConfigDataBuilder setShowSelectStorageBtn(Boolean showSelectStorageBtn);

    /******************   PathSelectDialog   **************************/
    IConfigDataBuilder setPathSelectDialogWidth(Integer width);

    IConfigDataBuilder setPathSelectDialogHeight(Integer height);

    /******************   TitlebarFragment   **************************/

    IConfigDataBuilder setTitlebarFragment(AbstractTitlebarFragment titlebarFragment);

    IConfigDataBuilder setShowTitlebarFragment(Boolean showTitlebarFragment);

    IConfigDataBuilder setTitlebarMainTitle(FontBean titlebarMainTitle);

    IConfigDataBuilder setTitlebarSubtitleTitle(FontBean titlebarSubtitleTitle);

    IConfigDataBuilder setTitlebarBG(Integer titlebarBG);

    IConfigDataBuilder setMorePopupItemListeners(CommonItemListener... morePopupItemListener);

    /******************   TabbarFragment   **************************/

    IConfigDataBuilder setTabbarFragment(AbstractTabbarFragment tabbarFragment);

    IConfigDataBuilder setShowTabbarFragment(Boolean showTabbarFragment);

    /******************   FileShowFragment   **************************/

    IConfigDataBuilder setFileShowFragment(AbstractFileShowFragment fileShowFragment);

    IConfigDataBuilder setOnlyShowImage();

    IConfigDataBuilder setOnlyShowVideo();

    IConfigDataBuilder setFileItemListener(FileItemListener fileItemListener);

    IConfigDataBuilder setFileBeanController(AbstractFileBeanController fileBeanController);

    /******************   HandleFragment   **************************/

    IConfigDataBuilder setHandleFragment(AbstractHandleFragment handleFragment);

    IConfigDataBuilder setShowHandleFragment(Boolean showHandleFragment);

    IConfigDataBuilder setAlwaysShowHandleFragment(Boolean alwaysShowHandleFragment);

    IConfigDataBuilder setHandleItemListeners(CommonItemListener... handleItemListener);

    PathSelectFragment show();

}
