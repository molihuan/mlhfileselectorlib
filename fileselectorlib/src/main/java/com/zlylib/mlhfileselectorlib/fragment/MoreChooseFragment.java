package com.zlylib.mlhfileselectorlib.fragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.zlylib.mlhfileselectorlib.R;
import com.zlylib.mlhfileselectorlib.SelectOptions;
import com.zlylib.mlhfileselectorlib.adapter.MoreChooseAdapter;

import java.util.List;


/**
 * author: molihuan
 * 2022.6.1
 * 多选按钮Fragment
 */
public class MoreChooseFragment extends BaseFragment implements OnItemChildClickListener {

    private RecyclerView mMoreChooselistRecyclerView;
    private MoreChooseAdapter mMoreChooseAdapter;
    List<String> mMoreChooseName;


    public MoreChooseFragment(List<String> moreChooseName) {
        super(R.layout.fragment_show_hidden_morechoose_ml);
        mMoreChooseName=moreChooseName;
    }


    @Override
    public void initData(View view) {
        mMoreChooselistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mMoreChooseAdapter = new MoreChooseAdapter(mMoreChooseName);
        mMoreChooselistRecyclerView.setAdapter(mMoreChooseAdapter);
        mMoreChooseAdapter.onAttachedToRecyclerView(mMoreChooselistRecyclerView);
        mMoreChooseAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void setListeners(View view) {

    }

    @Override
    public void getComponents(View view) {
        mMoreChooselistRecyclerView=view.findViewById(R.id.recycv_morechoose_list);
    }


    /**
     * fragment隐藏显示监听
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden){
            mIActivityAndFragment.invokeFuncAiF(00011);
        }else {
            mIActivityAndFragment.invokeFuncAiF(00012);
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        SelectOptions.getInstance().getOnItemClicks()[position].onItemsClick( view,mFileSelectorActivity,position,mFileSelectorActivity.getmCurFolder(),mFileSelectorActivity.getmSelectedFileList(),mFileSelectorActivity.getmSelectedPathData(),mFileSelectorActivity.getmAdapter());

//        TextView textView = (TextView) view;
//        switch (position){
//            case 0://全选按钮
//                if (textView.getText().equals("全选")){
//                    mIActivityAndFragment.invokeFuncAiF(00011);
//                    textView.setText("取消全选");
//                }else {
//                    mIActivityAndFragment.invokeFuncAiF(00012);
//                    textView.setText("全选");
//                }
//                break;
//            case 1:
//                mIActivityAndFragment.invokeFuncAiF(00013);
//                break;
//        }
    }
}
