package com.molihuan.pathselector.fragment;

import androidx.annotation.CallSuper;

import com.molihuan.pathselector.interfaces.IFileShowFragment;
import com.molihuan.pathselector.utils.MConstants;

/**
 * @ClassName: AbstractFileShowFragment
 * @Author: molihuan
 * @Date: 2022/11/22/17:39
 * @Description: 中间显示所以文件的Fragment抽象类
 */
public abstract class AbstractFileShowFragment extends AbstractFragment implements IFileShowFragment {
    protected BasePathSelectFragment psf;             //总fragment

    @Override
    @CallSuper
    public void initData() {
        psf = (BasePathSelectFragment) mConfigData.fragmentManager.findFragmentByTag(MConstants.TAG_ACTIVITY_FRAGMENT);
    }
}
