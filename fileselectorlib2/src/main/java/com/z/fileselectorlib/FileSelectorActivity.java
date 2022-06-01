package com.z.fileselectorlib;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.z.fileselectorlib.Objects.BasicParams;
import com.z.fileselectorlib.Objects.FileInfo;
import com.z.fileselectorlib.Utils.FileUtil;
import com.z.fileselectorlib.Utils.PermissionUtil;
import com.z.fileselectorlib.Utils.StatusBarUtil;
import com.z.fileselectorlib.Utils.TimeUtil;
import com.z.fileselectorlib.adapter.FileListAdapter;
import com.z.fileselectorlib.adapter.NavigationAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FileSelectorActivity extends AppCompatActivity {

    private ArrayList<FileInfo> currentFileList = new ArrayList<>();
    private ArrayList<String> FileSelected = new ArrayList<>();
    private ArrayList<String> RelativePaths = new ArrayList<>();
    private String currentPath;
    private ListView lvFileList;
    private TextView tvTips;
    private TextView tv_select_num;
    private RelativeLayout llTopView;
    private ImageView imBack;
    private LinearLayout llBottomView;
    private Button select_confirm;
    private Button select_cancel;
    private RecyclerView navigation_view;
    private LinearLayout llRoot;
    private NavigationAdapter navigationAdapter;
    private ImageView imMore;
    private PopupWindow moreOptions;
    private Window window;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener itemLongClickListener;
    private FileListAdapter fileListAdapter;
    private Activity activity;
    private boolean onSelect=false;
    private int SelectNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selector);

        //init params
        tv_select_num =findViewById(R.id.select_num);
        tvTips =findViewById(R.id.tips);
        lvFileList=findViewById(R.id.FileList);
        imBack=findViewById(R.id.back);
        llTopView=findViewById(R.id.top_view);
        llBottomView=findViewById(R.id.bottom_view);
        select_confirm=findViewById(R.id.select_confirm);
        select_cancel=findViewById(R.id.select_cancel);
        navigation_view=findViewById(R.id.navigation_view);
        llRoot=findViewById(R.id.root);
        imMore=findViewById(R.id.more);
        activity=this;
        setOnItemClick();
        setOnItemLongClick();

        //init views
        initSelectNum();
        initTips();
        initBackBtn();
        initTopView();
        initBottomView();
        initRootButton();
        initMoreOptionsView();


        if (PermissionUtil.isStoragePermissionGranted(this)) {
            //get init files
            String initPath= BasicParams.getInstance().getRootPath();
            currentPath=initPath;
            File[] test=(new File(initPath)).listFiles();
            if (test!=null)getFileList(initPath);
            setFileList();
            setPathView(initPath);

        }

    }

    @Override
    public void onBackPressed() {
        if (onSelect){
            quitSelectMod();
        }
        else if (!currentPath.equals(BasicParams.BasicPath)){
            refreshFileList(new File(currentPath).getParent());
        }
        else super.onBackPressed();
    }

    private void initMoreOptionsView() {
        if (BasicParams.getInstance().isNeedMoreOptions())imMore.setVisibility(View.VISIBLE);
        else imMore.setVisibility(View.INVISIBLE);
        imMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加载布局
                LinearLayout layout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.popup_more_options, null);
                //找到布局的控件
                ListView listView = (ListView) layout.findViewById(R.id.options);
                //设置适配器
                listView.setAdapter(new ArrayAdapter<String>(activity,R.layout.option_list_item,R.id.option_text,BasicParams.getInstance().getOptionsName()));
                // 实例化popupWindow
                moreOptions = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //控制键盘是否可以获得焦点
                moreOptions.setFocusable(true);
                //设置popupWindow弹出窗体的背景
                moreOptions.setBackgroundDrawable(getDrawable(R.drawable.popwindow_white));
                BackGroundAlpha(0.6f);
                moreOptions.showAsDropDown(imMore,-imMore.getWidth()+50,-imMore.getHeight()+30);
                moreOptions.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        BackGroundAlpha(1.0f);
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        moreOptions.dismiss();
                        BasicParams.getInstance().getOnOptionClicks()[position].onclick(activity,position,currentPath,FileSelected);
                        //刷新列表
                        getFileList(currentPath);
                        fileListAdapter.updateFileList(currentFileList);
                        if (onSelect)fileListAdapter.clearSelections();
                    }
                });
            }
        });
    }

    public void BackGroundAlpha(float f) {
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.alpha = f;
        activity.getWindow().setAttributes(layoutParams);
    }

    private void initRootButton() {
        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rootPath= BasicParams.BasicPath;
                getFileList(rootPath);
                RelativePaths.clear();
                fileListAdapter.updateFileList(currentFileList);
                if (onSelect)fileListAdapter.clearSelections();
                navigationAdapter.UpdatePathList(RelativePaths);
            }
        });
    }

    private void setPathView(String initPath) {
        RelativePaths= FileUtil.getRelativePaths(initPath);
        navigation_view.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        navigationAdapter=new NavigationAdapter(this,RelativePaths);
        navigationAdapter.setRecycleItemClickListener(new NavigationAdapter.OnRecycleItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view, int position) {
                List<String>sublist=RelativePaths.subList(0,position+1);
                RelativePaths= new ArrayList<>(sublist);
                getFileList(FileUtil.mergeAbsolutePath(RelativePaths));
                fileListAdapter.updateFileList(currentFileList);
                if (onSelect)fileListAdapter.clearSelections();
                navigationAdapter.UpdatePathList(RelativePaths);
            }
        });
        navigation_view.setAdapter(navigationAdapter);
    }

    private void setFileList() {
        fileListAdapter =new FileListAdapter(currentFileList,this);
        lvFileList.setAdapter(fileListAdapter);
        lvFileList.setOnItemClickListener(onItemClickListener);
        lvFileList.setOnItemLongClickListener(itemLongClickListener);
    }

    private void initBottomView() {
        BottomViewShow(View.INVISIBLE,0);
        select_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                Bundle bundle_back=new Bundle();
                bundle_back.putStringArrayList(FileSelectorSettings.FILE_PATH_LIST_REQUEST,FileSelected);
                intent.putExtras(bundle_back);
                activity.setResult(FileSelectorSettings.BACK_WITH_SELECTIONS,intent);
                activity.finish();
            }
        });
        select_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitSelectMod();
            }
        });
    }

    private void quitSelectMod() {
        fileListAdapter.clearSelections();
        fileListAdapter.setSelect(false);
        BottomViewShow(View.INVISIBLE,0);
        onSelect=false;
        SelectNum=0;
        FileSelected.clear();
        changeSelectNum(0);
        tv_select_num.setVisibility(View.INVISIBLE);
    }

    private void setOnItemLongClick() {
        itemLongClickListener=new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!onSelect && currentFileList.get(position).getFileType() != FileInfo.FileType.Parent) {
                    tv_select_num.setVisibility(View.VISIBLE);
                    BottomViewShow(View.VISIBLE, 140);
                    fileListAdapter.setSelect(true);
                    onSelect=true;
                }
                return true;
            }
        };
    }

    public void BottomViewShow(int visible, int i) {
        llBottomView.setVisibility(visible);
        ViewGroup.LayoutParams params = llBottomView.getLayoutParams();
        params.height = i;
        llBottomView.setLayoutParams(params);
    }

    private void initBackBtn() {
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setResult(FileSelectorSettings.BACK_WITHOUT_SELECT);
                activity.finish();
            }
        });
    }

    private void initTopView() {
        int theme_color= Color.parseColor(BasicParams.getInstance().getColor());
        llTopView.setBackgroundColor(theme_color);
        window= this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
        window.setStatusBarColor(theme_color);
        if (ColorUtils.calculateLuminance(theme_color)>0.5) {
            StatusBarUtil.setStatusBarDarkTheme(this, true);
            tvTips.setTextColor(getColor(R.color.text_color_dark));
        }
        else {
            StatusBarUtil.setStatusBarDarkTheme(this, false);
            tvTips.setTextColor(getColor(R.color.text_color_light));
        }
    }

    private void initTips() {
        tvTips.setText(BasicParams.getInstance().getTips());
    }

    private void initSelectNum() {
        changeSelectNum(0);
        tv_select_num.setVisibility(View.INVISIBLE);
    }

    private void changeSelectNum(int num) {
        String selectNum=getString(R.string.selectNum);
        selectNum=String.format(selectNum,num, BasicParams.getInstance().getMaxSelectNum());
        tv_select_num.setText(selectNum);
    }

    private void setOnItemClick() {
        onItemClickListener=new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileInfo file_select=currentFileList.get(position);
                if (onSelect && file_select.getFileType() != FileInfo.FileType.Parent){
                    FileListAdapter.ViewHolder viewHolder = (FileListAdapter.ViewHolder) view.getTag();
                    if (file_select.FileFilter(BasicParams.getInstance().getSelectableFileTypes())) {
                        if (SelectNum < BasicParams.getInstance().getMaxSelectNum() || viewHolder.ckSelector.isChecked()) {
                            viewHolder.ckSelector.toggle();
                            if (file_select.getFileType() != FileInfo.FileType.Parent) {
                                fileListAdapter.ModifyFileSelected(position, viewHolder.ckSelector.isChecked());
                                if (viewHolder.ckSelector.isChecked()) {
                                    FileSelected.add(file_select.getFilePath());
                                } else {
                                    FileSelected.remove(file_select.getFilePath());
                                }
                                SelectNum = FileSelected.size();
                                changeSelectNum(SelectNum);
                            }
                        }
                    }else Toast.makeText(activity, "该文件类型不可选", Toast.LENGTH_SHORT).show();
                }else {
                    if (file_select.getFileType() == FileInfo.FileType.Folder ||
                            file_select.getFileType() == FileInfo.FileType.Parent) {
                        refreshFileList(file_select.getFilePath());
                    }
                }
            }
        };
    }

    private void refreshFileList(String path) {
        currentPath= path;
        getFileList(path);
        fileListAdapter.updateFileList(currentFileList);
        RelativePaths=FileUtil.getRelativePaths(path);
        navigationAdapter.UpdatePathList(RelativePaths);
        navigation_view.scrollToPosition(navigationAdapter.getItemCount()-1);
        if (onSelect){
            fileListAdapter.clearSelections();
        }
    }


    private void getFileList(String Path) {
        currentFileList.clear();
        File initFile = new File(Path);
        if (!Path.equals(BasicParams.BasicPath)){
            FileInfo fileInfo=new FileInfo();
            fileInfo.setFileName("返回上一级");
            fileInfo.setFileLastUpdateTime("");
            fileInfo.setFileType(FileInfo.FileType.Parent);
            fileInfo.setFilePath(initFile.getParent());
            fileInfo.setFileCount(-1);
            currentFileList.add(fileInfo);
        }
        File[] files = initFile.listFiles();
        if (files == null){
            Log.d("myfile", "can not list file");
            return;
        }
        List<File> file_list= Arrays.asList(files);
        FileUtil.SortFilesByName(file_list);
        for (File f : file_list) {
            if (f.getName().indexOf(".") != 0) {
                //隐藏文件不显示
                if (f.isDirectory()) {
                    //文件夹
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFileName(f.getName());
                    fileInfo.setFileLastUpdateTime(TimeUtil.getDateInString(new Date(f.lastModified())));
                    fileInfo.setFileType(FileInfo.FileType.Folder);
                    fileInfo.setFilePath(f.getPath());
                    fileInfo.setFileCount(FileUtil.getSubfolderNum(f.getPath()));
                    Log.d("myfile", fileInfo.getFileName() + ":" + fileInfo.getFileCount());
                    currentFileList.add(fileInfo);
                } else {
                    if (BasicParams.getInstance().isUseFilter()) {
                        if (!FileUtil.fileFilter(f.getPath(),BasicParams.getInstance().getFileTypeFilter()))
                            continue;
                    }
                    if (FileUtil.isAudioFileType(f.getPath())) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setFileName(f.getName());
                        fileInfo.setFileLastUpdateTime(TimeUtil.getDateInString(new Date(f.lastModified())));
                        fileInfo.setFilePath(f.getPath());
                        fileInfo.setFileType(FileInfo.FileType.Audio);
                        fileInfo.setFileCount(f.length());
                        Log.d("myfile", fileInfo.getFileName() + ":" + fileInfo.getFileCount());
                        currentFileList.add(fileInfo);
                    }
                    else if (FileUtil.isImageFileType(f.getPath())){
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setFileName(f.getName());
                        fileInfo.setFileLastUpdateTime(TimeUtil.getDateInString(new Date(f.lastModified())));
                        fileInfo.setFilePath(f.getPath());
                        fileInfo.setFileType(FileInfo.FileType.Image);
                        fileInfo.setFileCount(f.length());
                        Log.d("myfile", fileInfo.getFileName() + ":" + fileInfo.getFileCount());
                        currentFileList.add(fileInfo);
                    }
                    else if (FileUtil.isVideoFileType(f.getPath())){
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setFileName(f.getName());
                        fileInfo.setFileLastUpdateTime(TimeUtil.getDateInString(new Date(f.lastModified())));
                        fileInfo.setFilePath(f.getPath());
                        fileInfo.setFileType(FileInfo.FileType.Video);
                        fileInfo.setFileCount(f.length());
                        Log.d("myfile", fileInfo.getFileName() + ":" + fileInfo.getFileCount());
                        currentFileList.add(fileInfo);
                    }
                    else if (FileUtil.isTextFileType(f.getPath())){
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setFileName(f.getName());
                        fileInfo.setFileLastUpdateTime(TimeUtil.getDateInString(new Date(f.lastModified())));
                        fileInfo.setFilePath(f.getPath());
                        fileInfo.setFileType(FileInfo.FileType.Text);
                        fileInfo.setFileCount(f.length());
                        Log.d("myfile", fileInfo.getFileName() + ":" + fileInfo.getFileCount());
                        currentFileList.add(fileInfo);
                    }
                    else {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setFileName(f.getName());
                        fileInfo.setFileLastUpdateTime(TimeUtil.getDateInString(new Date(f.lastModified())));
                        fileInfo.setFilePath(f.getPath());
                        fileInfo.setFileType(FileInfo.FileType.Unknown);
                        fileInfo.setFileCount(f.length());
                        Log.d("myfile", fileInfo.getFileName() + ":" + fileInfo.getFileCount());
                        currentFileList.add(fileInfo);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v("permission","onRequestPermissionsResult requestCode ： " + requestCode
                + " Permission: " + permissions[0] + " was " + grantResults[0]
                + " Permission: " + permissions[1] + " was " + grantResults[1]
        );
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //resume tasks needing this permission
            Log.i("permission", "granted");
            //TODO: 再次加载文件目录
            String initPath= BasicParams.getInstance().getRootPath();
            File[] test=(new File(initPath)).listFiles();
            if (test!=null)getFileList(initPath);
            setFileList();
            setPathView(initPath);
        }
    }

}
