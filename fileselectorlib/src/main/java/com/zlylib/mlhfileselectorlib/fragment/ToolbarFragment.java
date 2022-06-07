package com.zlylib.mlhfileselectorlib.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zlylib.mlhfileselectorlib.R;


/**
 * author: molihuan
 * 2022.6.1
 * 通用返回toolbar  fragment
 */
public class ToolbarFragment extends BaseFragment {
    private String title;
    private String subtitle;
    private ImageView btn_back_toolbar;
    private TextView main_title_toolbar;
    private TextView subtitle_toolbar;
    private ImageView imgv_options_toolbar;

    /**
     * 设置标题
     * @param title
     */
    public ToolbarFragment(String title) {
        super(R.layout.fragment_back_toolbar_ml);
        this.title=title;
    }
    public ToolbarFragment(String title, String subtitle) {
        super(R.layout.fragment_back_toolbar_ml);
        this.title=title;
        this.subtitle=subtitle;
    }

    @Override
    public void initData(View view) {
        setTitles(title,subtitle);
    }

    @Override
    public void setListeners(View view) {
        btn_back_toolbar.setOnClickListener(this);
        imgv_options_toolbar.setOnClickListener(this);
    }

    @Override
    public void getComponents(View view) {
        btn_back_toolbar=view.findViewById(R.id.btn_back_toolbar);
        main_title_toolbar=view.findViewById(R.id.main_title_toolbar);
        subtitle_toolbar=view.findViewById(R.id.subtitle_toolbar);
        imgv_options_toolbar=view.findViewById(R.id.imgv_options_toolbar);
    }



    /**
     * 设置标题
     * @param title
     * @param subtitle
     */
    public void setTitles(String title, String subtitle) {
        main_title_toolbar.setText(title);
        if (subtitle==null) {//如果副标题为空则移除它
            ((RelativeLayout)subtitle_toolbar.getParent()).removeView(subtitle_toolbar);
        }else {
            subtitle_toolbar.setText(subtitle);
        }
    }

    @Override
    public void onClick(View v) {
        int ID=v.getId();

        if (mActivity == null) {
            mActivity = getActivity();
        }

        if (ID == R.id.btn_back_toolbar) {//toolbar返回按钮
            mIActivityAndFragment.invokeFuncAiF(R.id.btn_back_toolbar);
        }else if (ID == R.id.imgv_options_toolbar){
            mIActivityAndFragment.invokeFuncAiF(R.id.imgv_options_toolbar);
        }
    }

}