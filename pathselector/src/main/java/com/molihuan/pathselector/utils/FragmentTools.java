package com.molihuan.pathselector.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Author: molihuan
 * 2022.6.1
 * fragment工具类
 */

public class FragmentTools {

    /**
     * 显示 隐藏 fragment
     *
     * @param fragmentManager Fragment管理者
     * @param frameLayoutID   添加的地方frameLayout
     * @param fragment        添加的布局Fragment
     * @param tag             tag
     * @param isShow          是否显示
     * @return
     */
    public static Fragment fragmentShowHide(FragmentManager fragmentManager, int frameLayoutID, Fragment fragment, String tag, boolean isShow) {
        // Fragment获取事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment == null) {
            Mtools.log("fragment is null and Unable to add");
            return fragment;
        }
        //判断是否已经被添加过了
        if (!fragment.isAdded()) {
            try {
                fragmentTransaction = fragmentTransaction.add(frameLayoutID, fragment, tag);
            } catch (Exception e) {
                Mtools.log("frameLayoutID may not exist and cannot be added");
                e.printStackTrace();
            }
        }

        if (isShow) {
            //显示fragment
            fragmentTransaction.show(fragment);
        } else {
            //隐藏fragment
            fragmentTransaction.hide(fragment);
        }

        //提交事务
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

    public static Fragment fragmentReplace(FragmentManager fragmentManager, int frameLayoutID, Fragment fragment, String tag) {
        // Fragment获取事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment == null) {
            Mtools.log("fragment is null and Unable to replace");
            return fragment;
        }

        try {
            fragmentTransaction = fragmentTransaction.replace(frameLayoutID, fragment, tag);
        } catch (Exception e) {
            Mtools.log("frameLayoutID may not exist and cannot be replace");
            e.printStackTrace();
        }


        //提交事务
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

}
