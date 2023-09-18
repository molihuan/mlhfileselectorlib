package com.molihuan.pathselector.fragment;

import android.app.Dialog;

import com.molihuan.pathselector.controller.impl.ActivityController;
import com.molihuan.pathselector.controller.impl.DialogController;
import com.molihuan.pathselector.controller.impl.FragmentController;
import com.molihuan.pathselector.dao.SelectConfigData;
import com.molihuan.pathselector.interfaces.IFileShowFragment;
import com.molihuan.pathselector.interfaces.IHandleFragment;
import com.molihuan.pathselector.interfaces.ITabbarFragment;
import com.molihuan.pathselector.interfaces.ITitlebarFragment;
import com.molihuan.pathselector.service.IFileDataManager;
import com.molihuan.pathselector.service.impl.ConfigDataBuilderImpl;
import com.molihuan.pathselector.utils.MConstants;

/**
 * @ClassName: BasePathSelectFragment
 * @Author: molihuan
 * @Date: 2022/11/23/19:20
 * @Description: 拥有了她就拥有了它就拥有了全世界(具体方法可以看她的实现类或者她的接口)
 */
public abstract class BasePathSelectFragment extends AbstractFragment implements IFileShowFragment, IHandleFragment, ITabbarFragment, ITitlebarFragment {

    public ConfigDataBuilderImpl mConfigDataBuilder = ConfigDataBuilderImpl.getInstance();

    public abstract void returnDataToActivityResult();

    /**
     * 获取配置数据
     *
     * @return
     */
    public abstract SelectConfigData getSelectConfigData();

    public abstract AbstractTitlebarFragment getTitlebarFragment();

    public abstract AbstractTabbarFragment getTabbarFragment();

    public abstract AbstractFileShowFragment getFileShowFragment();

    public abstract AbstractHandleFragment getHandleFragment();

    public abstract IFileDataManager getPathFileManager();

    public abstract IFileDataManager getUriFileManager();

    /**
     * 关闭选择器
     */
    public void close() {
        if (mConfigData.buildController instanceof DialogController) {
            mConfigData.buildController.getDialogFragment().dismiss();
        } else if (mConfigData.buildController instanceof ActivityController) {
            mActivity.finish();
        } else if (mConfigData.buildController instanceof FragmentController) {
            super.dismiss();
        }
    }

    /**
     * 更新UI
     */
    public void updateUI() {
        updateFileList();
    }

    public void setSortType(MConstants.SortRules sortType) {
        mConfigDataBuilder.setSortType(sortType);
        updateFileList();
    }


    /**
     * 请使用close()
     */
    @Deprecated
    @Override
    public void dismiss() {
        if (mConfigData.buildController instanceof DialogController) {
            mConfigData.buildController.getDialogFragment().dismiss();
        }
    }

    @Override
    public Dialog getDialog() {
        if (mConfigData.buildController instanceof DialogController) {
            return mConfigData.buildController.getDialogFragment().getDialog();
        }
        return null;
    }


    /**
     * 显示或隐藏handleFragment
     *
     * @param isShow
     */
    public abstract void handleShowHide(boolean isShow);
}
