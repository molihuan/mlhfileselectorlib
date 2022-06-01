package com.zlylib.mlhfileselectorlib.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zlylib.mlhfileselectorlib.R;
import com.zlylib.mlhfileselectorlib.bean.BreadModel;

import java.util.List;

/**
 * BreadAdapter
 * Created by zhangliyang on 2020/6/20.
 */

public class BreadAdapter extends BaseQuickAdapter<BreadModel, BaseViewHolder> {

    public BreadAdapter(@Nullable List<BreadModel> data) {
        super(R.layout.bread_item,data);
        addChildClickViewIds(R.id.btn_bread);
    }

    @Override
    protected void convert(BaseViewHolder helper, BreadModel item) {
        helper.setText(R.id.btn_bread,item.getCurName());
    }
}
