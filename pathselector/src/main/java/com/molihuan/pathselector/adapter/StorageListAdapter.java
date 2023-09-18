package com.molihuan.pathselector.adapter;

import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.entity.StorageBean;

import java.util.List;

/**
 * @ClassName: StorageListAdapter
 * @Author: molihuan
 * @Date: 2022/11/22/21:55
 * @Description:
 */
public class StorageListAdapter extends BaseQuickAdapter<StorageBean, BaseViewHolder> implements LoadMoreModule {

    public StorageListAdapter(int layoutResId, @Nullable List<StorageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, StorageBean storageBean) {
        TextView tv = holder.getView(R.id.general_item_textview_mlh);
        tv.setText(storageBean.getRootPath());
        if (storageBean.getSelected()) {
            tv.setTextColor(Color.rgb(255, 165, 0));
        } else {
            tv.setTextColor(Color.GRAY);
        }

    }
}
