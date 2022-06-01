package com.zlylib.mlhfileselectorlib.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.zlylib.mlhfileselectorlib.R;
import com.zlylib.mlhfileselectorlib.SelectOptions;
import com.zlylib.mlhfileselectorlib.adapter.BreadAdapter;
import com.zlylib.mlhfileselectorlib.adapter.FileListAdapter;
import com.zlylib.mlhfileselectorlib.adapter.SelectSdcardAdapter;
import com.zlylib.mlhfileselectorlib.bean.BreadModel;
import com.zlylib.mlhfileselectorlib.bean.EssFile;
import com.zlylib.mlhfileselectorlib.bean.EssFileCountCallBack;
import com.zlylib.mlhfileselectorlib.bean.EssFileListCallBack;
import com.zlylib.mlhfileselectorlib.core.EssFileCountTask;
import com.zlylib.mlhfileselectorlib.core.EssFileListTask;
import com.zlylib.mlhfileselectorlib.interfaces.IActivityAndFragment;
import com.zlylib.mlhfileselectorlib.utils.Const;
import com.zlylib.mlhfileselectorlib.utils.FileUtils;
import com.zlylib.mlhfileselectorlib.utils.FragmentTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import com.zlylib.titlebarlib.ActionBarCommon;
//import com.zlylib.titlebarlib.OnActionBarChildClickListener;

public class FileSelectorActivity extends AppCompatActivity implements OnItemClickListener, OnItemChildClickListener, OnItemLongClickListener,
        View.OnClickListener, FileListAdapter.onLoadFileCountListener, EssFileListCallBack, EssFileCountCallBack, IActivityAndFragment {

    /*当前目录，默认是SD卡根目录*/
    private String mCurFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    /*是否刚才切换了SD卡路径*/
    private boolean mHasChangeSdCard = false;
    /*所有可访问存储设备列表*/
    private List<String> mSdCardList;
//    private ActionBarCommon abc;
    private RecyclerView mRecyclerView;//文件信息
    private RecyclerView mBreadRecyclerView;//地址栏信息
    private ImageButton mImbSelectSdCard;
    /**
     * 已选中的文件列表
     */
    private ArrayList<EssFile> mSelectedFileList = new ArrayList<>();//文件信息列表
    private ArrayList<String> mSelectedList = new ArrayList<>();//地址栏列表
    private ArrayList<String> mSelectedPathData = new ArrayList<>();//最终返回的数据列表
    /*当前选中排序方式的位置*/
    private int mSelectSortTypeIndex = 0;
    private BreadAdapter mBreadAdapter;
    private FileListAdapter mAdapter;

    private EssFileListTask essFileListTask;
    private EssFileCountTask essFileCountTask;

    private PopupWindow mSelectSdCardWindow;
    private BackToolbarFragment backToolbarFragment;
    private MoreChooseFragment moreChooseFragment;

    private boolean singleChoose=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        //得到所有的存储路径（内部存储+外部存储）
        mSdCardList = FileUtils.getAllSdPaths(this);
        if (!mSdCardList.isEmpty()) {
            mCurFolder = mSdCardList.get(0) + File.separator;
            if(FileUtils.exist(SelectOptions.getInstance().getTargetPath())){
                mCurFolder = SelectOptions.getInstance().getTargetPath();
            }
        }

        getComponent();
        initUi();
        setListeners();
        initData();//初始化数据
    }

    /**
     * 设置监听
     */
    private void setListeners() {
        mImbSelectSdCard.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mBreadAdapter.setOnItemChildClickListener(this);

    }
    /**
     * 获取视图组件
     */
    private void getComponent() {
        mRecyclerView = findViewById(R.id.rcv_file_list);
        mBreadRecyclerView = findViewById(R.id.breadcrumbs_view);
        mImbSelectSdCard = findViewById(R.id.imb_select_sdcard);
    }

    //@SuppressLint("ResourceAsColor")//作用不大可删
    private void initUi() {
//        abc = findViewById(R.id.abc);
//        if (SelectOptions.getInstance().getTitleBg() != 0) {
//            abc.setBackgroundColor(getResources().getColor(SelectOptions.getInstance().getTitleBg()));
//        }
//        if (SelectOptions.getInstance().getTitleColor() != 0) {
//            abc.getTitleTextView().setTextColor(getResources().getColor(SelectOptions.getInstance().getTitleColor()));
//        }
//        if (SelectOptions.getInstance().getTitleLiftColor() != 0) {
//            abc.getLeftIconView().setColorFilter(getResources().getColor(SelectOptions.getInstance().getTitleLiftColor()));
//        }
//        if (SelectOptions.getInstance().getTitleRightColor() != 0) {
//            abc.getRightTextView().setTextColor(getResources().getColor(SelectOptions.getInstance().getTitleRightColor()));
//        }
//        abc.setOnLeftIconClickListener(new OnActionBarChildClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        abc.setOnRightTextClickListener(new OnActionBarChildClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (SelectOptions.getInstance().isOnlySelectFolder()) {
//                    mSelectedList.add(mCurFolder);
//                }
//                //选中
//                if (mSelectedList.isEmpty()) {
//                    return;
//                }
//                //不为空
//                Intent result = new Intent();
//                result.putStringArrayListExtra(Const.EXTRA_RESULT_SELECTION, mSelectedList);
//                setResult(RESULT_OK, result);
//                finish();
//            }
//        });
//        if (SelectOptions.getInstance().isOnlyShowFolder()) {
//            abc.getRightTextView().setText("选中");
//        }


        if (getSupportActionBar() != null){//去除title
            getSupportActionBar().hide();
        }

        //选择内部存储SD卡按钮显示
        if (!mSdCardList.isEmpty() && mSdCardList.size() > 1) {
            mImbSelectSdCard.setVisibility(View.VISIBLE);
        }
        //文件RecyclerView信息初始化
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FileListAdapter(new ArrayList<EssFile>());
        mAdapter.setLoadFileCountListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.onAttachedToRecyclerView(mRecyclerView);
        //状态栏RecyclerView信息初始化
        List<BreadModel> breadModelList = FileUtils.getBreadModeListFromPath(mSdCardList, mCurFolder);
        mBreadRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mBreadAdapter = new BreadAdapter(breadModelList);
        mBreadRecyclerView.setAdapter(mBreadAdapter);
        mBreadAdapter.onAttachedToRecyclerView(mBreadRecyclerView);

    }

    private void initData() {

        //执行异步任务
        executeListTask(mSelectedFileList, mCurFolder, SelectOptions.getInstance().getFileTypes(), SelectOptions.getInstance().getSortType());
        initActionBarFragment(null);
    }

    /**
     * 可以重写此方法自定义样式
     * 初始化fragment
     */
    public void initActionBarFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();//开启事务
        if (backToolbarFragment==null&&fragment==null){
            backToolbarFragment = new BackToolbarFragment("请选择路径");//实例化fragment
        }
        if (fragment==null) {
            backToolbarFragment= (BackToolbarFragment) FragmentTools.fragmentShowHide(fragmentTransaction,R.id.toolbar_area,backToolbarFragment,true);
        }else {
            backToolbarFragment= (BackToolbarFragment) FragmentTools.fragmentShowHide(fragmentTransaction,R.id.toolbar_area,fragment,true);
        }

    }

    /**
     * 显示隐藏fragment
     * @param isShow
     */
    public void moreChooseShowHide(boolean isShow) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();//开启事务
        if (moreChooseFragment==null){
            moreChooseFragment = new MoreChooseFragment();//实例化fragment
        }
        moreChooseFragment = (MoreChooseFragment) FragmentTools.fragmentShowHide(fragmentTransaction,R.id.show_hide_morechoose,moreChooseFragment,isShow);

    }

    private void executeListTask(List<EssFile> essFileList, String queryPath, String[] types, int sortType) {
        essFileListTask = new EssFileListTask(essFileList, queryPath, types, sortType, SelectOptions.getInstance().isOnlyShowFolder(), this);
        essFileListTask.execute();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imb_select_sdcard) {
            showPopupWindow();
        }
    }

    /**
     * 显示选择SdCard的PopupWindow
     * 点击其他区域隐藏，阴影
     */
    private void showPopupWindow() {
        if (mSelectSdCardWindow != null) {
            mSelectSdCardWindow.showAsDropDown(mImbSelectSdCard);
            return;
        }
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_select_sdcard, null);
        mSelectSdCardWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSelectSdCardWindow.setFocusable(true);
        mSelectSdCardWindow.setOutsideTouchable(true);
        RecyclerView recyclerView = popView.findViewById(R.id.rcv_pop_select_sdcard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final SelectSdcardAdapter adapter = new SelectSdcardAdapter(FileUtils.getAllSdCardList(mSdCardList));
        recyclerView.setAdapter(adapter);
        adapter.onAttachedToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapterIn, View view, int position) {
                mSelectSdCardWindow.dismiss();
                mHasChangeSdCard = true;
                executeListTask(mSelectedFileList, FileUtils.getChangeSdCard(adapter.getData().get(position), mSdCardList), SelectOptions.getInstance().getFileTypes(), SelectOptions.getInstance().getSortType());
            }
        });
        mSelectSdCardWindow.showAsDropDown(mImbSelectSdCard);
    }

    /**
     * 状态栏item监听
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter.equals(mBreadAdapter) && view.getId() == R.id.btn_bread) {
            //点击某个路径时
            String queryPath = FileUtils.getBreadModelListByPosition(mSdCardList, mBreadAdapter.getData(), position);
            if (mCurFolder.equals(queryPath)) {//点击的为当前路径
                if (!mSdCardList.isEmpty()) {
                    for (int i = 0; i < mSdCardList.size(); i++) {//遍历匹配根目录当前路径为根路径时
                        if ((mSdCardList.get(i) + File.separator).equals(queryPath)){showPopupWindow();}
                    }
                }
                return;
            }
            Toast.makeText(FileSelectorActivity.this,queryPath,Toast.LENGTH_LONG).show();
            executeListTask(mSelectedFileList, queryPath, SelectOptions.getInstance().getFileTypes(), SelectOptions.getInstance().getSortType());
        }


    }
    /**
     * 文件item点击监听
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        if (adapter.equals(mAdapter)) {
            EssFile item = mAdapter.getData().get(position);

            if (item.isVisible()){//item checkboxke可见
                //判断是否单选
                if (SelectOptions.isSingle) {
                    mSelectedPathData.clear();
                    List<EssFile> data = mAdapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setChecked(false);
                    }
                    item.setChecked(true);
                    mSelectedPathData.add(item.getAbsolutePath());
                }else {
                    item.setChecked(!item.isChecked());
                }
                mAdapter.notifyDataSetChanged();
                return;
            }

            if (item.isDirectory()) {
                //点击文件夹
                //保存当前的垂直滚动位置
                mBreadAdapter.getData().get(mBreadAdapter.getData().size() - 1).setPrePosition(mRecyclerView.computeVerticalScrollOffset());
                executeListTask(mSelectedFileList, mCurFolder + item.getName() + File.separator, SelectOptions.getInstance().getFileTypes(), SelectOptions.getInstance().getSortType());
            } else {
                //选中某文件后，判断是否只能选择文件夹
                if (SelectOptions.getInstance().isOnlySelectFolder()) {
                    if (!item.getFile().isDirectory()) {
                        //Snackbar.make(mRecyclerView, "您只能选择文件夹", Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(FileSelectorActivity.this,"您只能选择文件夹",Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                //选中某文件----单选
                if (SelectOptions.isSingle) {
                    mSelectedPathData.clear();//清理数据列表
                    mSelectedPathData.add(item.getAbsolutePath());//添加当前路径
                    callBackPaths();
                    return;
                }


                if (mAdapter.getData().get(position).isChecked()) {
                    int index = findFileIndex(item);
                    if (index != -1) {
                        mSelectedFileList.remove(index);
                        mSelectedList.remove(index);
                    }
                } else {
                    if (mSelectedFileList.size() >= SelectOptions.getInstance().maxCount) {
                        //超出最大可选择数量后
                        //Snackbar.make(mRecyclerView, "您最多只能选择" + SelectOptions.getInstance().maxCount + "个。", Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(FileSelectorActivity.this,"您最多只能选择",Toast.LENGTH_LONG).show();
                        return;
                    }
                    mSelectedFileList.add(item);
                    mSelectedList.add(item.getAbsolutePath());
                }
                mAdapter.getData().get(position).setChecked(!mAdapter.getData().get(position).isChecked());
                // mAdapter.notifyItemChanged(position, "");
                mAdapter.notifyDataSetChanged();
                //abc.getRightTextView().setText(String.format(getString(R.string.selected_file_count), String.valueOf(mSelectedFileList.size()), String.valueOf(SelectOptions.getInstance().maxCount)));
            }
        }
    }
    /**
     * 文件item长按监听
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        if (adapter.equals(mAdapter)) {
            //EssFile item = mAdapter.getData().get(position);
            List<EssFile> allData = mAdapter.getData();//获取所有文件数据
            if (allData==null||allData.size()==0)return true;

            if (allData.get(0).isVisible()){
                moreChooseShowHide(false);
                backToolbarFragment.setBtnChooseVisibility(View.VISIBLE);
            }else {
                moreChooseShowHide(true);
                backToolbarFragment.setBtnChooseVisibility(View.INVISIBLE);
            }

            for (int i = 0; i < allData.size(); i++) {
                allData.get(i).setVisible(!allData.get(i).isVisible());//设置checkbox显示隐藏
            }
            mAdapter.notifyDataSetChanged();//更新数据
            return true;
        }
        return false;
    }




    /**
     * 查找文件位置
     */
    private int findFileIndex(EssFile item) {
        for (int i = 0; i < mSelectedFileList.size(); i++) {
            if (mSelectedFileList.get(i).getAbsolutePath().equals(item.getAbsolutePath())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onLoadFileCount(int posistion) {
        essFileCountTask = new EssFileCountTask(posistion, mAdapter.getData().get(posistion).getAbsolutePath(), SelectOptions.getInstance().getFileTypes(), SelectOptions.getInstance().isOnlyShowFolder(), this);
        essFileCountTask.execute();
    }

    /**
     * 查找到文件列表后
     *
     * @param queryPath 查询路径
     * @param fileList  文件列表
     */
    @Override
    public void onFindFileList(String queryPath, List<EssFile> fileList) {
        if (fileList.isEmpty()) {
            mAdapter.setEmptyView(R.layout.empty_file_list);
        }
        mCurFolder = queryPath;
        mAdapter.setNewInstance(fileList);
        List<BreadModel> breadModelList = FileUtils.getBreadModeListFromPath(mSdCardList, mCurFolder);
        if (mHasChangeSdCard) {
            mBreadAdapter.setNewInstance(breadModelList);
            mHasChangeSdCard = false;
        } else {
            if (breadModelList.size() > mBreadAdapter.getData().size()) {
                //新增
                List<BreadModel> newList = BreadModel.getNewBreadModel(mBreadAdapter.getData(), breadModelList);
                mBreadAdapter.addData(newList);
            } else {
                //减少
                int removePosition = BreadModel.getRemovedBreadModel(mBreadAdapter.getData(), breadModelList);
                if (removePosition > 0) {
                    mBreadAdapter.setNewData(mBreadAdapter.getData().subList(0, removePosition));
                }
            }
        }

        mBreadRecyclerView.smoothScrollToPosition(mBreadAdapter.getItemCount() - 1);
        //先让其滚动到顶部，然后再scrollBy，滚动到之前保存的位置
        mRecyclerView.scrollToPosition(0);
        int scrollYPosition = mBreadAdapter.getData().get(mBreadAdapter.getData().size() - 1).getPrePosition();
        //恢复之前的滚动位置
        mRecyclerView.scrollBy(0, scrollYPosition);
    }

    @Override
    public void onFindChildFileAndFolderCount(int position, String childFileCount, String childFolderCount) {
        mAdapter.getData().get(position).setChildCounts(childFileCount, childFolderCount);
        // mAdapter.notifyItemChanged(position, "childCountChanges");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        List<EssFile> allData = mAdapter.getData();//获取所有文件数据
        try {
            //if (allData==null||allData.size()==0)return;
            if (allData.get(0).isVisible()){
                for (int i = 0; i < allData.size(); i++) {
                    allData.get(i).setVisible(false);//设置checkbox显示隐藏
                }
                mAdapter.notifyDataSetChanged();//更新数据

                moreChooseShowHide(false);
                backToolbarFragment.setBtnChooseVisibility(View.VISIBLE);

                return ;
            }

        }catch (Exception e){

        }


        if (!FileUtils.canBackParent(mCurFolder, mSdCardList)) {
            super.onBackPressed();
            return;
        }
        executeListTask(mSelectedFileList, new File(mCurFolder).getParentFile().getAbsolutePath() + File.separator, SelectOptions.getInstance().getFileTypes(), SelectOptions.getInstance().getSortType());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO: 2019/3/12 暂时移除
//        EventBus.getDefault().unregister(this);
        if (essFileListTask != null) {
            essFileListTask.cancel(true);
        }
        if (essFileCountTask != null) {
            essFileCountTask.cancel(true);
        }

    }

    /**
     * 设置所有CheckBox的状态
     * @param check
     * @return
     */
    public boolean setAllCheckBoxData(boolean check){
        List<EssFile> allData = mAdapter.getData();//获取所有文件数据
        if (allData==null)return false;
        for (int i = 0; i < allData.size(); i++) {
            allData.get(i).setChecked(check);
        }
        mAdapter.notifyDataSetChanged();
        return true;
    }



    @Override
    public Object invokeFuncAiF(int functionCode) {
        if (functionCode == R.id.btn_both_noboth) {

        }else if (functionCode == R.id.btn_more_ok){
            if (!SelectOptions.isSingle){
                mSelectedPathData.clear();//清理数据列表
                List<EssFile> data = mAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isChecked()) {
                        mSelectedPathData.add(data.get(i).getAbsolutePath());
                    }
                }
            }
            callBackPaths();
        }else if (functionCode==R.id.btn_back_toolbar){
            onBackPressed();//返回按钮
        }else if (functionCode==R.id.btn_choose_toolbar){//选择按钮
            mSelectedPathData.clear();//清理数据列表
            mSelectedPathData.add(mCurFolder);//添加当前路径
            callBackPaths();
        }else if (functionCode==00011){//全选
            setAllCheckBoxData(true);
        }else if (functionCode==00012){//取消
            setAllCheckBoxData(false);
        }

        return null;
    }

    /**
     * 销毁选择器
     * 返回路径
     */
    private void callBackPaths() {
        //没有数据
        if (mSelectedPathData==null||mSelectedPathData.isEmpty()) {
            Toast.makeText(FileSelectorActivity.this,"你还没有选择呢！",Toast.LENGTH_LONG).show();
            return;
        }
        //不为空
        Intent result = new Intent();
        result.putStringArrayListExtra(Const.EXTRA_RESULT_SELECTION, mSelectedPathData);
        setResult(RESULT_OK, result);
//        Toast.makeText(FileSelectorActivity.this,mCurFolder,Toast.LENGTH_LONG).show();
        finish();
    }
}
