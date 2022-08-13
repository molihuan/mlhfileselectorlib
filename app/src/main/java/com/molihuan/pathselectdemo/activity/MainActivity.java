package com.molihuan.pathselectdemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.molihuan.pathselectdemo.R;
import com.molihuan.pathselectdemo.fragments.CustomToolbarFragment;
import com.molihuan.pathselector.PathSelector;
import com.molihuan.pathselector.adapters.FileListAdapter;
import com.molihuan.pathselector.adapters.TabbarFileListAdapter;
import com.molihuan.pathselector.dao.SelectOptions;
import com.molihuan.pathselector.entities.FileBean;
import com.molihuan.pathselector.fragments.PathSelectFragment;
import com.molihuan.pathselector.utils.Constants;
import com.molihuan.pathselector.utils.Mtools;

import java.util.List;

/**
 * @ClassName MainActivity
 * @Description TODO pathselector demo
 * @Author molihuan
 * @Date 2022/6/22 3:07
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnActivitySelector;
    private Button btnFragmentSelector;
    private Button btnDialogSelector;
    private Button btnCustomSelector;
    private PathSelectFragment mPathSelectFragment;

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
        btnActivitySelector=findViewById(R.id.btn_activity_selector);
        btnFragmentSelector=findViewById(R.id.btn_fragment_selector);
        btnDialogSelector=findViewById(R.id.btn_dialog_selector);
        btnCustomSelector=findViewById(R.id.btn_custom_toolbar_selector);
    }

    private void initData() {

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
        if (requestCode == 10011) {
            if(data!=null){
                List<String> pathData = data.getStringArrayListExtra(Constants.CALLBACK_DATA_ARRAYLIST_STRING);//获取数据
                StringBuilder builder = new StringBuilder();
                for (String path : pathData) {
                    builder.append(path).append("");
                }
                Mtools.toast(MainActivity.this,builder.toString());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dialog_selector :
                dialogSelectShow();
                break;
            case R.id.btn_fragment_selector :
                fragmentSelectShow();
                break;
            case R.id.btn_activity_selector :
                activitySelectShow();
                break;
            case R.id.btn_custom_toolbar_selector :
                customToolbarSelectShow();

                break;
        }
    }



    /**
     * 自定义Toolbar方式 详细选项设置请看本类中{@link #activitySelectShow()} 传送门
     *
     */
    private void customToolbarSelectShow() {
        //获取PathSelectFragment实例onBackPressed中处理返回按钮点击事件
        mPathSelectFragment = PathSelector.build(MainActivity.this, Constants.BUILD_ACTIVITY)
                .requestCode(10011)//请求码
                .showToolBarFragment(true)//是否显示ToolbarFragment 如果自定义必须为true 默认为true
                .setToolbarFragment(new CustomToolbarFragment())
                .setToolbarViewClickers(
                        new SelectOptions.onToolbarListener() {
                            @Override
                            public void onClick(View view, String currentPath, List<FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, FileListAdapter fileAdapter,List<FileBean> callBackFileBeanList) {
                                Mtools.toast(getBaseContext(),"点击了按钮1");
                            }
                        }
                )
                .setMoreOPtions(
                        new String[]{"全选", "删除"},
                        //lambda表达式
                        (view, currentPath, fileBeanList, callBackData, tabbarAdapter, fileAdapter,callBackFileBeanList) -> {
                            Mtools.toast(getBaseContext(), "点击了全选");
                        },
                        //匿名对象
                        new SelectOptions.onToolbarOptionsListener() {
                            @Override
                            public void onOptionClick(View view, String currentPath, List<FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, FileListAdapter fileAdapter,List<FileBean> callBackFileBeanList) {
                                Mtools.toast(getBaseContext(),"点击了删除");
                            }
                        }
                )
                .start();//开始构建
    }
    /**
     * dialog方式 详细选项设置请看本类中{@link #activitySelectShow()} 传送门
     */
    private void dialogSelectShow(){
        //获取PathSelectFragment实例onBackPressed中处理返回按钮点击事件
        mPathSelectFragment = PathSelector.build(MainActivity.this, Constants.BUILD_DIALOG)
                .isSingle()
                .frameLayoutID(R.id.fragment_select_show_area)//加载位置FrameLayout的ID
                .requestCode(10011)//请求码
                .showToolBarFragment(true)//是否显示ToolbarFragment
                //设置toolbarFragment更多选项item和其回调
                .setMoreOPtions(
                        new String[]{"选择"},
                        new boolean[]{false},
                        (view,currentPath, fileBeanList,callBackData,tabbarAdapter,fileAdapter,callBackFileBeanList) -> {
                            Mtools.toast(getBaseContext(),"点击了选择"+callBackData.get(0));
                        }
                )
                .start();//开始构建
    }
    /**
     * fragment方式 详细选项设置请看本类中{@link #activitySelectShow()} 传送门
     */
    private void fragmentSelectShow(){
        //获取PathSelectFragment实例onBackPressed中处理返回按钮点击事件
        mPathSelectFragment = PathSelector.build(MainActivity.this, Constants.BUILD_FRAGMENT)
                .frameLayoutID(R.id.fragment_select_show_area)//加载位置,FrameLayout的ID
                .requestCode(10011)//请求码
                .showToolBarFragment(false)//是否显示ToolbarFragment
                //设置多选item和其回调
                .setMoreChooseItems(
                        new String[]{"全选", "删除"},
                        (view,currentPath, fileBeanList,callBackData,tabbarAdapter,fileAdapter,callBackFileBeanList) -> {
                            Mtools.toast(getBaseContext(),"点击了全选");
                        },
                        (view,currentPath, fileBeanList,callBackData,tabbarAdapter,fileAdapter,callBackFileBeanList) -> {
                            Mtools.toast(getBaseContext(),"点击了删除"+callBackData.get(0));
                        }
                )
                .start();//开始构建
    }

    /**
     * activity方式
     */
    private void activitySelectShow(){
        //Constants.BUILD_ACTIVITY为ACTIVITY模式
        PathSelector.build(MainActivity.this, Constants.BUILD_ACTIVITY)
                .requestCode(10011)//请求码
                //.setRootPath("/storage/emulated/0/")//设置根目录(注意最后没有/)
                .setMaxCount(3)//设置最大选择数量，默认是-1不限制
                //.setToolbarFragment(new TestFragment())//加载自定义ToolbarFragment
                //.setMoreChooseFragment(new TestFragment())//加载自定义MoreChooseFragment
                //.setShowFileTypes("mp4","")//设置显示文件类型如果无后缀请使用""
                //.setSelectFileTypes("mp3","mp4","ppt","")//设置选择文件类型如果无后缀请使用""
                .setSortType(Constants.SORT_NAME_ASC)//设置排序类型
                //.isSingle()//单选模式不能多选
                .showToolBarFragment(true)//是否显示ToolbarFragment
                .setToolbarMainTitle("路径选择器")//设置ToolbarFragment主标题
                .setToolbarSubtitleTitle("MLH")//设置ToolbarFragment副标题
                .setToolbarSubtitleColor(Color.BLACK)//ToolbarFragment副标题颜色
                //设置多选item和其回调
                .setMoreChooseItems(
                        new String[]{"choose", "删除"},
                        //匿名对象
                        new SelectOptions.onMoreChooseItemsListener() {
                            @Override
                            public void onItemsClick(View view, String currentPath, List<FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, FileListAdapter fileAdapter, List<FileBean> callBackFileBeanList) {
                                Mtools.toast(getBaseContext(),"choose了 ："+callBackData.get(0));
                            }
                        },
                        //lambda表达式 为了简洁下面都使用lambda表达式
                        (view,currentPath, fileBeanList,callBackData,tabbarAdapter,fileAdapter,callBackFileBeanList) -> {
                            Mtools.toast(getBaseContext(),"点击了删除");
                        }
                )
                //设置toolbarFragment更多选项item和其回调
                .setMoreOPtions(
                        new String[]{"选择"},
                        new boolean[]{true},//选择结束后是否需要销毁当前Activity
                        (view,currentPath, fileBeanList,callBackData,tabbarAdapter,fileAdapter,callBackFileBeanList) -> {
                            Mtools.toast(getBaseContext(),"点击了选择"+callBackData.get(0));
                        }
                )
                //设置文件列表中FileItem和其回调
                .setFileItemListener(new com.molihuan.pathselector.dao.SelectOptions.onFileItemListener() {
                    @Override
                    public boolean onFileItemClick(View view, String currentPath, List<com.molihuan.pathselector.entities.FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, com.molihuan.pathselector.adapters.FileListAdapter fileAdapter,FileBean fileBean) {



                        return false;
                    }
                    @Override
                    public boolean onLongFileItemClick(View view, String currentPath, List<com.molihuan.pathselector.entities.FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, com.molihuan.pathselector.adapters.FileListAdapter fileAdapter,FileBean fileBean) {
                        return false;
                    }
                })
                .start();//开始构建
    }

    @Override
    public void onBackPressed() {
        //让PathSelectFragment先处理返回按钮点击事件
        if (mPathSelectFragment!=null&&mPathSelectFragment.onBackPressed()){
            return;
        }
        super.onBackPressed();
    }


}