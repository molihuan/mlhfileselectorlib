package com.molihuan.pathselector.fragment;

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
    public abstract IFileDataManager getPathFileManager();

    public abstract IFileDataManager getUriFileManager();

    public abstract void returnDataToActivityResult();

    /**
     * 显示或隐藏handleFragment
     *
     * @param isShow
     */
    public abstract void handleShowHide(boolean isShow);
}
