package com.molihuan.pathselector.fragment;

import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.CallSuper;

import com.molihuan.pathselector.activity.AbstractActivity;
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


    @Override
    @CallSuper
    public void initView() {
        super.initView();
        setStatusBarColor(mConfigData.statusBarHexColor);
    }

    /**
     * 设置状态栏颜色
     *
     * @param hexColor:16进制颜色
     */
    public void setStatusBarColor(String hexColor) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (!(mActivity instanceof AbstractActivity)) {
            return;
        }

        Window window = mActivity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 将十六进制颜色值转换为对应的整数值
        int color = Color.parseColor(hexColor);
        // 将整数颜色值设置为状态栏颜色
        window.setStatusBarColor(color);

    }
}
