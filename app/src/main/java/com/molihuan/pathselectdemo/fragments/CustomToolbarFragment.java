package com.molihuan.pathselectdemo.fragments;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.molihuan.pathselectdemo.R;
import com.molihuan.pathselector.fragments.AbstractToolbarFragment;


/**
 * @ClassName CustomToolbarFragment
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/27 2:38
 */
public class CustomToolbarFragment extends AbstractToolbarFragment {
    private TextView btn1;
    private TextView btn2;
    private RelativeLayout relalToolbar;
    @Override
    public int getFragmentViewId() {
        return R.layout.item_tv;
    }

    @Override
    public void getComponents(View view) {
        btn1=view.findViewById(R.id.tv1);
        btn2=view.findViewById(R.id.tv2);
        relalToolbar=view.findViewById(R.id.relal_custom_toolbar);
    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void initView() {

    }

    @Override
    public void setListeners() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    protected void showOptionsPopupWindow(View view) {
        super.showOptionsPopupWindow(view);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int i) {
        super.onItemClick(adapter, view, i);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv1:
                super.viewClick(v,0);
                break;
            case R.id.tv2:
                super.showOptionsPopupWindow(relalToolbar);
                break;
        }

    }
}
