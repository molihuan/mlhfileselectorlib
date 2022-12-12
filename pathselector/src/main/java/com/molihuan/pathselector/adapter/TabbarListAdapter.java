package com.molihuan.pathselector.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.entity.TabbarFileBean;

import java.util.List;

/**
 * @ClassName: TabbarAdapter
 * @Author: molihuan
 * @Date: 2022/11/22/21:55
 * @Description:
 */
public class TabbarListAdapter extends BaseQuickAdapter<TabbarFileBean, BaseViewHolder> implements LoadMoreModule {

    public TabbarListAdapter(int layoutResId, @Nullable List<TabbarFileBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, TabbarFileBean item) {
        RelativeLayout relatContainer = holder.getView(R.id.relatl_item_tabbar_mlh);
        if (item.getPath() == null) {
            relatContainer.setVisibility(View.GONE);
        } else {
            relatContainer.setVisibility(View.VISIBLE);
        }
        TextView tv = holder.getView(R.id.tv_item_tabbar);
        tv.setText(item.getName());
    }
}
