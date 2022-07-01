package com.molihuan.pathselector.adapters;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.molihuan.pathselector.R;

import java.util.List;

/**
 * @ClassName ToolbarOptionsAdapter
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/26 20:55
 */
public class ToolbarOptionsAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements LoadMoreModule {
    public ToolbarOptionsAdapter(List<String> data) {
        super(R.layout.general_item_tv_mlh, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        TextView tv = holder.getView(R.id.generl_textview_item_mlh);
        tv.setText(s);
    }
}
