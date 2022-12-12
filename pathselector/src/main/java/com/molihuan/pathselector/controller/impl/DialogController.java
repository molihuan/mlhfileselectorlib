package com.molihuan.pathselector.controller.impl;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.molihuan.pathselector.PathSelector;
import com.molihuan.pathselector.controller.AbstractBuildController;
import com.molihuan.pathselector.dialog.AbstractFragmentDialog;
import com.molihuan.pathselector.dialog.impl.PathSelectDialog;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.utils.MConstants;


/**
 * @ClassName: DialogController
 * @Author: molihuan
 * @Date: 2022/11/22/23:53
 * @Description:
 */
public class DialogController extends AbstractBuildController {
    private AbstractFragmentDialog pathSelectDialog;

    @Override
    public PathSelectFragment show() {
        Context context = mConfigData.context;
        Fragment fragment = PathSelector.getFragment();
        FragmentManager fragmentManager;
        if (fragment != null) {
            fragmentManager = fragment.getChildFragmentManager();
        } else {
            if (context instanceof FragmentActivity) {
                fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            } else {
                throw new ClassCastException("context必须为FragmentActivity类型以及其子类(如 AppCompatActivity)或PathSelector.fragment不为空");
            }
        }
        mConfigData.fragmentManager = fragmentManager;

        pathSelectDialog = new PathSelectDialog();

        //显示 Dialog 弹窗
        pathSelectDialog.show(fragmentManager, MConstants.TAG_ACTIVITY_FRAGMENT);//这里设置tag没有用需要在AbstractDialog中设置tag

        return pathSelectDialog.getPathSelectFragment();
    }

    @Override
    public PathSelectFragment getPathSelectFragment() {
        return pathSelectDialog.getPathSelectFragment();
    }

    @Override
    public AbstractFragmentDialog getDialogFragment() {
        return pathSelectDialog;
    }
}
