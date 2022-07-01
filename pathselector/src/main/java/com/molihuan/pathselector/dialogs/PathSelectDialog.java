package com.molihuan.pathselector.dialogs;

import android.view.View;
import android.widget.FrameLayout;

import com.molihuan.pathselector.R;
import com.molihuan.pathselector.fragments.PathSelectFragment;
import com.molihuan.pathselector.utils.Constants;
import com.molihuan.pathselector.utils.FragmentTools;

/**
 * @ClassName PathSelectDialog
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/28 22:33
 */
public class PathSelectDialog extends BaseDialogFragment {

    private FrameLayout mFrameLayout;
    public static PathSelectFragment mPathSelectFragment;



    @Override
    public int getFragmentViewId() {
        return R.layout.dialog_path_select_mlh;
    }

    @Override
    public void getComponents(View view) {
        mFrameLayout=view.findViewById(R.id.framelayout_dialog_show_body_mlh);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        mPathSelectFragment = new PathSelectFragment()
                .setmIToolbarFragmentCallBack((fragment,isShow)->{
                    FragmentTools.fragmentShowHide(getChildFragmentManager(),
                            R.id.frameLayout_toolbar_area,
                            fragment,
                            Constants.TAG_FRAGMENT_TOOLBAR,
                            isShow);
                })
                .setmIMoreChooseCheckBoxCallBack((fragment,isShow)->{
                    FragmentTools.fragmentShowHide(getChildFragmentManager(),
                            R.id.frameLayout_morechoose_area,
                            fragment,
                            Constants.TAG_FRAGMENT_MORECHOOSE,
                            isShow);
                })
                ;
        FragmentTools.fragmentShowHide(getChildFragmentManager(),
                R.id.framelayout_dialog_show_body_mlh,
                mPathSelectFragment,
                Constants.TAG_FRAGMENT_PATHSELECT,
                true);
    }

    @Override
    public void setListeners() {

    }
}
