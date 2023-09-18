package com.molihuan.pathselector.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.molihuan.pathselector.dao.SelectConfigData;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.interfaces.IActivityAndFragment;
import com.molihuan.pathselector.service.impl.ConfigDataBuilderImpl;

import java.util.Map;


/**
 * @ClassName: AbstractActivity
 * @Author: molihuan
 * @Date: 2022/11/22/13:07
 * @Description: 自定义路径选择的Activity必须继承此类
 */
public abstract class AbstractActivity extends AppCompatActivity implements IActivityAndFragment {

    protected SelectConfigData mConfigData = ConfigDataBuilderImpl.getInstance().getSelectConfigData();

    protected PathSelectFragment pathSelectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局资源
        setContentView(setContentViewID());
        //获取组件
        getComponents();
        //初始化数据
        initData(savedInstanceState);
        //初始化视图
        initView(savedInstanceState);
        //设置监听
        setListeners();
    }

    public abstract @LayoutRes
    int setContentViewID();

    public void getComponents() {
    }

    public void initData(Bundle savedInstanceState) {

    }


    public void initView(Bundle savedInstanceState) {
        hideActionBar();
    }

    public void setListeners() {
    }


    protected void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    /**
     * 与Fragment通讯接口
     *
     * @param data
     * @return
     */
    @Override
    public Map invoke(Map data) {
        return null;
    }

    /**
     * 返回按钮监听
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 处理一些需要存储的权限
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
