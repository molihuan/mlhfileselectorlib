package com.molihuan.pathselector.dialog.impl;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;

import com.molihuan.pathselector.R;
import com.molihuan.pathselector.dialog.AbstractFragmentDialog;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.utils.FragmentTools;
import com.molihuan.pathselector.utils.MConstants;
import com.molihuan.pathselector.utils.Mtools;

/**
 * @ClassName: PathSelectDialog
 * @Author: molihuan
 * @Date: 2022/11/22/15:28
 * @Description: build type Dialog
 */
public class PathSelectDialog extends AbstractFragmentDialog {
    private PathSelectFragment pathSelectFragment;

    @Override
    public int setFragmentViewId() {
        return R.layout.dialog_path_select_mlh;
    }

    @Override
    public void getComponents(View view) {

    }

    @Override
    public void initData() {

        pathSelectFragment = new PathSelectFragment();

    }

    @Override
    public void initView() {
        super.initView();

        mConfigData.fragmentManager = getChildFragmentManager();

        Mtools.log("pathSelectFragment  show  start");
        FragmentTools.fragmentShowHide(
                mConfigData.fragmentManager,
                R.id.framelayout_dialog_show_body_mlh,
                pathSelectFragment,
                MConstants.TAG_ACTIVITY_FRAGMENT,
                true
        );
        Mtools.log("pathSelectFragment  show  end");

    }

    @Override
    public PathSelectFragment getPathSelectFragment() {
        return pathSelectFragment;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        //会有两次点击先消费掉一次
        if (event.getAction() != KeyEvent.ACTION_DOWN) {
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //此处捕获back操作，如果不希望所在的Activity监听到back键，需要返回true，消费掉。
            //让fragment先处理返回按钮事件
            if (pathSelectFragment != null && pathSelectFragment.onBackPressed()) {
                return true;
            } else {
                dismissAllowingStateLoss();
                return true;
            }

        } else {
            //这里注意当不是返回键时需将事件扩散，否则无法处理其他点击事件
            return false;
        }


    }
}
