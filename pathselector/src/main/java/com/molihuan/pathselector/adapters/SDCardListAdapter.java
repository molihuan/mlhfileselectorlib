package com.molihuan.pathselector.adapters;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.utils.Constants;

import java.util.List;

/**
 * @ClassName
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/26 20:55
 */
public class SDCardListAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements LoadMoreModule {
    public SDCardListAdapter(List<String> data) {
        super(R.layout.general_item_tv_mlh, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        TextView tv = holder.getView(R.id.generl_textview_item_mlh);
        if (s.equals(Constants.DEFAULT_ROOTPATH)){
            tv.setText("内部存储");
        }else {
            tv.setText(s);
        }
    }
}
