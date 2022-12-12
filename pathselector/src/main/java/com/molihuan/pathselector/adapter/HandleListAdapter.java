package com.molihuan.pathselector.adapter;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.entity.FontBean;
import com.molihuan.pathselector.listener.CommonItemListener;

import java.util.List;

/**
 * @ClassName: HandleAdapter
 * @Author: molihuan
 * @Date: 2022/11/22/21:55
 * @Description:
 */
public class HandleListAdapter extends BaseQuickAdapter<CommonItemListener, BaseViewHolder> implements LoadMoreModule {
    private int itemWidth;

    private HandleListAdapter(int layoutResId, @Nullable List<CommonItemListener> data) {
        super(layoutResId, data);
    }

    public HandleListAdapter(int id, @Nullable List<CommonItemListener> data, int itemWidth) {
        this(id, data);
        this.itemWidth = itemWidth;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, CommonItemListener itemListener) {
        RelativeLayout relatl = holder.getView(R.id.item_handle_relatl_mlh);
        ImageView imgIco = holder.getView(R.id.item_handle_imav_ico_mlh);
        TextView tv = holder.getView(R.id.item_handle_tv_mlh);

        FontBean fontBean = itemListener.getFontBean();
        //设置宽度
        relatl.getLayoutParams().width = itemWidth;

        tv.setText(fontBean.getText());
        tv.setTextColor(fontBean.getColor());
        tv.setTextSize(fontBean.getSize());
    }
}
