package com.molihuan.pathselector.fragment;

import android.app.Dialog;

import com.molihuan.pathselector.controller.impl.DialogController;
import com.molihuan.pathselector.dao.SelectConfigData;
import com.molihuan.pathselector.interfaces.IFileShowFragment;
import com.molihuan.pathselector.interfaces.IHandleFragment;
import com.molihuan.pathselector.interfaces.ITabbarFragment;
import com.molihuan.pathselector.interfaces.ITitlebarFragment;
import com.molihuan.pathselector.service.IFileDataManager;

/**
 * @ClassName: BasePathSelectFragment
 * @Author: molihuan
 * @Date: 2022/11/23/19:20
 * @Description: 拥有了她就拥有了它就拥有了全世界(具体方法可以看她的实现类或者她的接口)
 */
public abstract class BasePathSelectFragment extends AbstractFragment implements IFileShowFragment, IHandleFragment, ITabbarFragment, ITitlebarFragment {
    
    public abstract void returnDataToActivityResult();

    public abstract SelectConfigData getSelectConfigData();

    public abstract AbstractTitlebarFragment getTitlebarFragment();

    public abstract AbstractTabbarFragment getTabbarFragment();

    public abstract AbstractFileShowFragment getFileShowFragment();

    public abstract AbstractHandleFragment getHandleFragment();

    public abstract IFileDataManager getPathFileManager();

    public abstract IFileDataManager getUriFileManager();

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
