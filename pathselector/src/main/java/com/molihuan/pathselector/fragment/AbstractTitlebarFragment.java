package com.molihuan.pathselector.fragment;

import android.widget.TextView;

import androidx.annotation.CallSuper;

import com.molihuan.pathselector.adapter.MorePopupAdapter;
import com.molihuan.pathselector.interfaces.ITitlebarFragment;
import com.molihuan.pathselector.listener.CommonItemListener;
import com.molihuan.pathselector.utils.MConstants;

import java.util.List;

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

    @Override
    public MorePopupAdapter getMorePopupAdapter() {
        return null;
    }

    @Override
    public List<CommonItemListener> getMorePopupItemListeners() {
        return null;
    }

    @Override
    public void refreshMorePopup() {

    }

    @Override
    public TextView getOnlyOneMorePopupTextView() {
        return null;
    }
}
