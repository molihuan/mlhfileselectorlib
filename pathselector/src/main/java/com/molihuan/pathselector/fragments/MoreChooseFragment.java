package com.molihuan.pathselector.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.adapters.MoreChooseListAdapter;
import com.molihuan.pathselector.controllers.BuildControl;
import com.molihuan.pathselector.dao.SelectOptions;
import com.molihuan.pathselector.dialogs.PathSelectDialog;
import com.molihuan.pathselector.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName MoreChooseFragment
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/25 16:42
 */
public class MoreChooseFragment extends BaseFragment implements OnItemClickListener {
    private RecyclerView mMoreChooseRecyclerView;
    private MoreChooseListAdapter mMoreChooseListAdapter;
    private List<String> mMoreChooseName;
    private SelectOptions mSelectOptions;

    public MoreChooseFragment(List<String> mMoreChooseName) {
        this.mMoreChooseName = mMoreChooseName;
    }

    @Override
    public int getFragmentViewId() {
        return R.layout.fragment_morechoose_mlh;
    }

    @Override
    public void getComponents(View view) {
        mMoreChooseRecyclerView=view.findViewById(R.id.recycv_morechoose_list);
    }

    @Override
    public void initData() {
        mSelectOptions = SelectOptions.getInstance();
        mMoreChooseName = Arrays.asList(mSelectOptions.MoreChooseItemName);
    }

    @Override
    public void initView() {

        mMoreChooseRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));

        mMoreChooseListAdapter = new MoreChooseListAdapter(mMoreChooseName);
        mMoreChooseRecyclerView.setAdapter(mMoreChooseListAdapter);
    }

    @Override
    public void setListeners() {
        mMoreChooseListAdapter.setOnItemClickListener(this);
    }



    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {

        PathSelectFragment psf;
        //获取数据源
        if (BuildControl.buidlType==Constants.BUILD_DIALOG){
            psf = PathSelectDialog.mPathSelectFragment;
        }else {
            psf = (PathSelectFragment) mSelectOptions.fragmentManager.findFragmentByTag(Constants.TAG_FRAGMENT_PATHSELECT);
        }

        //监听回调
        mSelectOptions.moreChooseItemListeners[i].onItemsClick(view,
                psf.getCurrentPath(),
                psf.getFileList(),
                psf.getCallBackData(),
                psf.getTabbarFileListAdapter(),
                psf.getFileListAdapter(),
                psf.getCallBackFileBeanList()
        );
        //Activity销毁模式回调
        if (mSelectOptions.moreChooseItemNeedCallBack[i]) {
            List<String> data = psf.getCallBackData();
            callBackData(data);
        }
    }

    /**
     * 销毁选择器
     * 返回数据、路径
     */
    public void callBackData(List<String> data) {
        //没有数据
        if (data==null||data.isEmpty()) {
            Toast.makeText(mActivity,"你还没有选择呢！",Toast.LENGTH_LONG).show();
            return;
        }
        //不为空
        Intent result = new Intent();
        result.putStringArrayListExtra(Constants.CALLBACK_DATA_ARRAYLIST_STRING, (ArrayList<String>) data);
        mActivity.setResult(mActivity.RESULT_OK, result);//设置返回原界面的结果
        mActivity.finish();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * fragment隐藏显示监听
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


}
