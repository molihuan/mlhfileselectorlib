package com.zlylib.mlhfileselectorlib.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.zlylib.mlhfileselectorlib.adapter.ToolbarOptionsAdapter;
import com.zlylib.mlhfileselectorlib.bean.BreadModel;
import com.zlylib.mlhfileselectorlib.bean.FileBean;
import com.zlylib.mlhfileselectorlib.core.FileCountTask;
import com.zlylib.mlhfileselectorlib.core.FileListTask;
import com.zlylib.mlhfileselectorlib.fragment.ToolbarFragment;
import com.zlylib.mlhfileselectorlib.interfaces.FileCountCallBack;
import com.zlylib.mlhfileselectorlib.interfaces.FileListCallBack;
import com.zlylib.mlhfileselectorlib.interfaces.IActivityAndFragment;
import com.zlylib.mlhfileselectorlib.utils.Const;
import com.zlylib.mlhfileselectorlib.utils.FileUtils;
import com.zlylib.mlhfileselectorlib.utils.FragmentTools;
import com.zlylib.mlhfileselectorlib.utils.PermissionsTools;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class FileSelectorActivity extends AppCompatActivity implements OnItemClickListener, OnItemChildClickListener, OnItemLongClickListener,
        View.OnClickListener, FileListAdapter.onLoadFileCountListener, FileListCallBack, FileCountCallBack, IActivityAndFragment {

    /*????????????????????????SD????????????*/
    private String mCurFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    /*?????????????????????SD?????????*/
    private boolean mHasChangeSdCard = false;
    /*?????????????????????????????????*/
    private List<String> mSdCardList;
    private RecyclerView mRecyclerView;//????????????
    private RecyclerView mBreadRecyclerView;//???????????????
    private ImageButton mImbSelectSdCard;//????????????????????????
    private LinearLayout linl_path_statusbar;//
    //????????????????????????
    private ArrayList<FileBean> mSelectedFileList = new ArrayList<>();//??????????????????
    private ArrayList<String> mSelectedList = new ArrayList<>();//???????????????
    private ArrayList<String> mSelectedPathData = new ArrayList<>();//???????????????????????????
    /*?????????????????????????????????*/
    private int mSelectSortTypeIndex = 0;
    private BreadAdapter mBreadAdapter;//????????????????????????
    private FileListAdapter mAdapter;//?????????????????????

    private FileListTask fileListTask;
    private FileCountTask fileCountTask;

    private PopupWindow mSelectSdCardWindow;
    private PopupWindow mToolbarOptionsWindow;
    private Fragment mToolbarFragment;
    private Fragment moreChooseFragment;

    private boolean singleChoose=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        PermissionsTools.getAllNeedPermissions(this,getContentResolver());

        getComponent();
        initUi();
        setListeners();
        initData();//???????????????
    }

    /**
     * ????????????
     */
    private void setListeners() {
        mImbSelectSdCard.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mBreadAdapter.setOnItemChildClickListener(this);

    }
    /**
     * ??????????????????
     */
    private void getComponent() {
        mRecyclerView = findViewById(R.id.rcv_file_list);
        mBreadRecyclerView = findViewById(R.id.breadcrumbs_view);
        mImbSelectSdCard = findViewById(R.id.imb_select_sdcard);
        linl_path_statusbar = findViewById(R.id.linl_path_statusbar);
    }

    private void initUi() {
        //?????????????????????ActionBar?????????????????????ToolbarFragment????????????????????????ToolbarFragment
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        //??????????????????????????????????????????+???????????????
        mSdCardList = FileUtils.getAllSdPaths(this);
        if (!mSdCardList.isEmpty()) {
            mCurFolder = mSdCardList.get(0) + File.separator;
            if(FileUtils.exist(SelectOptions.getInstance().getTargetPath())){
                mCurFolder = SelectOptions.getInstance().getTargetPath();//???????????????????????????
            }
        }

        //????????????SD?????????????????????
        if (!mSdCardList.isEmpty() && mSdCardList.size() > 1) {
            mImbSelectSdCard.setVisibility(View.VISIBLE);
        }else {
            mImbSelectSdCard.setVisibility(View.INVISIBLE);
        }
        //??????RecyclerView???????????????
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FileListAdapter(new ArrayList<FileBean>());
        mAdapter.setLoadFileCountListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.onAttachedToRecyclerView(mRecyclerView);
        //?????????RecyclerView???????????????
        List<BreadModel> breadModelList = FileUtils.getBreadModeListFromPath(mSdCardList, mCurFolder);
        mBreadRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mBreadAdapter = new BreadAdapter(breadModelList);
        mBreadRecyclerView.setAdapter(mBreadAdapter);
        mBreadAdapter.onAttachedToRecyclerView(mBreadRecyclerView);
        //?????????Toolbar
        initToolbarFragment();

    }

    private void initData() {

        //??????????????????
        executeListTask(mSelectedFileList, mCurFolder, SelectOptions.getInstance().getFileTypes(), SelectOptions.getInstance().getSortType());

    }



    /**
     * ?????????ToolbarFragment
     */
    public void initToolbarFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();//????????????
        if (mToolbarFragment==null) {
            mToolbarFragment = SelectOptions.getInstance().getToolbarFragment();//??????ToolbarFragment
        }
        mToolbarFragment= FragmentTools.fragmentShowHide(fragmentTransaction,R.id.toolbar_area,mToolbarFragment,true);//??????ToolbarFragment????????????
    }

    /**
     * ??????????????????fragment
     * @param isShow
     */
    public void moreChooseShowHide(boolean isShow) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();//????????????
        if (moreChooseFragment==null){
            moreChooseFragment = SelectOptions.getInstance().getMoreChooseFragment();//?????????fragment
        }
        moreChooseFragment = FragmentTools.fragmentShowHide(fragmentTransaction,R.id.show_hide_morechoose,moreChooseFragment,isShow);
    }

    private void executeListTask(List<FileBean> fileBeanList, String queryPath, String[] types, int sortType) {
        fileListTask = new FileListTask(fileBeanList, queryPath, types, sortType, SelectOptions.getInstance().isOnlyShowFolder(), this);
        fileListTask.execute();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imb_select_sdcard) {
            showSdPopupWindow();
        }
    }

    /**
     * ????????????SdCard???PopupWindow
     * ?????????????????????????????????
     */
    private void showSdPopupWindow() {
        if (mSelectSdCardWindow != null) {
            mSelectSdCardWindow.showAsDropDown(linl_path_statusbar);

            return;
        }
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_select_sdcard, null);
        mSelectSdCardWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        mSelectSdCardWindow.showAsDropDown(linl_path_statusbar);
    }
    /**
     * ????????????toolbar options PopupWindow
     * ?????????????????????????????????
     */
    private void showToolbarOptionsPopupWindow() {

        if (SelectOptions.getInstance().getOptionsName()==null||SelectOptions.getInstance().getOptionsName().length==1)return;

        if (mToolbarOptionsWindow != null) {
            mToolbarOptionsWindow.showAtLocation(linl_path_statusbar, Gravity.RIGHT|Gravity.TOP,0,0);
            return;
        }
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_toolbar_options, null);
        mToolbarOptionsWindow = new PopupWindow(popView, linl_path_statusbar.getWidth()/3, ViewGroup.LayoutParams.WRAP_CONTENT);
        mToolbarOptionsWindow.setFocusable(true);
        mToolbarOptionsWindow.setOutsideTouchable(true);
        RecyclerView recyclerView = popView.findViewById(R.id.rcv_pop_toolbar_options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ToolbarOptionsAdapter adapter = new ToolbarOptionsAdapter(Arrays.asList(SelectOptions.getInstance().getOptionsName()));
        recyclerView.setAdapter(adapter);
        adapter.onAttachedToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapterIn, View view, int position) {
                mToolbarOptionsWindow.dismiss();
                //??????
                SelectOptions.getInstance().getOnOptionClicks()[position].onOptionClick(view,FileSelectorActivity.this,position,mCurFolder,mSelectedFileList,mSelectedPathData,mAdapter);
                if (SelectOptions.getInstance().getOptionsNeedCallBack()[position]) {
                    mSelectedPathData.clear();//??????????????????
                    mSelectedPathData.add(mCurFolder);//??????????????????
                    callBackPaths(mSelectedPathData);
                }

            }
        });
        mToolbarOptionsWindow.showAtLocation(linl_path_statusbar, Gravity.RIGHT|Gravity.TOP,0,0);
    }

    /**
     * ???????????????item??????
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter.equals(mBreadAdapter) && view.getId() == R.id.btn_bread) {
            //?????????????????????
            String queryPath = FileUtils.getBreadModelListByPosition(mSdCardList, mBreadAdapter.getData(), position);
            if (mCurFolder.equals(queryPath)) {//????????????????????????
                if (!mSdCardList.isEmpty()) {
                    for (int i = 0; i < mSdCardList.size(); i++) {//????????????????????????????????????????????????
                        if ((mSdCardList.get(i) + File.separator).equals(queryPath)){showSdPopupWindow();}
                    }
                }
                return;
            }
            //Toast.makeText(FileSelectorActivity.this,queryPath,Toast.LENGTH_LONG).show();
            executeListTask(mSelectedFileList, queryPath, SelectOptions.getInstance().getFileTypes(), SelectOptions.getInstance().getSortType());
        }


    }
    /**
     * ??????item????????????
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        if (adapter.equals(mAdapter)) {
            FileBean item = mAdapter.getData().get(position);

            if (item.isVisible()){//item checkboxke??????
                //??????????????????
                if (SelectOptions.isSingle) {
                    mSelectedPathData.clear();
                    List<FileBean> data = mAdapter.getData();
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
                //???????????????
                //?????????????????????????????????
                mBreadAdapter.getData().get(mBreadAdapter.getData().size() - 1).setPrePosition(mRecyclerView.computeVerticalScrollOffset());
                executeListTask(mSelectedFileList, mCurFolder + item.getName() + File.separator, SelectOptions.getInstance().getFileTypes(), SelectOptions.getInstance().getSortType());
            } else {
                //????????????
                if (SelectOptions.getInstance().isOnlySelectFolder()) {
                    if (!item.getFile().isDirectory()) {
                        Toast.makeText(FileSelectorActivity.this,"????????????????????????",Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                if (SelectOptions.getInstance().getOnFileItem()!=null) {
                    //????????????
                    SelectOptions.getInstance().getOnFileItem().onFileItemClick(view,FileSelectorActivity.this,position,item.getAbsolutePath(),mSelectedFileList,mSelectedPathData,mAdapter);
                }

                //???????????????----??????
                if (SelectOptions.isSingle&&SelectOptions.getInstance().getOnFileItem()!=null) {
                    mSelectedPathData.clear();//??????????????????
                    mSelectedPathData.add(item.getAbsolutePath());//??????????????????
                    callBackPaths(mSelectedPathData);
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
                        //??????????????????????????????
                        //Snackbar.make(mRecyclerView, "?????????????????????" + SelectOptions.getInstance().maxCount + "??????", Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(FileSelectorActivity.this,"?????????????????????",Toast.LENGTH_LONG).show();
                        return;
                    }
                    mSelectedFileList.add(item);
                    mSelectedList.add(item.getAbsolutePath());
                }

                //mAdapter.getData().get(position).setChecked(!mAdapter.getData().get(position).isChecked());
                // mAdapter.notifyItemChanged(position, "");
                mAdapter.notifyDataSetChanged();
            }
        }
    }
    /**
     * ??????item????????????
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        if (adapter.equals(mAdapter)) {
            //EssFile item = mAdapter.getData().get(position);
            List<FileBean> allData = mAdapter.getData();//????????????????????????
            if (allData==null||allData.size()==0)return true;
            FileBean item = allData.get(position);
            if (item.isFile()){
                if (SelectOptions.getInstance().getOnFileItem()!=null) {
                    //??????????????????
                    SelectOptions.getInstance().getOnFileItem().onLongFileItemClick(view,FileSelectorActivity.this,position,item.getAbsolutePath(),mSelectedFileList,mSelectedPathData,mAdapter);
                }
            }

            if (allData.get(0).isVisible()){
                moreChooseShowHide(false);
                if (mToolbarFragment instanceof ToolbarFragment){

                }

            }else {
                moreChooseShowHide(true);
                if (mToolbarFragment instanceof ToolbarFragment){

                }

            }

            for (int i = 0; i < allData.size(); i++) {
                allData.get(i).setVisible(!allData.get(i).isVisible());//??????checkbox????????????
            }
            mAdapter.notifyDataSetChanged();//????????????
            return true;
        }
        return false;
    }




    /**
     * ??????????????????
     */
    private int findFileIndex(FileBean item) {
        for (int i = 0; i < mSelectedFileList.size(); i++) {
            if (mSelectedFileList.get(i).getAbsolutePath().equals(item.getAbsolutePath())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onLoadFileCount(int posistion) {
        fileCountTask = new FileCountTask(posistion, mAdapter.getData().get(posistion).getAbsolutePath(), SelectOptions.getInstance().getFileTypes(), SelectOptions.getInstance().isOnlyShowFolder(), this);
        fileCountTask.execute();
    }

    /**
     * ????????????????????????
     * @param queryPath ????????????
     * @param fileList  ????????????
     */
    @Override
    public void onFindFileList(String queryPath, List<FileBean> fileList) {
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
                //??????
                List<BreadModel> newList = BreadModel.getNewBreadModel(mBreadAdapter.getData(), breadModelList);
                mBreadAdapter.addData(newList);
            } else {
                //??????
                int removePosition = BreadModel.getRemovedBreadModel(mBreadAdapter.getData(), breadModelList);
                if (removePosition > 0) {
                    mBreadAdapter.setNewData(mBreadAdapter.getData().subList(0, removePosition));
                }
            }
        }

        mBreadRecyclerView.smoothScrollToPosition(mBreadAdapter.getItemCount() - 1);
        //????????????????????????????????????scrollBy?????????????????????????????????
        mRecyclerView.scrollToPosition(0);
        int scrollYPosition = mBreadAdapter.getData().get(mBreadAdapter.getData().size() - 1).getPrePosition();
        //???????????????????????????
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
        List<FileBean> allData = mAdapter.getData();//????????????????????????
        try {
            //if (allData==null||allData.size()==0)return;
            if (allData.get(0).isVisible()){
                for (int i = 0; i < allData.size(); i++) {
                    allData.get(i).setVisible(false);//??????checkbox????????????
                }
                mAdapter.notifyDataSetChanged();//????????????

                moreChooseShowHide(false);
                if (mToolbarFragment instanceof ToolbarFragment){

                }
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
//        EventBus.getDefault().unregister(this);
        if (fileListTask != null) {
            fileListTask.cancel(true);
        }
        if (fileCountTask != null) {
            fileCountTask.cancel(true);
        }

    }

    /**
     * ????????????CheckBox?????????
     * @param isCheck
     * @return
     */
    public boolean setAllCheckBoxData(boolean isCheck){
        List<FileBean> allData = mAdapter.getData();//????????????????????????
        if (allData==null)return false;
        for (int i = 0; i < allData.size(); i++) {
            allData.get(i).setChecked(isCheck);
        }
        mAdapter.notifyDataSetChanged();
        return true;
    }




    @Override
    public Object invokeFuncAiF(int functionCode) {





        if (functionCode == 66666) {

        }else if (functionCode == 00013){
            if (!SelectOptions.isSingle){
                mSelectedPathData.clear();//??????????????????
                List<FileBean> data = mAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isChecked()) {
                        mSelectedPathData.add(data.get(i).getAbsolutePath());
                    }
                }
            }
            callBackPaths(mSelectedPathData);
        }else if (functionCode==R.id.btn_back_toolbar){
            onBackPressed();//????????????
        }else if (functionCode==R.id.imgv_options_toolbar){//????????????

            showToolbarOptionsPopupWindow();

            //mSelectedPathData.clear();//??????????????????
            //mSelectedPathData.add(mCurFolder);//??????????????????
            //callBackPaths();
        }else if (functionCode==00011){//??????
            setAllCheckBoxData(true);
        }else if (functionCode==00012){//??????
            setAllCheckBoxData(false);
        }else if (functionCode==40000){
            return getmCurFolder();
        } else if (functionCode==40001){
            return getmSelectedFileList();
        } else if (functionCode==40002){
            return getmSelectedPathData();
        } else if (functionCode==40003){
            return getmAdapter();
        }

        return null;
    }

    /**
     * ???????????????
     * ????????????
     */
    public void callBackPaths(ArrayList<String> callBackPathData) {
        //????????????
        if (callBackPathData==null||callBackPathData.isEmpty()) {
            Toast.makeText(FileSelectorActivity.this,"????????????????????????",Toast.LENGTH_LONG).show();
            return;
        }
        //?????????
        Intent result = new Intent();
        result.putStringArrayListExtra(Const.EXTRA_RESULT_SELECTION, callBackPathData);
        setResult(RESULT_OK, result);//??????????????????????????????
        finish();
    }

    /**
     * ???????????????????????????
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //????????????uri?????????????????????
        Uri uri;
        if (data == null) {
            return;
        }
        if (requestCode == 11 && (uri = data.getData()) != null) {
            getContentResolver().takePersistableUriPermission(uri, data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));//???????????????????????????????????????????????????????????????

        }
    }

    /**
     * ???????????????????????????????????????
     * @return
     */
    public String getmCurFolder() {
        return mCurFolder;
    }

    public ArrayList<FileBean> getmSelectedFileList() {
        return mSelectedFileList;
    }

    public ArrayList<String> getmSelectedPathData() {
        return mSelectedPathData;
    }

    public FileListAdapter getmAdapter() {
        return mAdapter;
    }
}
