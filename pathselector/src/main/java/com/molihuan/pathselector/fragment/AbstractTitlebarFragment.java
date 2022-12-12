package com.molihuan.pathselector.fragment;

import androidx.annotation.CallSuper;

import com.molihuan.pathselector.interfaces.ITitlebarFragment;
import com.molihuan.pathselector.utils.MConstants;

/**
 * @ClassName: AbstractTitlebarFragment
 * @Author: molihuan
 * @Date: 2022/11/22/17:37
 * @Description: 标题区域 Fragment
 */
public abstract class AbstractTitlebarFragment extends AbstractFragment implements ITitlebarFragment {

    protected BasePathSelectFragment psf;             //总fragment

    @CallSuper
    @Override
    public void initData() {
        psf = (BasePathSelectFragment) mConfigData.fragmentManager.findFragmentByTag(MConstants.TAG_ACTIVITY_FRAGMENT);
    }
}
