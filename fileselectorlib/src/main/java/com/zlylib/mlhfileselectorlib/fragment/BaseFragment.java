package com.zlylib.mlhfileselectorlib.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.zlylib.mlhfileselectorlib.interfaces.IActivityAndFragment;

/**
 * 自定义布局建议继承此类
 * 基本的fragment
 * Created by molihuan on 2022/6/7.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    public int mFragmentViewId;//Fragment布局资源id
    public View mFragmentView;//FragmentView
    public Activity mActivity;//依附的activity
    public IActivityAndFragment mIActivityAndFragment;//定义activity与fragment通信接口


    public BaseFragment(@LayoutRes int fragmentViewId) {//Fragment布局资源id
        mFragmentViewId=fragmentViewId;
    }

    public BaseFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mFragmentView==null){
            mFragmentView = inflater.inflate(mFragmentViewId, container, false);//获取布局
            getComponents(mFragmentView);//获取组件
            setListeners(mFragmentView);//设置监听
            initData(mFragmentView);//初始化数据
        }
        return mFragmentView;
    }

    public abstract void initData(View view);

    public abstract void setListeners(View view);

    public abstract void getComponents(View view);

    @Override
    public abstract void onClick(View v);

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
            //获取通信接口实例
            mIActivityAndFragment= (IActivityAndFragment) context;
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