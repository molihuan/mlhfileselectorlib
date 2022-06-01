package com.zlylib.mlhfileselectorlib.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zlylib.mlhfileselectorlib.R;

import java.util.List;

/**
 * SelectSdcardAdapter
 * Created by zhangliyang on 2020/6/22.
 */

public class SelectSdcardAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SelectSdcardAdapter(@Nullable List<String> data) {
        super(R.layout.item_select_sdcard,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_item_select_sdcard,item);
    }
}
