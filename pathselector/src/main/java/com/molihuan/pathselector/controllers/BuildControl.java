package com.molihuan.pathselector.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.molihuan.pathselector.PathSelector;
import com.molihuan.pathselector.activities.PathSelectActivity;
import com.molihuan.pathselector.dao.SelectOptions;
import com.molihuan.pathselector.dialogs.PathSelectDialog;
import com.molihuan.pathselector.fragments.PathSelectFragment;
import com.molihuan.pathselector.utils.Constants;
import com.molihuan.pathselector.utils.FragmentTools;

/**
 * @ClassName BuildControl
 * @Description TODO 跳转控制
 * @Author molihuan
 * @Date 2022/6/22 4:04
 */
public class BuildControl {

    private static boolean withoutActionBar=true;//不需要ActionBar
    public static int buidlType;//不需要ActionBar
    public static PathSelectDialog mPathSelectDialog;//构建的PathSelectDialog


    /**
     * 开始打开文件选择器
     */
    public static PathSelectFragment show(PathSelector mPathSelector, SelectOptions mSelectOptions) {
        buidlType = mPathSelector.getBuidlType();
        switch (buidlType){
            case Constants.BUILD_FRAGMENT :
                return buildByFragment(mPathSelector,mSelectOptions);
            case Constants.BUILD_ACTIVITY :
                buildByActivity(mPathSelector,mSelectOptions);
                return null;
            case Constants.BUILD_DIALOG:
                buildByDialog(mPathSelector,mSelectOptions);
                return null;
            default:
                return null;
        }
    }
    /**
     * Dialog模式
     * @param mPathSelector
     * @param mSelectOptions
     */
    private static void buildByDialog(PathSelector mPathSelector, SelectOptions mSelectOptions){
        Context activity = mPathSelector.getActivity();
        if (activity instanceof FragmentActivity){
            withoutActionBar=false;
            buildByDialog((FragmentActivity) activity,mSelectOptions);
        }else {
            throw new ClassCastException("PathSelector.build(context,Constant.BUILD_FRAGMENT)中context必须为FragmentActivity类型以及其子类(如 AppCompatActivity)");
        }
    }

    private static void buildByDialog(FragmentActivity activity, SelectOptions mSelectOptions){

        withoutActionBar=true;
        mSelectOptions.fragmentManager=activity.getSupportFragmentManager();
        mPathSelectDialog = new PathSelectDialog();
        mPathSelectDialog.show(mSelectOptions.fragmentManager,Constants.TAG_DIALOG_FRAGMENT);

    }

    /**
     * ACTIVITY模式
     * @param mPathSelector
     * @param mSelectOptions
     */
    private static void buildByActivity(PathSelector mPathSelector, SelectOptions mSelectOptions){
        Context activity = mPathSelector.getActivity();

        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, PathSelectActivity.class);
        Fragment fragment = mPathSelector.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, mSelectOptions.requestCode);//设置返回码
        } else {
            if (activity instanceof FragmentActivity){
                ((FragmentActivity)activity).startActivityForResult(intent, mSelectOptions.requestCode);
            }else {
                ((Activity)activity).startActivityForResult(intent, mSelectOptions.requestCode);
            }

        }
    }
    /**
     * Fragment模式
     * Fragment模式必须为FragmentActivity类型以及其子类(AppCompatActivity)
     * @param mPathSelector
     * @param mSelectOptions
     */
    private static PathSelectFragment buildByFragment(PathSelector mPathSelector, SelectOptions mSelectOptions){
        Context activity = mPathSelector.getActivity();
        if (activity instanceof FragmentActivity){
            withoutActionBar=false;
            return buildByFragment((FragmentActivity) activity,mSelectOptions);
        }else {
            throw new ClassCastException("PathSelector.build(context,Constant.BUILD_FRAGMENT)中context必须为FragmentActivity类型以及其子类(如 AppCompatActivity)");
        }
    }
    /**
     * Fragment模式(PathSelectActivity)
     * @param activity
     * @param mSelectOptions
     */
    public static PathSelectFragment buildByFragment(FragmentActivity activity, SelectOptions mSelectOptions){


        if (withoutActionBar){
            //去除ActionBar
            if (activity instanceof AppCompatActivity){
                ActionBar supportActionBar = ((AppCompatActivity)activity).getSupportActionBar();
                if (supportActionBar != null) supportActionBar.hide();
            }else {
                android.app.ActionBar actionBar = activity.getActionBar();
                if (actionBar != null) actionBar.hide();
            }
        }

        withoutActionBar=true;
        PathSelectFragment pathSelectFragment = new PathSelectFragment();
        mSelectOptions.fragmentManager=activity.getSupportFragmentManager();
        FragmentTools.fragmentShowHide(mSelectOptions.fragmentManager,mSelectOptions.frameLayoutID,pathSelectFragment,Constants.TAG_FRAGMENT_PATHSELECT,true);
        return pathSelectFragment;
    }
}
