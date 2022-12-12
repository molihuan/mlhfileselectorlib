package com.molihuan.pathselector.activity.impl;


import com.molihuan.pathselector.R;
import com.molihuan.pathselector.activity.AbstractActivity;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.utils.FragmentTools;
import com.molihuan.pathselector.utils.MConstants;

/**
 * @ClassName: PathSelectActivity
 * @Author: molihuan
 * @Date: 2022/11/22/13:48
 * @Description: build type activity  路径选择器Activity页面
 */
public class PathSelectActivity extends AbstractActivity {

    private PathSelectFragment pathSelectFragment;

    @Override
    public int setContentViewID() {
        return R.layout.activity_path_select_mlh;
    }

    @Override
    public void getComponents() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        super.initView();
        pathSelectFragment = new PathSelectFragment();
        mConfigData.fragmentManager = getSupportFragmentManager();
        ////加载 PathSelectFragment
        FragmentTools.fragmentShowHide(
                mConfigData.fragmentManager,
                R.id.framelayout_show_body_mlh,
                pathSelectFragment,
                MConstants.TAG_ACTIVITY_FRAGMENT,
                true
        );
    }

    @Override
    public void onBackPressed() {
        //让fragment先处理返回按钮事件
        if (pathSelectFragment != null && pathSelectFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void setListeners() {

    }
}
