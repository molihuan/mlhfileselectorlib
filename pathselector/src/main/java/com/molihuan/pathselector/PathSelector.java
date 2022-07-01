package com.molihuan.pathselector;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.molihuan.pathselector.service.SelectManager;
import com.molihuan.pathselector.utils.Constants;

/**
 * @ClassName PathSelector
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/22 3:12
 */
public class PathSelector {
    private static int buidlType;//跳转显示方式：Activity Fragment
    private static Context mContext;//上下文
    private static Fragment mFragment;//fragment

    private PathSelector(Activity activity) {
        this.mContext=activity;
        this.mFragment=null;
    }

    private PathSelector(Fragment fragment){
        this.mContext=fragment.getActivity();
        this.mFragment=fragment;
    }

    public static SelectManager build(Activity activity,int buidlType){
        if (buidlType== Constants.BUILD_FRAGMENT){
            throw new IllegalArgumentException("BUILD_FRAGMENT模式下context必须为FragmentActivity以及其子类(AppCompatActivity)类型");
        }
        PathSelector.buidlType=buidlType;
        return new PathSelector(activity).initManager();
    }

    public static SelectManager build(Fragment fragment,int buidlType){
        if (buidlType== Constants.BUILD_FRAGMENT){
            throw new IllegalArgumentException("BUILD_FRAGMENT模式下context必须为FragmentActivity以及其子类(AppCompatActivity)类型");
        }
        PathSelector.buidlType=buidlType;
        return new PathSelector(fragment).initManager();
    }


    /**
     * Fragment模式参数必须为FragmentActivity类型以及其子类(AppCompatActivity)
     * @param activity
     * @param buidlType
     * @return
     */
    public static SelectManager build(FragmentActivity activity,int buidlType){
        PathSelector.buidlType=buidlType;
        return new PathSelector(activity).initManager();
    }

    /**
     * 初始化管理者
     * @return
     */
    private SelectManager initManager(){
        return new SelectManager(this);
    }

    public Context getActivity() {
        return mContext;
    }

    public Fragment getFragment() {
        return mFragment != null ? mFragment : null;
    }

    public int getBuidlType() {
        return buidlType;
    }

    /**
     * 释放资源
     */
    public void release() {
        mContext=null;
        mFragment=null;
    }
}
