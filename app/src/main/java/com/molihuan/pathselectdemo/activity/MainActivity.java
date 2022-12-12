package com.molihuan.pathselectdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.molihuan.pathselectdemo.R;
import com.molihuan.pathselector.PathSelector;
import com.molihuan.pathselector.configs.PathSelectorConfig;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.fragment.BasePathSelectFragment;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.listener.CommonItemListener;
import com.molihuan.pathselector.utils.MConstants;
import com.molihuan.pathselector.utils.Mtools;

import java.util.List;


/**
 * @ClassName MainActivity
 * @Description pathselector demo
 * @Author molihuan
 * @Date 2022/6/22 3:07
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnActivitySelector;
    private Button btnFragmentSelector;
    private Button btnDialogSelector;
    private Button btnCustomSelector;

    private PathSelectFragment selector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getComponents();//获取组件
        initData();//初始化数据
        initView();//初始化视图
        setListeners();//设置监听
    }


    private void getComponents() {
        btnActivitySelector = findViewById(R.id.btn_activity_selector);
        btnFragmentSelector = findViewById(R.id.btn_fragment_selector);
        btnDialogSelector = findViewById(R.id.btn_dialog_selector);
        btnCustomSelector = findViewById(R.id.btn_custom_toolbar_selector);
    }

    private void initData() {
        PathSelectorConfig.setDebug(true);
    }

    private void initView() {
        getSupportActionBar().hide();//隐藏ActionBar
    }

    private void setListeners() {
        btnActivitySelector.setOnClickListener(this);
        btnFragmentSelector.setOnClickListener(this);
        btnDialogSelector.setOnClickListener(this);
        btnCustomSelector.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_selector:
                dialogSelectShow();
                break;
            case R.id.btn_fragment_selector:
                fragmentSelectShow();
                break;
            case R.id.btn_activity_selector:
                activitySelectShow();
                break;
            case R.id.btn_custom_toolbar_selector:
                //customToolbarSelectShow();
                t1();
                break;
        }
    }

    private void t1() {
    }


    /**
     * dialog方式 详细选项设置请看本类中{@link #activitySelectShow()} 传送门
     */
    private void dialogSelectShow() {
        //获取PathSelectFragment实例onBackPressed中处理返回按钮点击事件
        selector = PathSelector.build(MainActivity.this, MConstants.BUILD_DIALOG)
                
                .setMorePopupItemListeners(
                        new CommonItemListener("SelectAll") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {

                                pathSelectFragment.selectAllFile(true);

                                return false;
                            }
                        },
                        new CommonItemListener("DeselectAll") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                pathSelectFragment.selectAllFile(false);
                                return false;
                            }
                        }
                )
                .setHandleItemListeners(
                        new CommonItemListener("OK") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                StringBuilder builder = new StringBuilder();
                                builder.append("you selected:\n");
                                for (FileBean fileBean : selectedFiles) {
                                    builder.append(fileBean.getPath() + "\n");
                                }
                                Mtools.toast(builder.toString());
                                return false;
                            }
                        },
                        new CommonItemListener("cancel") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                pathSelectFragment.openCloseMultipleMode(false);
                                return false;
                            }
                        }
                )
                .show();


    }


    /**
     * Fragment方式 详细选项设置请看本类中{@link #activitySelectShow()} 传送门
     */
    private void fragmentSelectShow() {


        selector = PathSelector.build(MainActivity.this, MConstants.BUILD_FRAGMENT)
                .setFrameLayoutId(R.id.fragment_select_show_area)

                .setMorePopupItemListeners(
                        new CommonItemListener("SelectAll") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {

                                pathSelectFragment.selectAllFile(true);

                                return false;
                            }
                        },
                        new CommonItemListener("DeselectAll") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                pathSelectFragment.selectAllFile(false);
                                return false;
                            }
                        }
                )
                .setHandleItemListeners(
                        new CommonItemListener("OK") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                StringBuilder builder = new StringBuilder();
                                builder.append("you selected:\n");
                                for (FileBean fileBean : selectedFiles) {
                                    builder.append(fileBean.getPath() + "\n");
                                }
                                Mtools.toast(builder.toString());
                                return false;
                            }
                        },
                        new CommonItemListener("cancel") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                pathSelectFragment.openCloseMultipleMode(false);
                                return false;
                            }
                        }
                )
                .show();


    }

    /**
     * Activity方式
     */
    private void activitySelectShow() {
        //Constants.BUILD_ACTIVITY为ACTIVITY模式
        selector = PathSelector.build(MainActivity.this, MConstants.BUILD_ACTIVITY)
                .setRequestCode(635)

                .setMorePopupItemListeners(
                        new CommonItemListener("SelectAll") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {

                                pathSelectFragment.selectAllFile(true);

                                return false;
                            }
                        },
                        new CommonItemListener("DeselectAll") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                pathSelectFragment.selectAllFile(false);
                                return false;
                            }
                        }
                )
                .setHandleItemListeners(
                        new CommonItemListener("OK") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                StringBuilder builder = new StringBuilder();
                                builder.append("you selected:\n");
                                for (FileBean fileBean : selectedFiles) {
                                    builder.append(fileBean.getPath() + "\n");
                                }
                                Mtools.toast(builder.toString());
                                return false;
                            }
                        },
                        new CommonItemListener("cancel") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                pathSelectFragment.openCloseMultipleMode(false);
                                return false;
                            }
                        }
                )
                .show();
    }

    /**
     * 自定义Toolbar方式 详细选项设置请看本类中{@link #activitySelectShow()} 传送门
     */
    private void customToolbarSelectShow() {

    }

    @Override
    public void onBackPressed() {

        //让pathSelectFragment先处理返回按钮点击事件
        if (selector != null && selector.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }


}