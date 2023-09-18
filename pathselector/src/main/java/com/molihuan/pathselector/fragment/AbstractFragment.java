package com.molihuan.pathselector.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.molihuan.pathselector.dao.SelectConfigData;
import com.molihuan.pathselector.interfaces.IActivityAndFragment;
import com.molihuan.pathselector.service.impl.ConfigDataBuilderImpl;


/**
 * @ClassName: AbstractFragment
 * @Author: molihuan
 * @Date: 2022/11/22/14:29
 * @Description: 自定义路径选择的fragment必须继承其子类AbstractFileShowFragment、AbstractHandleFragment、AbstractTabbarFragment、AbstractTitlebarFragment
 */
public abstract class AbstractFragment extends DialogFragment {
    //FragmentView
    public View mFragmentView;
    //依附的Activity
    public Activity mActivity;
    //与Activity通讯接口
    public IActivityAndFragment mIActivityAndFragment;

    protected SelectConfigData mConfigData = ConfigDataBuilderImpl.getInstance().getSelectConfigData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mFragmentView == null) {
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

    public abstract @LayoutRes
    int setFragmentViewId();

    public abstract void getComponents(View view);

    public void initData() {

    }

    public void initView() {

    }

    public void setListeners() {

    }


    /**
     * 子类可以重写此方法让fragment先处理返回按钮事件
     *
     * @return true表示Fragment已经处理了Activity可以不用处理了 false反之
     */
    public boolean onBackPressed() {
        return false;
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
