package com.molihuan.pathselector.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.entity.FontBean;
import com.molihuan.pathselector.listener.CommonItemListener;

import java.util.List;

/**
 * @ClassName: MorePopupAdapter
 * @Author: molihuan
 * @Date: 2022/11/22/21:55
 * @Description:
 */
public class MorePopupAdapter extends BaseQuickAdapter<CommonItemListener, BaseViewHolder> implements LoadMoreModule {


    public MorePopupAdapter(int layoutResId, @Nullable List<CommonItemListener> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CommonItemListener item) {
        FontBean fontBean = item.getFontBean();
        RelativeLayout container = holder.getView(R.id.general_item_relatl_container_mlh);
        ImageView leftIco = holder.getView(R.id.general_item_imav_ico_mlh);
        TextView tv = holder.getView(R.id.general_item_textview_mlh);

        tv.setText(fontBean.getText());
        //如果已经设置了样式就不设置了
        if (item.setViewStyle(container, leftIco, tv)) {
            return;
        }

        if (fontBean.getLeftIcoResId() != null) {
            leftIco.setImageResource(fontBean.getLeftIcoResId());
            leftIco.setVisibility(View.VISIBLE);
        }


        tv.setTextColor(fontBean.getColor());
        tv.setTextSize(fontBean.getSize());
    }
}
