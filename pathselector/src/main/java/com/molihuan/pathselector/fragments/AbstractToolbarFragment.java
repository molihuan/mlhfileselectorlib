package com.molihuan.pathselector.fragments;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.adapters.ToolbarOptionsAdapter;
import com.molihuan.pathselector.controllers.BuildControl;
import com.molihuan.pathselector.dao.SelectOptions;
import com.molihuan.pathselector.dialogs.PathSelectDialog;
import com.molihuan.pathselector.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName AbstractToolbarFragment
 * @Description TODO 用于自定义ToolbarFragment的抽象类建议继承此类
 * @Author molihuan
 * @Date 2022/6/26 15:47
 */
public abstract class AbstractToolbarFragment extends BaseFragment implements OnItemClickListener {

    protected String[] optionNames;//选项名数组
    protected PopupWindow mOptionsPopupWindow;

    protected SelectOptions mSelectOptions;

    @Override
    public abstract int getFragmentViewId();

    @Override
    public abstract void getComponents(View view);


    @CallSuper
    @Override
    public void initData() {
        mSelectOptions = SelectOptions.getInstance();//获取实例
        optionNames = mSelectOptions.optionsName;//获取选项名
    }

    @Override
    public abstract void initView();


    @Override
    public abstract void setListeners();

    protected void showOptionsPopupWindow(View parent) {
        //默认父视图的右上角,宽度为父视图的/3,高度自适应
        showOptionsPopupWindow(parent,Gravity.RIGHT|Gravity.TOP,0,0,parent.getWidth()/3, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    /**
     * 简单选项PopupWindow 帮助快速构建
     * 本类所有的方法均可重写，具体怎么创造就看你自己了
     * @param parent 依附的视图
     * @param gravity 对齐方式
     * @param x 相对父视图偏移量
     * @param y 相对父视图偏移量
     * @param width PopupWindow宽度
     * @param height PopupWindow高度
     */
    protected void showOptionsPopupWindow(View parent, int gravity, int x, int y,int width, int height) {
        if (optionNames==null||optionNames.length<=1)return;

        if (mOptionsPopupWindow != null) {
            mOptionsPopupWindow.showAtLocation(parent, gravity,x,y);//显示在右上角位置
            return;
        }

        View popView = LayoutInflater.from(mActivity).inflate(R.layout.general_recyview_mlh, null);//加载布局文件
        mOptionsPopupWindow = new PopupWindow(popView, width, height);//设置宽度高度
        mOptionsPopupWindow.setFocusable(true);
        mOptionsPopupWindow.setOutsideTouchable(true);
        RecyclerView recyclerView = popView.findViewById(R.id.general_recyclerview_mlh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        ToolbarOptionsAdapter adapter = new ToolbarOptionsAdapter(Arrays.asList(optionNames));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);//设置监听
        mOptionsPopupWindow.showAtLocation(parent, Gravity.RIGHT|Gravity.TOP,0,0);//显示位置

    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int i) {
        if (adapter instanceof ToolbarOptionsAdapter){
            mOptionsPopupWindow.dismiss();
            optionItemClick(view,i);
        }
    }

    /**
     * optionItem点击
     * @param view
     * @param i
     */
    protected void optionItemClick(View view, int i) {

        PathSelectFragment psf;
        //获取数据源
        if (BuildControl.buidlType==Constants.BUILD_DIALOG){
            psf = PathSelectDialog.mPathSelectFragment;
        }else {
            psf = (PathSelectFragment) mSelectOptions.fragmentManager.findFragmentByTag(Constants.TAG_FRAGMENT_PATHSELECT);
        }

        if (mSelectOptions.optionListeners!=null) {
            //监听回调
            mSelectOptions.optionListeners[i].onOptionClick(view,
                    psf.getCurrentPath(),
                    psf.getFileList(),
                    psf.getCallBackData(),
                    psf.getTabbarFileListAdapter(),
                    psf.getFileListAdapter(),
                    psf.getCallBackFileBeanList()
            );
        }

        if (mSelectOptions.optionsNeedCallBack!=null) {
            //Activity销毁模式回调
            if (mSelectOptions.optionsNeedCallBack[i]) {
                List<String> data = psf.getCallBackData();
                callBackData(data);
            }
        }
    }

    /**
     * 一般视图点击回调
     * @param view
     * @param i
     */
    protected void viewClick(View view, int i) {
        PathSelectFragment psf;
        //获取数据源
        if (BuildControl.buidlType==Constants.BUILD_DIALOG){
            psf = PathSelectDialog.mPathSelectFragment;
        }else {
            psf = (PathSelectFragment) mSelectOptions.fragmentManager.findFragmentByTag(Constants.TAG_FRAGMENT_PATHSELECT);
        }

        if (mSelectOptions.toolbarListeners!=null) {
            //监听回调
            mSelectOptions.toolbarListeners[i].onClick(view,
                    psf.getCurrentPath(),
                    psf.getFileList(),
                    psf.getCallBackData(),
                    psf.getTabbarFileListAdapter(),
                    psf.getFileListAdapter(),
                    psf.getCallBackFileBeanList()
            );
        }

        if (mSelectOptions.toolbarViewNeedCallBack!=null) {
            //Activity销毁模式回调
            if (mSelectOptions.toolbarViewNeedCallBack[i]) {
                List<String> data = psf.getCallBackData();
                callBackData(data);
            }
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


}
