package com.molihuan.pathselectdemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.molihuan.pathselectdemo.R;
import com.molihuan.pathselectdemo.fragments.CustomTitlebarFragment;
import com.molihuan.pathselector.PathSelector;
import com.molihuan.pathselector.configs.PathSelectorConfig;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.entity.FontBean;
import com.molihuan.pathselector.fragment.BasePathSelectFragment;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.listener.CommonItemListener;
import com.molihuan.pathselector.listener.FileItemListener;
import com.molihuan.pathselector.utils.FragmentTools;
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
    private Button btnTest;

    private PathSelectFragment selector;
    private long firstBackTime;


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
        btnTest = findViewById(R.id.btn_test);
    }

    private void initData() {
        //开启调试模式，生成环境请关闭
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
        btnTest.setOnClickListener(this);
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
                customTitlebarSelectShow();
//                t1();
                break;
            case R.id.btn_test:
                FragmentTools.fragmentReplace(
                        getSupportFragmentManager(),
                        R.id.fragment_select_show_area,
                        new CustomTitlebarFragment(),
                        "55"
                );
                break;
        }
    }


    /**
     * dialog方式
     */
    private void dialogSelectShow() {
        //获取PathSelectFragment实例onBackPressed中处理返回按钮点击事件
        selector = PathSelector.build(MainActivity.this, MConstants.BUILD_DIALOG)
                .setMorePopupItemListeners(
                        new CommonItemListener("SelectAll") {
                            @Override
                            public boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                /**取消dialog弹窗
                                 * pathSelectFragment.getSelectConfigData().buildController.getDialogFragment().dismiss();
                                 */

                                //pathSelectFragment.selectAllFile(true);
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
     * Fragment方式
     */
    private void fragmentSelectShow() {


        selector = PathSelector.build(this, MConstants.BUILD_FRAGMENT)
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
        selector = PathSelector.build(this, MConstants.BUILD_ACTIVITY)
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
     * 自定义Toolbar方式
     */
    private void customTitlebarSelectShow() {
        PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
                .setTitlebarFragment(new CustomTitlebarFragment())
                .show();
    }

    /**
     * 基本配置
     */
    private void CompleteConfiguration() {

        PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
                //.setBuildType(MConstants.BUILD_DIALOG)//已经在build中已经设置了
                //.setContext(this)//已经在build中已经设置了
                .setRootPath("/storage/emulated/0/")//初始路径
                .setShowSelectStorageBtn(true)//是否显示内部存储选择按钮
                .setShowTitlebarFragment(true)//是否显示标题栏
                .setShowTabbarFragment(true)//是否显示面包屑
                .setAlwaysShowHandleFragment(true)//是否总是显示长按弹出选项
                .setShowFileTypes("", "mp3", "mp4")//只显示(没有后缀)或(后缀为mp3)或(后缀为mp4)的文件
                .setSelectFileTypes("", "mp3")//只能选择(没有后缀)或(后缀为mp3)的文件
                .setMaxCount(3)//最多可以选择3个文件,默认是-1不限制
                .setRadio()//单选,默认多选
                .setSortType(MConstants.SORT_NAME_ASC)//按名称排序
                .setTitlebarMainTitle(new FontBean("My Selector"))//设置标题栏主标题,还可以设置字体大小,颜色等
                .setTitlebarBG(Color.GREEN)//设置标题栏背景颜色
                .setFileItemListener(//设置文件item点击回调(点击是文件才会回调,如果点击是文件夹则不会)
                        new FileItemListener() {
                            @Override
                            public boolean onClick(View v, FileBean file, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                Mtools.toast("you clicked path:\n" + file.getPath());
                                return false;
                            }
                        }
                )
                .setMorePopupItemListeners(//设置右上角选项回调
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
                .setHandleItemListeners(//设置长按弹出选项回调
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

    @Override
    public void onBackPressed() {

        //让pathSelectFragment先处理返回按钮点击事件
        if (selector != null && selector.onBackPressed()) {
            return;
        }

        if (System.currentTimeMillis() - firstBackTime > 2000) {
            Mtools.toast("再按一次返回键退出程序");
            firstBackTime = System.currentTimeMillis();
            return;
        }

        super.onBackPressed();
    }


}