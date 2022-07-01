package com.molihuan.pathselector.adapters;

import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.controllers.BuildControl;
import com.molihuan.pathselector.utils.Constants;
import com.molihuan.pathselector.utils.DisplayTools;

import java.util.List;

/**
 * @ClassName MoreChooseListAdapter
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/25 16:50
 */
public class MoreChooseListAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements LoadMoreModule {
    private int dataNumber;
    public MoreChooseListAdapter(List<String> data) {
        super(R.layout.item_more_choose_mlh, data);
        dataNumber=data.size();
    }

    @Override
    protected void convert(BaseViewHolder helper, String s) {
        TextView textView = helper.getView(R.id.btn_more_choose_item);
        RelativeLayout relativeLayout = helper.getView(R.id.relal_more_choose_item);
        //获取总宽度并设置item宽度
        ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
        int width;
        switch (BuildControl.buidlType){
            case Constants.BUILD_DIALOG:
                //获取弹窗宽度
                width =BuildControl.mPathSelectDialog.getView().getWidth();
                break;
            default:
                //获取屏幕宽度
                width = DisplayTools.getScreenWidth(getContext());
                break;
        }
        params.width = width / dataNumber;
        relativeLayout.setLayoutParams(params);//设置item宽度
        textView.setText(s);
    }
}
