package com.molihuan.pathselector.activities;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.molihuan.pathselector.R;
import com.molihuan.pathselector.controllers.BuildControl;
import com.molihuan.pathselector.dao.SelectOptions;
import com.molihuan.pathselector.fragments.PathSelectFragment;
import com.molihuan.pathselector.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PathSelectActivity
 * @Description TODO 路径选择器主页面
 * @Author molihuan
 * @Date 2022/6/22 3:05
 */
public class PathSelectActivity extends BaseActivity {

    public PathSelectFragment mSelectFragment;//pathSelectFragment
    private SelectOptions mSelectOptions;

    @Override
    public int setContentViewID() {
        return R.layout.activity_path_select_mlh;
    }

    public void getComponents() {

    }

    @Override
    public void initData() {
        mSelectOptions = SelectOptions.getInstance();//获取实例
        mSelectOptions.frameLayoutID=R.id.framelayout_show_body_mlh;//设置显示的位置
    }

    @Override
    public void initView() {
        mSelectFragment = BuildControl.buildByFragment(this, mSelectOptions);//开始构建
    }
    @Override
    public void setListeners() {

    }

    @Override
    public  void onBackPressed() {
        //让Fragment接管返回按钮事件
        if (mSelectFragment!=null&&mSelectFragment.onBackPressed()){
            return;
        }
        super.onBackPressed();
    }

    /**
     * 销毁选择器
     * 返回数据、路径
     */
    public void callBackData(List<String> data) {
        //没有数据
        if (data==null||data.isEmpty()) {
            Toast.makeText(this,"你还没有选择呢！",Toast.LENGTH_LONG).show();
            return;
        }
        //不为空
        Intent result = new Intent();
        result.putStringArrayListExtra(Constants.CALLBACK_DATA_ARRAYLIST_STRING, (ArrayList<String>) data);
        setResult(RESULT_OK, result);//设置返回原界面的结果
        finish();
    }

    @Override
    public Object invoke(Object... parameters) {
        return null;
    }

    /**
     * 返回授权状态并存储
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}