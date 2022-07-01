package com.molihuan.pathselector.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.molihuan.pathselector.interfaces.IActivityAndFragment;


/**
 * 自定义布局建议继承此类
 * 基本的DialogFragment
 * Created by molihuan on 2022/6/7.
 */

public abstract class BaseDialogFragment extends DialogFragment implements View.OnClickListener {

    public View mFragmentView;//FragmentView
    public Activity mActivity;//依附的activity
    public IActivityAndFragment mIActivityAndFragment;//定义activity与fragment通信接口
    private static int DialogFragmentWith;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mFragmentView==null){
            mFragmentView = inflater.inflate(getFragmentViewId(), container, false);//获取布局
            getComponents(mFragmentView);//获取组件
            initData();//初始化数据
            initView();//初始化视图
            setListeners();//设置监听
        }
        return mFragmentView;
    }

    /**
     * 子类的数据初始化必须在此方法下，否则可能出现空指针异常
     * @param
     */

    public abstract int getFragmentViewId();
    public abstract void getComponents(View view);
    public abstract void initData();
    public abstract void initView();
    public abstract void setListeners();


    @Override
    public void onClick(View v) { }

    /**
     * 接管Activity返回键的接口
     * @return true表示Fragment已经处理了 false反之
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * fragment隐藏显示监听
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    /**
     * 当Activity和Fragment产生关系时调用
     * context可以强转为Activity
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (mActivity==null){
            mActivity= getActivity();
        }


        try {
            if (context instanceof IActivityAndFragment){
                //获取通信接口实例
                mIActivityAndFragment= (IActivityAndFragment) context;
            }
        } catch (Exception e) {
            Log.e("interfaceError","IActivityAndFragment错误，Activity必须实现IActivityAndFragment接口");
            e.printStackTrace();
        }
    }

    /**
     * 当Activity和Fragment脱离时调用
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }
}