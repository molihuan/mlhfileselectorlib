package com.molihuan.pathselector.fragment;

import androidx.annotation.CallSuper;

import com.molihuan.pathselector.interfaces.IHandleFragment;
import com.molihuan.pathselector.utils.MConstants;

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
}
