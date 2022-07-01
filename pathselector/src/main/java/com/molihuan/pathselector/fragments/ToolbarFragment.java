package com.molihuan.pathselector.fragments;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.adapters.ToolbarOptionsAdapter;
import com.molihuan.pathselector.controllers.BuildControl;
import com.molihuan.pathselector.dao.SelectOptions;
import com.molihuan.pathselector.dialogs.PathSelectDialog;
import com.molihuan.pathselector.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ToolbarFragment
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/26 15:47
 */
public final class ToolbarFragment extends AbstractToolbarFragment {

    private ImageView imgvBack;//返回键
    private ImageView imgvOptions;//更多选项
    private TextView tvMainTitle;//主标题
    private TextView tvSubtitleTitle;//二级标题(跑马灯还没实现)
    private TextView tvOneOption;//一个选项
    private RelativeLayout relToolbar;//父控件

    private String title;
    private String subtitle;
    private int toolbarBG;//背景色
    private int mainTitleColor;//主标题字颜色
    private int subtitleColor;//副标题字颜色
    private int optionColor;//选项字颜色
    private int optionSize;//选项字大小18
    private String[] optionNames;//选项名数组
    private PopupWindow mOptionsPopupWindow;

    protected SelectOptions mSelectOptions;

    @Override
    public int getFragmentViewId() {
        return R.layout.fragment_toolbar_mlh;
    }

    @Override
    public void getComponents(View view) {
        imgvBack=view.findViewById(R.id.imgv_back_toolbar);
        imgvOptions=view.findViewById(R.id.imgv_options_toolbar);
        tvMainTitle=view.findViewById(R.id.tv_main_title_toolbar);
        tvSubtitleTitle=view.findViewById(R.id.tv_subtitle_toolbar);
        tvOneOption=view.findViewById(R.id.tv_one_option_toolbar);
        relToolbar=view.findViewById(R.id.rel_toolbar);
    }



    @Override
    public void initData() {
        super.initData();
        mSelectOptions = SelectOptions.getInstance();
        title = mSelectOptions.toolbarMainTitle;
        subtitle = mSelectOptions.toolbarSubtitleTitle;
        toolbarBG = mSelectOptions.toolbarBG;
        mainTitleColor = mSelectOptions.toolbarMainTitleColor;
        subtitleColor = mSelectOptions.toolbarSubtitleColor;
        optionColor = mSelectOptions.toolbarOptionColor;
        optionSize = mSelectOptions.toolbarOptionSize;
        optionNames = mSelectOptions.optionsName;

    }

    @Override
    public void initView() {
        initOptionView();
        setTitles(title,subtitle);
        setColor(toolbarBG,mainTitleColor,subtitleColor,optionColor);
        setSize(19,17,optionSize);
    }



    //设置选项
    private void initOptionView() {
        if (optionNames==null||optionNames.length==0){
//            relToolbar.removeView(imgvOptions);
            imgvOptions.setVisibility(View.GONE);
            return;
        }
        if (optionNames.length==1){
//            relToolbar.removeView(imgvOptions);
            imgvOptions.setVisibility(View.GONE);
            tvOneOption.setVisibility(View.VISIBLE);
            tvOneOption.setText(optionNames[0]);

        }
    }



    /**
     * 设置大小
     * @param optionSize
     */
    public void setSize(int mainTitleSize, int subTitleSize, int optionSize) {
        if (subtitle!=null){
            tvMainTitle.setTextSize(mainTitleSize);
            tvSubtitleTitle.setTextSize(subTitleSize);
        }
        tvOneOption.setTextSize(optionSize);

    }

    /**
     * 设置颜色
     * @param toolbarBG
     * @param mainTitleColor
     * @param subtitleColor
     * @param optionColor
     */
    private void setColor(int toolbarBG, int mainTitleColor, int subtitleColor, int optionColor) {
        relToolbar.setBackgroundColor(toolbarBG);
        tvMainTitle.setTextColor(mainTitleColor);
        tvSubtitleTitle.setTextColor(subtitleColor);
        tvOneOption.setTextColor(optionColor);
    }

    /**
     * 设置标题
     * @param title
     * @param subtitle
     */
    public void setTitles(String title, String subtitle) {

        tvMainTitle.setText(title);
        if (subtitle==null) {//如果副标题为空则移除它
            tvSubtitleTitle.setVisibility(View.GONE);
        }else {
            tvSubtitleTitle.setText(subtitle);
        }

    }

    @Override
    public void setListeners() {
        imgvBack.setOnClickListener(this);
        imgvOptions.setOnClickListener(this);
        tvOneOption.setOnClickListener(this);
    }

    /**
     * 选项PopupWindow
     */
    protected void showOptionsPopupWindow() {
        if (optionNames==null||optionNames.length<=1)return;

        if (mOptionsPopupWindow != null) {
            mOptionsPopupWindow.showAtLocation(relToolbar, Gravity.RIGHT|Gravity.TOP,0,0);//显示位置
            return;
        }

        View popView = LayoutInflater.from(mActivity).inflate(R.layout.general_recyview_mlh, null);//加载布局文件
        mOptionsPopupWindow = new PopupWindow(popView, relToolbar.getWidth()/3, ViewGroup.LayoutParams.WRAP_CONTENT);//设置宽度高度
        mOptionsPopupWindow.setFocusable(true);
        mOptionsPopupWindow.setOutsideTouchable(true);
        RecyclerView recyclerView = popView.findViewById(R.id.general_recyclerview_mlh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        ToolbarOptionsAdapter adapter = new ToolbarOptionsAdapter(Arrays.asList(optionNames));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);//设置监听
        mOptionsPopupWindow.showAtLocation(relToolbar, Gravity.RIGHT|Gravity.TOP,0,0);//显示位置

    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int i) {
        if (adapter instanceof ToolbarOptionsAdapter){
            mOptionsPopupWindow.dismiss();
            optionItemClick(view,i);
        }
    }

    /**
     * optionItem点击
     * @param view
     * @param i
     */
    protected void optionItemClick(View view, int i) {

        PathSelectFragment psf;
        //获取数据源
        if (BuildControl.buidlType==Constants.BUILD_DIALOG){
            psf = PathSelectDialog.mPathSelectFragment;
        }else {
            psf = (PathSelectFragment) mSelectOptions.fragmentManager.findFragmentByTag(Constants.TAG_FRAGMENT_PATHSELECT);
        }

        //监听回调
        mSelectOptions.optionListeners[i].onOptionClick(view,
                psf.getCurrentPath(),
                psf.getFileList(),
                psf.getCallBackData(),
                psf.getTabbarFileListAdapter(),
                psf.getFileListAdapter(),
                psf.getCallBackFileBeanList()
        );
        //Activity销毁模式回调
        if (mSelectOptions.optionsNeedCallBack[i]) {
            List<String> data = psf.getCallBackData();
            callBackData(data);
        }
    }

    /**
     * 销毁选择器
     * 返回数据、路径
     */
    public void callBackData(List<String> data) {
        //没有数据
        if (data==null||data.isEmpty()) {
            Toast.makeText(mActivity,"你还没有选择呢！",Toast.LENGTH_LONG).show();
            return;
        }
        //不为空
        Intent result = new Intent();
        result.putStringArrayListExtra(Constants.CALLBACK_DATA_ARRAYLIST_STRING, (ArrayList<String>) data);
        mActivity.setResult(mActivity.RESULT_OK, result);//设置返回原界面的结果
        mActivity.finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.imgv_back_toolbar){
            mActivity.finish();
            //返回键
        }else if (id==R.id.imgv_options_toolbar){
            //选项图标多个选项
            showOptionsPopupWindow();
        } else if (id==R.id.tv_one_option_toolbar){
            //一个选项
            optionItemClick(v,0);
        }
    }


}
