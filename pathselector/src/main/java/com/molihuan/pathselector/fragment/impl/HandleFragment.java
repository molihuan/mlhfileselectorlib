package com.molihuan.pathselector.fragment.impl;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.molihuan.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.adapter.HandleListAdapter;
import com.molihuan.pathselector.entity.FontBean;
import com.molihuan.pathselector.fragment.AbstractHandleFragment;
import com.molihuan.pathselector.listener.CommonItemListener;
import com.molihuan.pathselector.utils.MConstants;

import java.util.Arrays;

/**
 * @ClassName: HandleFragment
 * @Author: molihuan
 * @Date: 2022/11/22/18:19
 * @Description:
 */
public class HandleFragment extends AbstractHandleFragment implements OnItemClickListener {

    protected RecyclerView mRecView;


    protected CommonItemListener[] itemListeners;     //选项数组
    protected HandleListAdapter handleListAdapter;     //适配器
    protected FontBean fontBean;                      //字样式
    protected boolean isDialogBuild;                   //是否是dialog模式

    @Override
    public int setFragmentViewId() {
        return R.layout.fragment_handle_mlh;
    }

    @Override
    public void getComponents(View view) {
        mRecView = view.findViewById(R.id.recv_handle);
    }


    @Override
    public void initData() {
        super.initData();

        itemListeners = mConfigData.handleItemListeners;
        fontBean = itemListeners[0].getFontBean();//只需要一份样式

        if (mConfigData.buildType == MConstants.BUILD_DIALOG) {
            isDialogBuild = true;
        }


    }


    @Override
    public void initView() {
        //通过回调的方法获取mRecView宽度并设置其item宽度并设置数据适配器
        SizeUtils.forceGetViewSize(mRecView, new SizeUtils.OnGetSizeListener() {
            @Override
            public void onGetSize(View view) {
                //计算 mRecView item宽度
                int width = view.getMeasuredWidth() / itemListeners.length;
                //设置适配器
                mRecView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
                //TODO  Arrays.asList 返回的类型不是 java.util.ArrayList 而是 java.util.Arrays.ArrayList 返回的 ArrayList 对象是只读的
                handleListAdapter = new HandleListAdapter(R.layout.item_handle_mlh, Arrays.asList(itemListeners), width);
                mRecView.setAdapter(handleListAdapter);
                handleListAdapter.setOnItemClickListener(HandleFragment.this);
            }
        });

    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View v, int position) {
        if (adapter instanceof HandleListAdapter) {
            optionItemClick(v, position);
        }
    }

    /**
     * 点击option回调
     *
     * @param v 点击的视图
     * @param i 点击的索引
     */
    protected void optionItemClick(View v, int i) {
        itemListeners[i].onClick(v,
                psf.getSelectedFileList(),
                psf.getCurrentPath(),
                psf
        );
    }


}
