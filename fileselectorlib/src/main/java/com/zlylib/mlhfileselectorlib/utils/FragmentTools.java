package com.zlylib.mlhfileselectorlib.utils;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class FragmentTools {

    /**
     * 显示隐藏fragment
     * @param fragmentTransaction
     * @param frameLayoutID
     * @param fragment
     * @param isShow
     * @return
     */
    public static Fragment fragmentShowHide(FragmentTransaction fragmentTransaction, int frameLayoutID, Fragment fragment,boolean isShow) {

        if (fragment==null){
            Log.e("" ,"fragment为空");
            return fragment;
        }

        if (!fragment.isAdded()) {//判断是否已经被添加过了
            fragmentTransaction = fragmentTransaction.add(frameLayoutID, fragment);
        }


        if (isShow){
            fragmentTransaction.show(fragment);//显示fragment
        }else {
            fragmentTransaction.hide(fragment);//显示fragment
        }
        fragmentTransaction.commitAllowingStateLoss();//提交事务
        Log.e("","提交成功");
        return fragment;
    }

}
