package com.molihuan.pathselector.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.molihuan.pathselector.dao.SelectConfigData;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.interfaces.IActivityAndFragment;
import com.molihuan.pathselector.service.impl.ConfigDataBuilderImpl;

/**
 * @ClassName: AbstractFragmentDialog
 * @Author: molihuan
 * @Date: 2022/11/22/15:36
 * @Description:
 */
public abstract class AbstractFragmentDialog extends DialogFragment implements DialogInterface.OnKeyListener {
    //FragmentView
    public View mFragmentView;
    //依附的Activity
    public Activity mActivity;
    //与Activity通讯接口
    public IActivityAndFragment mIActivityAndFragment;

    protected Dialog mDialog;
    //宽
    private int mWidth;
    //高
    private int mHeight;

    protected SelectConfigData mConfigData = ConfigDataBuilderImpl.getInstance().getSelectConfigData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mFragmentView == null) {
            //获取dialog
            mDialog = getDialog();
            //初始化宽高
            mWidth = mConfigData.pathSelectDialogWidth;
            mHeight = mConfigData.pathSelectDialogHeight;

            //获取Fragment布局
            mFragmentView = inflater.inflate(setFragmentViewId(), container, false);
            //获取组件
            getComponents(mFragmentView);
            //初始化数据
            initData();
            //初始化视图
            initView();
            //设置监听
            setListeners();
        }
        return mFragmentView;
    }

    /**
     * 子类的数据初始化必须在这些方法中，否则可能出现空指针异常
     *
     * @param
     */

    public abstract int setFragmentViewId();

    public abstract void getComponents(View view);

    public abstract void initData();

    @CallSuper
    public void initView() {
        if (mDialog != null) {
            //点击外面不能取消
            mDialog.setCanceledOnTouchOutside(false);
        }
    }

    @CallSuper
    public void setListeners() {
        if (mDialog != null) {
            //添加监听
            mDialog.setOnKeyListener(this);
        }
    }

    public abstract PathSelectFragment getPathSelectFragment();


    /**
     * 子类可以重写此方法让fragment先处理返回按钮事件
     * keyCode == KeyEvent.KEYCODE_BACK(返回键)
     * true表示Fragment已经处理了Activity可以不用处理了 false反之
     */
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        //会有两次调用按下和松开先消费掉一次
        if (event.getAction() != KeyEvent.ACTION_DOWN) {
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //此处捕获back操作，如果不希望所在的Activity监听到back键，需要返回true，消费掉。
            dismissAllowingStateLoss();
            return true;
        } else {
            //这里注意当不是返回键时需将事件扩散，否则无法处理其他点击事件
            return false;
        }
    }

    /**
     * fragment隐藏显示监听
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    /**
     * 设置宽高
     */
    private void setWidthHeight() {
        if (mDialog != null) {
            if (mDialog.getWindow() != null) {
                mDialog.getWindow().setLayout(mWidth, mHeight);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        setWidthHeight();
    }

    /**
     * 当Activity和Fragment产生关系时调用
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //获取与fragment产生关系的Activity
        if (mActivity == null) {
            mActivity = getActivity();
        }
        //获取与Activity通讯实例
//        try {
//            if (context instanceof IActivityAndFragment) {
//                //获取通信接口实例
//                mIActivityAndFragment = (IActivityAndFragment) context;
//            } else {
//                throw new RuntimeException("The current class must implement the IActivityAndFragment interface");
//            }
//        } catch (Exception e) {
//            Log.e("Interface err", "The current class must implement the IActivityAndFragment interface");
//            e.printStackTrace();
//        }
    }

    /**
     * 当Activity和Fragment脱离时调用
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 当移除FragmentView时调用
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

