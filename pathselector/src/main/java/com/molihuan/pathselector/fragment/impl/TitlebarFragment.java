package com.molihuan.pathselector.fragment.impl;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.adapter.MorePopupAdapter;
import com.molihuan.pathselector.dialog.impl.SelectStorageDialog;
import com.molihuan.pathselector.entity.FontBean;
import com.molihuan.pathselector.fragment.AbstractTitlebarFragment;
import com.molihuan.pathselector.listener.CommonItemListener;
import com.molihuan.pathselector.utils.MConstants;

import java.util.Arrays;

/**
 * @ClassName: TitlebarFragment
 * @Author: molihuan
 * @Date: 2022/11/22/18:15
 * @Description:
 */
public class TitlebarFragment extends AbstractTitlebarFragment implements View.OnClickListener, OnItemClickListener {

    protected View positionView;                      //定位视图
    protected RelativeLayout relParent;               //父控件
    protected ImageView backImgView;                  //返回按钮
    protected ImageView storageImgView;                  //内存卡按钮

    protected ImageView searchImgView;                  //搜索按钮
    protected ImageView moreImgView;                  //更多选项
    protected TextView mainTitleTv;                   //主标题
    protected TextView subtitleTv;                    //副标题(跑马灯还没实现)
    protected TextView oneOptionTv;                   //一个选项
    protected PopupWindow optionsPopup;               //选项 PopupWindow
    protected MorePopupAdapter morePopupAdapter;               //选项 PopupWindow数据适配器
    protected CommonItemListener[] itemListeners;     //选项数组
    protected FontBean mainTitle;                     //主标题字样式
    protected FontBean subtitle;                      //副标题字样式
    protected boolean isDialogBuild;                   //是否是dialog模式

    protected SelectStorageDialog selectStorageDialog;


    @Override
    public int setFragmentViewId() {
        return R.layout.fragment_titlebar_mlh;
    }

    @Override
    public void getComponents(View view) {
        positionView = view.findViewById(R.id.view_position_titlebar);
        relParent = view.findViewById(R.id.rel_titlebar);
        backImgView = view.findViewById(R.id.imgv_back_titlebar);
        storageImgView = view.findViewById(R.id.imgv_storage_titlebar);

        searchImgView = view.findViewById(R.id.imgv_seach_titlebar);
        moreImgView = view.findViewById(R.id.imgv_more_options_titlebar);
        mainTitleTv = view.findViewById(R.id.tv_main_title_titlebar);
        subtitleTv = view.findViewById(R.id.tv_subtitle_titlebar);
        oneOptionTv = view.findViewById(R.id.tv_one_option_titlebar);
    }

    @Override
    public void initData() {
        super.initData();
        mainTitle = mConfigData.titlebarMainTitle;
        subtitle = mConfigData.titlebarSubtitleTitle;
        itemListeners = mConfigData.morePopupItemListeners;

        if (mConfigData.buildType == MConstants.BUILD_DIALOG) {
            isDialogBuild = true;
        }
    }

    @Override
    public void initView() {
        relParent.setBackgroundColor(mConfigData.titlebarBG);
        setViewSize();
        setTitleFont();
        setOptions();

    }

    protected void setViewSize() {

        if (isDialogBuild) {
            int icoSize = 65;

            relParent.getLayoutParams().height = 115;

            backImgView.getLayoutParams().height = icoSize;
            backImgView.getLayoutParams().width = icoSize;

            storageImgView.getLayoutParams().height = icoSize;
            storageImgView.getLayoutParams().width = icoSize;

            searchImgView.getLayoutParams().height = icoSize;
            searchImgView.getLayoutParams().width = icoSize;

            moreImgView.getLayoutParams().height = icoSize;
            moreImgView.getLayoutParams().width = icoSize;

        } else {

        }
    }

    protected void setOptions() {
        if (!mConfigData.showSelectStorageBtn) {
            storageImgView.setVisibility(View.GONE);
        }

        if (itemListeners == null || itemListeners.length == 0) {
            //没有选项
            moreImgView.setVisibility(View.GONE);
        } else if (itemListeners.length == 1) {
            //一个选项
            moreImgView.setVisibility(View.INVISIBLE);
            oneOptionTv.setVisibility(View.VISIBLE);
            FontBean font = itemListeners[0].getFontBean();

            oneOptionTv.setText(font.getText());
            oneOptionTv.setTextColor(font.getColor());
            oneOptionTv.setTextSize(font.getSize());
        } else {
            //多个选项的字样式设置通过Adapter来设置
        }
    }

    protected void setTitleFont() {
        if (mainTitle != null) {
            mainTitleTv.setText(mainTitle.getText());
            mainTitleTv.setTextColor(mainTitle.getColor());
            mainTitleTv.setTextSize(mainTitle.getSize());
        }
        if (subtitle != null) {
            subtitleTv.setText(subtitle.getText());
            subtitleTv.setTextColor(subtitle.getColor());
            subtitleTv.setTextSize(subtitle.getSize());
        }
    }


    @Override
    public void setListeners() {
        backImgView.setOnClickListener(this);
        storageImgView.setOnClickListener(this);
        searchImgView.setOnClickListener(this);
        moreImgView.setOnClickListener(this);
        oneOptionTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imgv_back_titlebar) {

            //返回按钮
            if (mConfigData.buildType == MConstants.BUILD_DIALOG) {
                mConfigData.buildController.getDialogFragment().dismissAllowingStateLoss();
            } else {
                mActivity.finish();
            }

        } else if (id == R.id.imgv_more_options_titlebar) {
            //更多按钮
            showPopupWindow();
        } else if (id == R.id.imgv_storage_titlebar) {
            //内存卡按钮
            showSelectStorageDialog();

        } else if (id == R.id.tv_one_option_titlebar) {
            //一个选项按钮
            optionItemClick(v, 0);
        } else if (id == R.id.imgv_seach_titlebar) {
            //搜索按钮

        }
    }

    public void showSelectStorageDialog() {
        if (selectStorageDialog == null) {
            selectStorageDialog = new SelectStorageDialog(mActivity);
        }
        selectStorageDialog.show();
    }

    // TODO 版本处理
    @SuppressWarnings("all")
    protected void showPopupWindow() {
        if (optionsPopup == null) {
            View popView = LayoutInflater.from(mActivity).inflate(R.layout.general_recyview_mlh, null);//加载布局文件
            optionsPopup = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//设置宽度高度
            optionsPopup.setFocusable(true);
            optionsPopup.setOutsideTouchable(true);
            optionsPopup.setElevation(3);//设置阴影 (注意阴影穿透---父组件和子组件必须都设置阴影)
            RecyclerView recyclerView = popView.findViewById(R.id.general_recyclerview_mlh);
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            //TODO  Arrays.asList 返回的类型不是 java.util.ArrayList 而是 java.util.Arrays.ArrayList 返回的 ArrayList 对象是只读的
            morePopupAdapter = new MorePopupAdapter(R.layout.general_item_tv_mlh, Arrays.asList(itemListeners));
            recyclerView.setAdapter(morePopupAdapter);
            morePopupAdapter.setOnItemClickListener(this);//设置监听
        }

        optionsPopup.showAsDropDown(positionView, 0, 0, Gravity.RIGHT);//显示位置

    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int i) {
        if (adapter instanceof MorePopupAdapter) {
            optionsPopup.dismiss();
            optionItemClick(view, i);
        }
    }

    /**
     * 点击option回调
     *
     * @param v 点击的视图
     * @param i 点击的索引
     */
    protected void optionItemClick(View v, int i) {
        itemListeners[i].onClick(v,
                psf.getSelectedFileList(),
                psf.getCurrentPath(),
                psf
        );
    }
}
