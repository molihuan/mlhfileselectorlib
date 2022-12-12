package com.molihuan.pathselector.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
public class MorePopupAdapter extends BaseQuickAdapter<CommonItemListener, BaseViewHolder> {


    public MorePopupAdapter(int layoutResId, @Nullable List<CommonItemListener> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CommonItemListener item) {
        FontBean fontBean = item.getFontBean();
        TextView tv = holder.getView(R.id.general_item_textview_mlh);


        tv.setText(fontBean.getText());
        tv.setTextColor(fontBean.getColor());
        tv.setTextSize(fontBean.getSize());
    }
}
