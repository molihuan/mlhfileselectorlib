package com.molihuan.demo01.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.molihuan.demo01.R;
import com.molihuan.demo01.fragments.EmptyFragment;
import com.z.fileselectorlib.FileSelectorSettings;
import com.z.fileselectorlib.Objects.BasicParams;
import com.z.fileselectorlib.Objects.FileInfo;
import com.zlylib.mlhfileselectorlib.FileSelector;
import com.zlylib.mlhfileselectorlib.SelectOptions;
import com.zlylib.mlhfileselectorlib.adapter.FileListAdapter;
import com.zlylib.mlhfileselectorlib.bean.FileBean;
import com.zlylib.mlhfileselectorlib.utils.Const;

import java.util.ArrayList;
/**
 * SelectCreator
 * Created by zhangliyang on 2020/6/20.
 * Updata by molihuan on 2022/6/7
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private Button btn_openFileChoose1;
    private Button btn_openFileChoose2;
    private Button btn_openFileChoose3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PermissionsTools.generalPermissionsOfStorage(this);
        //PermissionsTools.getAllNeedPermissions(this,getContentResolver());
        getComponent();
        setListener();
    }
    private void setListener() {
        btn_openFileChoose1.setOnClickListener(this);
        btn_openFileChoose2.setOnClickListener(this);
        btn_openFileChoose3.setOnClickListener(this);
    }
    private void getComponent() {
        btn_openFileChoose1=findViewById(R.id.btn_openFileChoose1);
        btn_openFileChoose2=findViewById(R.id.btn_openFileChoose2);
        btn_openFileChoose3=findViewById(R.id.btn_openFileChoose3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //文件选择器1
        if (requestCode == 100) {
            if(data!=null){
                ArrayList<String> essFileList = data.getStringArrayListExtra(Const.EXTRA_RESULT_SELECTION);
                StringBuilder builder = new StringBuilder();
                for (String file : essFileList) {
                    builder.append(file).append("");
                }
                ToastUtils.make().show(builder.toString());
            }
        }
        //文件选择器1

        //文件选择器2
        if (requestCode== FileSelectorSettings.PERMISSION_REQUEST_CODE && resultCode==FileSelectorSettings.BACK_WITH_SELECTIONS){
            assert data != null;
            Bundle bundle=data.getExtras();
            assert bundle != null;
            ArrayList<String> FilePathSelected
                    =bundle.getStringArrayList(FileSelectorSettings.FILE_PATH_LIST_REQUEST);
            for (String file_path :
                    FilePathSelected) {
                Log.v("file_sel", file_path);
            }
        }
        //文件选择器2

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_openFileChoose1 :
                openFileChoose1();
                break;
            case R.id.btn_openFileChoose2 :
                openFileChoose2();
                break;
            case R.id.btn_openFileChoose3 :
                break;
        }
    }


    /**
     * 文件选择器1
     */
    private void openFileChoose1() {
        /**
         *  设置 onlyShowFolder() 只显示文件夹 后 再设置setFileTypes（）不生效
         *  设置 onlyShowFolder() 只显示文件夹 后 默认设置了onlySelectFolder（）
         *  设置 onlySelectFolder() 只能选择文件夹 后 默认设置了isSingle（）
         *  设置 isSingle() 只能选择一个 后 再设置了setMaxCount（） 不生效
         */
        FileSelector.from(this)
                .onlyShowFolder()  //只显示文件夹
                .onlySelectFolder()  //只能选择文件夹
                .isSingle() // 只能选择一个
                .setMaxCount(5) //设置最大选择数
                .setFileTypes("png", "doc","apk", "mp3", "gif", "txt", "mp4", "zip", "pdf") //设置文件类型
                .setSortType(FileSelector.BY_NAME_ASC) //设置名字排序
                .setSortType(FileSelector.BY_TIME_ASC) //设置时间排序
                .setSortType(FileSelector.BY_SIZE_DESC) //设置大小排序
                .requestCode(100) //设置返回码
                .setTargetPath("/storage/emulated/0/") //设置默认目录
                .setToolbar(new EmptyFragment())//自定义Toolbar建议继承库中的BaseFragment
                //自定义Toolbar更多按钮以及监听事件
                .setMoreOPtions(new String[]{"选择","删除"},
                        new boolean[]{true,false},
                        new SelectOptions.IToolbarOptionsListener() {
                            @Override
                            public void onOptionClick(Context context, int position, String currentPath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter) {
                                ToastUtils.make().show("选择"+currentPath);
                            }
                        },
                        new SelectOptions.IToolbarOptionsListener() {
                            @Override
                            public void onOptionClick(Context context, int position, String currentPath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter) {
                                ToastUtils.make().show("删除"+currentPath);
                            }
                        }
                )
                //item点击/长按回调
                .setFileItemDispose(new SelectOptions.IOnFileItemListener() {
                    @Override
                    public void onFileItemClick(Context context, int position, String fileAbsolutePath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter) {
                        ToastUtils.make().show("点击"+fileAbsolutePath);
                    }

                    @Override
                    public void onLongFileItemClick(Context context, int position, String fileAbsolutePath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter) {
                        ToastUtils.make().show("长按"+fileAbsolutePath);
                    }
                })
                .start();
    }

    /**
     * 文件选择器2
     */
    private void openFileChoose2() {
        FileSelectorSettings settings=new FileSelectorSettings();
        settings.setRootPath(FileSelectorSettings.getSystemRootPath())//起始路径
                .setMaxFileSelect(2)//最大文件选择数
                .setTitle("请选择文件夹")//标题
                .setThemeColor("#3700B3")//主题颜色
                .setFileTypesToSelect(FileInfo.FileType.Folder)//可选择文件类型
                .setMoreOPtions(new String[]{"选择", "取消"},
                        new BasicParams.OnOptionClick() {
                            @Override
                            public void onclick(Activity activity, int position, String currentPath, ArrayList<String> FilePathSelected) {
                                ToastUtils.make().show(currentPath);
                            }
                        }, new BasicParams.OnOptionClick() {
                            @Override
                            public void onclick(Activity activity, int position, String currentPath, ArrayList<String> FilePathSelected) {
                                ToastUtils.make().show(currentPath);
                            }
                        })//更多功能拓展
                .show(MainActivity.this);//显示
    }
}