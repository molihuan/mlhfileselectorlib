package com.molihuan.pathselector.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.molihuan.pathselector.interfaces.IActivityAndFragment;

/**
 * @ClassName BaseActivity
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/24 0:54
 */

public abstract class BaseActivity extends AppCompatActivity implements IActivityAndFragment, View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentViewID());//设置布局
        getComponents();//获取组件
        initData();//初始化数据
        initView();//初始化视图
        setListeners();//设置监听
    }

    /**
     * 子类的数据初始化必须在此方法下，否则可能出现空指针异常
     * @param
     */

    public abstract int setContentViewID();
    public abstract void getComponents();
    public abstract void initView();
    public abstract void setListeners();
    public abstract void initData();


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v){

    }

    @Override
    public Object invoke(Object... parameters) {
        return null;
    }


}
