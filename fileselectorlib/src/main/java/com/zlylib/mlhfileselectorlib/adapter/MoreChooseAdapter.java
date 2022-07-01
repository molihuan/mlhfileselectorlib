package com.zlylib.mlhfileselectorlib.adapter;


import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zlylib.mlhfileselectorlib.R;
import com.zlylib.mlhfileselectorlib.utils.DisplayTools;

import java.util.List;

/**
 * BreadAdapter
 * Created by zhangliyang on 2020/6/20.
 */

public class MoreChooseAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int dataNumber;

    public MoreChooseAdapter(@Nullable List<String> data) {
        super(R.layout.item_more_choose,data);
        dataNumber=data.size();
        addChildClickViewIds(R.id.btn_more_choose_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView textView = helper.getView(R.id.btn_more_choose_item);
        RelativeLayout relativeLayout = helper.getView(R.id.rl_container);
        int screenWidth = DisplayTools.getScreenWidth(getContext());
        ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
        params.width = screenWidth / dataNumber;
        relativeLayout.setLayoutParams(params);//设置item宽度
        textView.setText(item);
    }
}
