package com.zlylib.mlhfileselectorlib.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zlylib.mlhfileselectorlib.R;
import com.zlylib.mlhfileselectorlib.SelectOptions;

import java.util.ArrayList;


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
    private TextView tv_rigth;
    private ImageView imgv_options_toolbar;
    private RelativeLayout rel_toolbar;
    private int toolbarColor= Color.WHITE;
    private int mainTitleColor= Color.WHITE;
    private int subtitleColor= Color.WHITE;
    private int tv_rigth_color= Color.WHITE;
    private int tv_rigth_size= 18;

    /**
     * 设置标题
     * @param title
     */
    public ToolbarFragment(String title) {
        super(R.layout.fragment_back_toolbar_ml);
        //Log.e("","ToolbarFragment构造方法运行了");
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
        rel_toolbar.setBackgroundColor(toolbarColor);
        main_title_toolbar.setTextColor(mainTitleColor);
        subtitle_toolbar.setTextColor(subtitleColor);
        setOneMoreOption(tv_rigth_color,tv_rigth_size);
    }

    @Override
    public void setListeners(View view) {
        btn_back_toolbar.setOnClickListener(this);
        imgv_options_toolbar.setOnClickListener(this);
    }

    @Override
    public void getComponents(View view) {
        //Log.e("","getComponents运行了");
        btn_back_toolbar=view.findViewById(R.id.btn_back_toolbar);
        main_title_toolbar=view.findViewById(R.id.main_title_toolbar);
        subtitle_toolbar=view.findViewById(R.id.subtitle_toolbar);
        imgv_options_toolbar=view.findViewById(R.id.imgv_options_toolbar);
        rel_toolbar=view.findViewById(R.id.rel_toolbar);
        tv_rigth=view.findViewById(R.id.tv_rigth_option_toolbar);
    }




    /**
     * 设置Toolbar颜色
     * @param color
     */
    public ToolbarFragment setToolbarColor(int color) {
        this.toolbarColor=color;
        return this;
    }

    /**
     *
     */
    public void setOneMoreOption(int color,int size) {
        if (SelectOptions.getInstance().getOptionsName()==null){
            rel_toolbar.removeView(imgv_options_toolbar);
            return;
        }

        if (SelectOptions.getInstance().getOptionsName().length==1) {
            rel_toolbar.removeView(imgv_options_toolbar);
            tv_rigth.setVisibility(View.VISIBLE);
            String name = SelectOptions.getInstance().getOptionsName()[0];
            tv_rigth.setText(name);
            tv_rigth.setTextColor(color);
            tv_rigth.setTextSize(size);
            tv_rigth.setOnClickListener(this);
        }
    }

    public void setTitles(String title, String subtitle) {
        main_title_toolbar.setText(title);
        if (subtitle==null) {//如果副标题为空则移除它
            ((RelativeLayout)subtitle_toolbar.getParent()).removeView(subtitle_toolbar);
        }else {
            subtitle_toolbar.setText(subtitle);
        }
    }

    /**
     * 单个右边option
     * @param color
     * @return
     */
    public ToolbarFragment setTextRColor(int color) {
        this.tv_rigth_color=color;
        return this;
    }
    public ToolbarFragment setTextRSize(int size) {
        this.tv_rigth_size=size;
        return this;
    }
    /**
     * 设置MainTitle颜色
     * @param color
     */
    public ToolbarFragment setMainTitleColor(int color) {
        this.mainTitleColor=color;
        return this;
    }

    /**
     * 设置Subtitle颜色
     * @param color
     */
    public ToolbarFragment setSubtitleColor(int color) {
        this.subtitleColor=color;
        return this;
    }



    @Override
    public void onClick(View v) {
        int ID=v.getId();

        if (mActivity == null) {
            mActivity = getActivity();
        }

        if (ID==R.id.tv_rigth_option_toolbar){
            SelectOptions.getInstance().getOnOptionClicks()[0].onOptionClick(v, mActivity,0,mFileSelectorActivity.getmCurFolder(),mFileSelectorActivity.getmSelectedFileList(),mFileSelectorActivity.getmSelectedPathData(),mFileSelectorActivity.getmAdapter());
            if (SelectOptions.getInstance().getOptionsNeedCallBack()[0]) {
                ArrayList<String> selectedPathData = mFileSelectorActivity.getmSelectedPathData();
                selectedPathData.clear();//清理数据列表
                selectedPathData.add(mFileSelectorActivity.getmCurFolder());//添加当前路径
                mFileSelectorActivity.callBackPaths(selectedPathData);
            }
        }

        if (ID == R.id.btn_back_toolbar) {//toolbar返回按钮
            mIActivityAndFragment.invokeFuncAiF(R.id.btn_back_toolbar);
        }else if (ID == R.id.imgv_options_toolbar){
            mIActivityAndFragment.invokeFuncAiF(R.id.imgv_options_toolbar);
        }
    }

}