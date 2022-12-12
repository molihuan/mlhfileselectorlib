package com.molihuan.pathselector.fragment;

import androidx.annotation.CallSuper;

import com.molihuan.pathselector.interfaces.ITabbarFragment;
import com.molihuan.pathselector.utils.MConstants;

/**
 * @ClassName: AbstractTabbarFragment
 * @Author: molihuan
 * @Date: 2022/11/22/17:38
 * @Description: 面包屑Fragment
 */
public abstract class AbstractTabbarFragment extends AbstractFragment implements ITabbarFragment {
    protected BasePathSelectFragment psf;             //总fragment

    @CallSuper
    @Override
    public void initData() {
        psf = (BasePathSelectFragment) mConfigData.fragmentManager.findFragmentByTag(MConstants.TAG_ACTIVITY_FRAGMENT);
    }
}
