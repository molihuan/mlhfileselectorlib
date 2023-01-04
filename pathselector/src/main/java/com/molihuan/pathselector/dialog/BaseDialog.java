package com.molihuan.pathselector.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.molihuan.pathselector.dao.SelectConfigData;
import com.molihuan.pathselector.fragment.BasePathSelectFragment;
import com.molihuan.pathselector.service.impl.ConfigDataBuilderImpl;
import com.molihuan.pathselector.utils.MConstants;

/**
 * @ClassName: BaseDialog
 * @Author: molihuan
 * @Date: 2022/12/11/14:09
 * @Description:
 */
public abstract class BaseDialog extends AlertDialog implements View.OnClickListener {

    protected Context mContext;

    protected SelectConfigData mConfigData = ConfigDataBuilderImpl.getInstance().getSelectConfigData();

    protected BasePathSelectFragment psf;             //总fragment

    public BaseDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局资源
        setContentView(setContentViewID());
        //获取组件
        getComponents();
        //初始化数据
        initData();
        //初始化视图
        initView();
        //设置监听
        setListeners();
    }

    public abstract @LayoutRes
    int setContentViewID();

    public abstract void getComponents();

    
    public void initData() {
        psf = (BasePathSelectFragment) mConfigData.fragmentManager.findFragmentByTag(MConstants.TAG_ACTIVITY_FRAGMENT);
    }

    public void initView() {
    }

    public void setListeners() {
    }


    @Override
    public void onClick(View v) {

    }

    /**
     * 回调接口
     */
    public interface IOnConfirmListener {
        boolean onClick(View v, BaseDialog dialog);
    }

    public interface IOnCancelListener {
        boolean onClick(View v, BaseDialog dialog);
    }
}
