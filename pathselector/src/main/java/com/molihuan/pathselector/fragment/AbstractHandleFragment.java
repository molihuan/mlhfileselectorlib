package com.molihuan.pathselector.fragment;

import androidx.annotation.CallSuper;

import com.molihuan.pathselector.adapter.HandleListAdapter;
import com.molihuan.pathselector.interfaces.IHandleFragment;
import com.molihuan.pathselector.listener.CommonItemListener;
import com.molihuan.pathselector.utils.MConstants;

import java.util.List;

/**
 * @ClassName: AbstractHandleFragment
 * @Author: molihuan
 * @Date: 2022/11/22/17:40
 * @Description: 底部按钮显示的Fragment
 */
public abstract class AbstractHandleFragment extends AbstractFragment implements IHandleFragment {
    protected BasePathSelectFragment psf;             //总fragment

    @CallSuper
    @Override
    public void initData() {
        psf = (BasePathSelectFragment) mConfigData.fragmentManager.findFragmentByTag(MConstants.TAG_ACTIVITY_FRAGMENT);
    }

    @Override
    public List<CommonItemListener> getHandleItemListeners() {
        return null;
    }

    @Override
    public HandleListAdapter getHandleListAdapter() {
        return null;
    }

    @Override
    public void refreshHandleList() {

    }
}
