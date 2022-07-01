package com.molihuan.pathselector.utils;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * author: molihuan
 * 2022.6.1
 *   fragment工具类
 */

public class FragmentTools {

    /**
     * 显示隐藏fragment
     * @param fragmentManager Fragment管理
     * @param frameLayoutID   添加的地方frameLayout
     * @param fragment   添加的布局Fragment
     * @param tag    tag
     * @param isShow    是否显示
     * @return
     */
    public static Fragment fragmentShowHide(FragmentManager fragmentManager, int frameLayoutID, Fragment fragment,String tag, boolean isShow) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment==null){
            Log.e("" ,"fragment为空，无法添加");
            return fragment;
        }

        if (!fragment.isAdded()) {//判断是否已经被添加过了
            try {
                fragmentTransaction = fragmentTransaction.add(frameLayoutID, fragment,tag);
            } catch (Exception e) {
                Log.e("" ,"frameLayoutID可能不存在，无法添加");
                e.printStackTrace();
            }
        }


        if (isShow){
            fragmentTransaction.show(fragment);//显示fragment
        }else {
            fragmentTransaction.hide(fragment);//隐藏fragment
        }
        fragmentTransaction.commitAllowingStateLoss();//提交事务
        //Log.e("","提交成功");
        return fragment;
    }

}
