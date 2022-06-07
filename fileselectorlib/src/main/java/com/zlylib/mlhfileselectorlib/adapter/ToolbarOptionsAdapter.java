package com.zlylib.mlhfileselectorlib.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zlylib.mlhfileselectorlib.R;

import java.util.List;

/**
 * ToolbarOptionsAdapter
 * Created by molihuan on 2022/6/5.
 */

public class ToolbarOptionsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ToolbarOptionsAdapter(@Nullable List<String> data) {
        super(R.layout.item_toolbar_options,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_item_toolbar_options,item);
    }
}
