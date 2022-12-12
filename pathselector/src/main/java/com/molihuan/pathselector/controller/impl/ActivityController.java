package com.molihuan.pathselector.controller.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.molihuan.pathselector.PathSelector;
import com.molihuan.pathselector.activity.impl.PathSelectActivity;
import com.molihuan.pathselector.controller.AbstractBuildController;
import com.molihuan.pathselector.dialog.AbstractFragmentDialog;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;


/**
 * @ClassName: ActivityController
 * @Author: molihuan
 * @Date: 2022/11/22/23:53
 * @Description:
 */
public class ActivityController extends AbstractBuildController {
    @Override
    public PathSelectFragment show() {
        Integer requestCode = mConfigData.requestCode;
        //判断是否设置了请求码
        if (requestCode == null) {
            throw new NullPointerException("requestCode is a null object reference and you must set it");
        }

        Context activity = mConfigData.context;
        if (activity == null) {
            return null;
        }
        Intent intent = new Intent(activity, PathSelectActivity.class);
        Fragment fragment = PathSelector.getFragment();

        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);//设置返回码
        } else {
            if (activity instanceof FragmentActivity) {
                ((FragmentActivity) activity).startActivityForResult(intent, requestCode);
            } else {
                ((Activity) activity).startActivityForResult(intent, requestCode);
            }
        }
        return null;
    }

    @Override
    public PathSelectFragment getPathSelectFragment() {
        //都跳转了，就没有了
        return null;
    }

    @Override
    public AbstractFragmentDialog getDialogFragment() {
        return null;
    }
}
