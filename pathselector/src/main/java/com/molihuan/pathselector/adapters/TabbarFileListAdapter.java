package com.molihuan.pathselector.adapters;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.entities.TabbarFileBean;

import java.util.List;

/**
 * @ClassName TabbarFileListAdapter
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/24 2:19
 */
public class TabbarFileListAdapter extends BaseQuickAdapter<TabbarFileBean, BaseViewHolder> implements LoadMoreModule {
    public TabbarFileListAdapter(List<TabbarFileBean> data) {
        super(R.layout.item_tabbar_files_list_mlh, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, TabbarFileBean fileBean) {
        TextView btn =holder.getView(R.id.btn_item_tabbar);
        btn.setText(fileBean.getFileName());
    }
}
