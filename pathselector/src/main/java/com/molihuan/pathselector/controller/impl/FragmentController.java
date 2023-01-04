package com.molihuan.pathselector.controller.impl;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.molihuan.pathselector.PathSelector;
import com.molihuan.pathselector.controller.AbstractBuildController;
import com.molihuan.pathselector.dialog.AbstractFragmentDialog;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.utils.FragmentTools;
import com.molihuan.pathselector.utils.MConstants;
import com.molihuan.pathselector.utils.Mtools;


/**
 * @ClassName: FragmentController
 * @Author: molihuan
 * @Date: 2022/11/22/23:53
 * @Description:
 */
public class FragmentController extends AbstractBuildController {

    private PathSelectFragment pathSelectFragment;

    @Override
    public PathSelectFragment show() {
        Integer frameLayoutId = mConfigData.frameLayoutId;
        if (frameLayoutId == null) {
            throw new NullPointerException("frameLayoutId is a null object reference and you must set it");
        }
        Context context = mConfigData.context;
        Fragment fragment = PathSelector.getFragment();
        FragmentManager fragmentManager;
        if (fragment != null) {
            fragmentManager = fragment.getChildFragmentManager();
        } else {
            if (context instanceof FragmentActivity) {
                FragmentActivity activity = (FragmentActivity) context;
                fragmentManager = activity.getSupportFragmentManager();
            } else {
                throw new ClassCastException("context必须为FragmentActivity类型以及其子类(如 AppCompatActivity)或PathSelector.fragment不为空");
            }
        }
        mConfigData.fragmentManager = fragmentManager;
        Mtools.log("PathSelectFragment  new  start");

        pathSelectFragment = new PathSelectFragment();

        Mtools.log("PathSelectFragment  new  end");

        Mtools.log("PathSelectFragment  show  start");
        FragmentTools.fragmentReplace(
                fragmentManager,
                frameLayoutId,
                pathSelectFragment,
                MConstants.TAG_ACTIVITY_FRAGMENT
        );
        Mtools.log("PathSelectFragment  show  end");

        return pathSelectFragment;
    }

    @Override
    public PathSelectFragment getPathSelectFragment() {
        return pathSelectFragment;
    }

    @Override
    public AbstractFragmentDialog getDialogFragment() {
        return null;
    }
}
