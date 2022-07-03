package com.molihuan.pathselector.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.adapters.FileListAdapter;
import com.molihuan.pathselector.adapters.SDCardListAdapter;
import com.molihuan.pathselector.adapters.TabbarFileListAdapter;
import com.molihuan.pathselector.controllers.BuildControl;
import com.molihuan.pathselector.dao.SelectOptions;
import com.molihuan.pathselector.entities.FileBean;
import com.molihuan.pathselector.entities.TabbarFileBean;
import com.molihuan.pathselector.service.BeanListManager;
import com.molihuan.pathselector.utils.Constants;
import com.molihuan.pathselector.utils.FileTools;
import com.molihuan.pathselector.utils.FragmentTools;
import com.molihuan.pathselector.utils.Mtools;
import com.molihuan.pathselector.utils.PermissionsTools;
import com.molihuan.pathselector.utils.UriTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主要实现的类
 * @ClassName PathSelectFragment
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/22 3:08
 */
public class PathSelectFragment extends BaseFragment implements OnItemClickListener, OnItemLongClickListener {

    private String mCurrentPath;//当前路径
    private String INITIALDIR ;//初始路径
    private RecyclerView mTabbarFileRecyclerView;//地址栏View
    private RecyclerView mFileRecyclerView;//文件View
    private ImageButton imbChangeSdCard;//更改存储设备按钮
    private LinearLayout mLinlPathStatusbar;//标签栏

    private List<String> mSdCardList;//所有可访问存储设备列表
    private List<FileBean> mFileList;//文件信息列表
    private List<FileBean> mCallBackFileBeanList= new ArrayList<>();;//最终返回的FileBeanList
    private List<TabbarFileBean> mTabbarFileList;//标签栏列表
    private List<String> mCallBackData = new ArrayList<>();//最终返回的数据列表
    private int mSortType;//排序方式
    private TabbarFileListAdapter mTabbarFileListAdapter;//标签栏适配器
    private FileListAdapter mFileListAdapter;//文件列表适配器

    private PopupWindow mSdCardPopupWindow;
    private Fragment mToolbarFragment;//ToolbarFragment
    private Fragment mMoreChooseFragment;
    private SelectOptions mSelectOptions;

    public static boolean isShowMorechoose=false;//是否显示多选

    private int choosedFileItemNumber=0;//已经选择FileItem个数
    private List<String> mShowFileTypes;//显示文件类型
    private List<String> mSelectFileTypes;//选择文件类型

    private IToolbarFragmentCallBack mIToolbarFragmentCallBack;//ToolbarFragment显示回调
    private IMoreChooseCheckBoxCallBack mIMoreChooseCheckBoxCallBack;//IMoreChooseCheckBoxCallBack显示回调

    @Override
    public int getFragmentViewId() {
        return R.layout.fragment_files_list_mlh;
    }

    @Override
    public void getComponents(View v) {

        mTabbarFileRecyclerView=v.findViewById(R.id.rcv_tabbar_files_list);
        mFileRecyclerView=v.findViewById(R.id.rcv_files_list);
        imbChangeSdCard=v.findViewById(R.id.imb_select_sdcard);
        mLinlPathStatusbar=v.findViewById(R.id.linl_path_statusbar);

    }

    @Override
    public void initData() {
        //申请权限
        PermissionsTools.getAllNeedPermissions(mActivity,PathSelectFragment.this,mActivity.getContentResolver());
        mSelectOptions = SelectOptions.getInstance();//获取SelectOptions实例

        mSdCardList = initRootPath(mActivity, imbChangeSdCard);//初始化root目录

        //获取显示、选择类型数据
        mShowFileTypes =Arrays.asList(mSelectOptions.getShowFileTypes());
        mSelectFileTypes=Arrays.asList(mSelectOptions.getSelectFileTypes());
        //获取排序类型
        mSortType = mSelectOptions.getSortType();

        //获取数据
        if (FileTools.isAndroidDataPath(mCurrentPath)) {//判断是否在Android/data目录下
            //通过uri来获取数据
            mFileList= UriTools.upDataFileBeanListByUri(mActivity,UriTools.file2Uri(mCurrentPath),mFileList,mFileListAdapter,mShowFileTypes,mSortType);
        }else {
            mFileList= BeanListManager.upDataFileBeanList(mFileList,mFileListAdapter,mCurrentPath, mShowFileTypes,mSortType);//加载数据
        }



        mTabbarFileList=BeanListManager.upDataTabbarFileBeanList(mTabbarFileList,
                mTabbarFileListAdapter,
                mCurrentPath,
                BeanListManager.TypeInitTabbar,
                mSdCardList
        );//初始化数据

    }

    /**
     * 初始化rootPath和存储路径
     * @param activity
     * @param imb
     * @return
     */
    private List<String> initRootPath(Activity activity, ImageButton imb) {
        List<String> SdCardList = FileTools.getAllSdPaths(activity);//获取所有的存储路径（内部存储+外部存储）
        mCurrentPath=mSelectOptions.rootPath;//设置当前路径

        if (mCurrentPath==null){//未设置rootpath
            if (SdCardList.isEmpty()) {//没获取到所有的存储路径
                mCurrentPath = Constants.DEFAULT_ROOTPATH;
            }else {
                mCurrentPath = SdCardList.get(0);
            }
        }

        //如果没有SD卡则不显示按钮
        if (!SdCardList.isEmpty() && SdCardList.size() > 1) {
            imb.setVisibility(View.VISIBLE);
        }else {
            imb.setVisibility(View.INVISIBLE);
        }

        INITIALDIR=mCurrentPath;//设置最初始路径

        return SdCardList;
    }

    /**
     * 所有存储设备PopupWindow
     * CardPopupWindow
     */
    private void showSdCardsPopupWindow() {
        if (mSdCardList==null||mSdCardList.size()<=1)return;

        if (mSdCardPopupWindow != null) {
            mSdCardPopupWindow.showAsDropDown(mLinlPathStatusbar);//显示位置
            return;
        }

        View popView = LayoutInflater.from(mActivity).inflate(R.layout.general_recyview_mlh, null);//加载布局文件
        mSdCardPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//设置宽度高度
        mSdCardPopupWindow.setFocusable(true);
        mSdCardPopupWindow.setOutsideTouchable(true);
        RecyclerView recyclerView = popView.findViewById(R.id.general_recyclerview_mlh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        SDCardListAdapter adapter = new SDCardListAdapter(mSdCardList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);//设置监听
        mSdCardPopupWindow.showAsDropDown(mLinlPathStatusbar);//显示位置

    }

    @Override
    public void initView() {
        //设置适配器
        mFileRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL, false));//设置布局管理者
        mFileListAdapter =new FileListAdapter(mFileList);//适配器添加数据
        mFileRecyclerView.setAdapter(mFileListAdapter);//RecyclerView设置适配器


        mTabbarFileRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL, false));//设置布局管理者
        mTabbarFileListAdapter =new TabbarFileListAdapter(mTabbarFileList);//适配器添加数据
        mTabbarFileRecyclerView.setAdapter(mTabbarFileListAdapter);//RecyclerView设置适配器


        //初始化 多选Fragment: mMoreChooseFragment
        if (mMoreChooseFragment==null){
            mMoreChooseFragment= mSelectOptions.getMoreChooseFragment();
        }
        //初始化 ToolbarFragment
        if (mSelectOptions.showToolBarFragment) {
            if (mToolbarFragment==null){
                mToolbarFragment= mSelectOptions.getToolbarFragment();
            }
            if (BuildControl.buidlType==Constants.BUILD_DIALOG){
                mIToolbarFragmentCallBack.onShowHide(mToolbarFragment,true);
            }else {
                FragmentTools.fragmentShowHide(mSelectOptions.fragmentManager, R.id.frameLayout_toolbar_area, mToolbarFragment, Constants.TAG_FRAGMENT_TOOLBAR,true);
            }
        }

//        mFileAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                fileBeanList.clear();//注意清理集合，不然上拉加载的数据会出现重复
//            }
//        });


    }

    public interface IToolbarFragmentCallBack{
        void onShowHide(Fragment fragment,boolean isShow);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.imb_select_sdcard){
            showSdCardsPopupWindow();
        }
    }

    @Override
    public void setListeners() {
        imbChangeSdCard.setOnClickListener(this);
        //设置监听
        mFileListAdapter.setOnItemClickListener(this);
        mFileListAdapter.setOnItemLongClickListener(this);

        mTabbarFileListAdapter.setOnItemClickListener(this);
        mTabbarFileListAdapter.setOnItemLongClickListener(this);
    }

    /**
     * moreChoose和CheckBox显示绑定
     * position长按的位置 -1表示没有长按
     */
    private void moreChooseCheckBox(int position){
        if (!mSelectOptions.mSingle){//不是单选才显示多选
            isShowMorechoose=!isShowMorechoose;

            if (mMoreChooseFragment!=null) {
                if (BuildControl.buidlType==Constants.BUILD_DIALOG){
                    mIMoreChooseCheckBoxCallBack.onShowHide(mMoreChooseFragment,isShowMorechoose);//回调
                }else {
                    //显示多选
                    FragmentTools.fragmentShowHide(mSelectOptions.fragmentManager, R.id.frameLayout_morechoose_area, mMoreChooseFragment, Constants.TAG_FRAGMENT_MORECHOOSE,isShowMorechoose);

                }
            }

            //显示CheckBox
            BeanListManager.setCheckBoxVisible(mFileList, mFileListAdapter, isShowMorechoose);
            //长按位置处理
            switch (position){
                case -1:

                    break;
                default:
                    FileBean fileBean = mFileList.get(position);
                    if (!fileBean.isChecked()&&fileBean.isVisible()){
                        fileBean.setChecked(true);
                    }
                    mFileListAdapter.notifyDataSetChanged();
                    //choosedFileItemNumber++;
            }

        }
        //不显示数量就为0
        if (!isShowMorechoose){
            choosedFileItemNumber=0;
        }

        if (mToolbarFragment instanceof ToolbarFragment){
            ((ToolbarFragment)mToolbarFragment).setTitles(mSelectOptions.toolbarMainTitle,mSelectOptions.toolbarSubtitleTitle);
        }
    }

    public interface IMoreChooseCheckBoxCallBack{
        void onShowHide(Fragment fragment,boolean isShow);
    }

    @Override
    public boolean onBackPressed() {
        if (isShowMorechoose){
            moreChooseCheckBox(-1);
            return true;
        }
        //如果当前路径为root路径就不处理返回按钮事件
        if (mCurrentPath.equals(INITIALDIR)||mSdCardList.contains(mCurrentPath)){
            return false;
        }else {
            mCurrentPath=FileTools.getParentPath(mCurrentPath);//更新当前路径

            if (FileTools.isAndroidDataPath(mCurrentPath)){//判断是否在Android/data目录下
                //通过uri来获取数据
                UriTools.upDataFileBeanListByUri(mActivity,UriTools.file2Uri(mCurrentPath),mFileList,mFileListAdapter,mShowFileTypes,mSortType);
                mTabbarFileList=UriTools.upDataTabbarFileBeanListByUri(mTabbarFileList,
                        mTabbarFileListAdapter,
                        mCurrentPath,
                        BeanListManager.TypeDelTabbar,
                        mSdCardList
                );//删除数据
            }else {
                //通过路径来获取数据
                BeanListManager.upDataFileBeanList(mFileList,mFileListAdapter,mCurrentPath,mShowFileTypes,mSortType);
                mTabbarFileList=BeanListManager.upDataTabbarFileBeanList(mTabbarFileList,
                        mTabbarFileListAdapter,
                        mCurrentPath,
                        BeanListManager.TypeDelTabbar,
                        mSdCardList
                );//删除数据
            }

//            BeanListManager.upDataFileBeanList(mFileList,mFileListAdapter,mCurrentPath,mShowFileTypes,mSortType);
//            mTabbarFileList=BeanListManager.upDataTabbarFileBeanList(mTabbarFileList,
//                    mTabbarFileListAdapter,
//                    mCurrentPath,
//                    BeanListManager.TypeDelTabbar,
//                    mSdCardList
//            );//移除数据



            return true;
        }
    }


    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        //点击的适配器是FileListAdapter
        if (adapter instanceof FileListAdapter){

            FileBean item = mFileList.get(position);

            if (isShowMorechoose){//已经开启多选

                if (mSelectFileTypes.size()!=0){//筛选选择类型，没有设置就不筛选
                    if (!mSelectFileTypes.contains(item.getFileExtension())){
                        Mtools.toast(mActivity,"选择类型不符合");
                        return;
                    }
                }



                if (!item.isChecked()) {//计数已经选择的数量
                    if (choosedFileItemNumber>=mSelectOptions.mMaxCount&&mSelectOptions.mMaxCount!=-1){//判断已经选择的数量是否超过规定
                        Mtools.toast(mActivity, String.format("最多选择%d项", mSelectOptions.mMaxCount));
                        return;
                    }else {
                        choosedFileItemNumber++;
                    }
                }else {
                    choosedFileItemNumber--;
                }



                if (mToolbarFragment instanceof ToolbarFragment){
                    ToolbarFragment toolbar = (ToolbarFragment) mToolbarFragment;
                    if (choosedFileItemNumber==0){
                        toolbar.setTitles(mSelectOptions.toolbarMainTitle,mSelectOptions.toolbarSubtitleTitle);
                    }else {
                        toolbar.setTitles(String.format("%s 已选(%d)",mSelectOptions.toolbarMainTitle, choosedFileItemNumber),mSelectOptions.toolbarSubtitleTitle);
                        toolbar.setSize(19,17,mSelectOptions.toolbarOptionSize);
                    }
                }



                item.setChecked(!item.isChecked());//设置状态
                mFileListAdapter.notifyDataSetChanged();//刷新适配器
            }else {//未开启多选

                if (mSelectOptions.fileItemListener!=null){//如果设置了fileItemListener
                    //fileitem回调
                    boolean state = mSelectOptions.fileItemListener.onFileItemClick(view,
                            mCurrentPath,
                            mFileList,
                            getCallBackData(),
                            mTabbarFileListAdapter,
                            mFileListAdapter,
                            item
                    );
                    if (state)return;//表示已经处理了
                }


                if (item.isFile()) {//点击的是文件

                    if (mSelectFileTypes.size()!=0){//筛选选择类型，没有设置就不筛选
                        if (!mSelectFileTypes.contains(item.getFileExtension())){
                            Mtools.toast(mActivity,"选择类型不符合");
                            return;
                        }
                    }

                }else {//点击的是文件夹
                    mCurrentPath=item.getFilePath();//更新当前路径

                    if (FileTools.isAndroidDataPath(mCurrentPath)){//判断是否在Android/data目录下
                        //通过uri来获取数据
                        UriTools.upDataFileBeanListByUri(mActivity,UriTools.file2Uri(mCurrentPath),mFileList,mFileListAdapter,mShowFileTypes,mSortType);
                        UriTools.upDataTabbarFileBeanListByUri(mTabbarFileList,
                                mTabbarFileListAdapter,
                                mCurrentPath,
                                BeanListManager.TypeAddTabbar,
                                mSdCardList
                        );//添加数据
                    }else {
                        //通过路径来获取数据
                        BeanListManager.upDataFileBeanList(mFileList,mFileListAdapter,mCurrentPath,mShowFileTypes,mSortType);
                        BeanListManager.upDataTabbarFileBeanList(mTabbarFileList,
                                mTabbarFileListAdapter,
                                mCurrentPath,
                                BeanListManager.TypeAddTabbar,
                                mSdCardList
                        );//添加数据
                    }


                }
            }


        }

        //点击的适配器是TabbarFileListAdapter
        if (adapter instanceof TabbarFileListAdapter){
            if (isShowMorechoose) {//已经开启多选
                moreChooseCheckBox(-1);
            }else {//未开启多选
                TabbarFileBean item = mTabbarFileList.get(position);
                mCurrentPath=item.getFilePath();//更新当前路径

                if (mTabbarFileList.size()==1){//如果只有一个item
                    showSdCardsPopupWindow();
                }else {


                    if (FileTools.isAndroidDataPath(mCurrentPath)){//判断是否在Android/data目录下
                        //通过uri来获取数据
                        UriTools.upDataFileBeanListByUri(mActivity,UriTools.file2Uri(mCurrentPath),mFileList,mFileListAdapter,mShowFileTypes,mSortType);
                        UriTools.upDataTabbarFileBeanListByUri(mTabbarFileList,
                                mTabbarFileListAdapter,
                                mCurrentPath,
                                BeanListManager.TypeDelTabbar,
                                mSdCardList
                        );//删除数据
                    }else {
                        //通过路径来获取数据
                        BeanListManager.upDataFileBeanList(mFileList,mFileListAdapter,mCurrentPath,mShowFileTypes,mSortType);
                        BeanListManager.upDataTabbarFileBeanList(mTabbarFileList,
                                mTabbarFileListAdapter,
                                mCurrentPath,
                                BeanListManager.TypeDelTabbar,
                                mSdCardList
                        );//删除数据
                    }

//                    BeanListManager.upDataFileBeanList(mFileList,mFileListAdapter,mCurrentPath,mShowFileTypes,mSortType);
//                    BeanListManager.upDataTabbarFileBeanList(mTabbarFileList,
//                            mTabbarFileListAdapter,
//                            mCurrentPath,
//                            BeanListManager.TypeDelTabbar,
//                            mSdCardList);//删除数据

                }


            }
        }

        //点击的适配器是SDCardListAdapter
        if (adapter instanceof SDCardListAdapter){
            mSdCardPopupWindow.dismiss();
            mCurrentPath = mSdCardList.get(position);

            if (FileTools.isAndroidDataPath(mCurrentPath)){//判断是否在Android/data目录下
                //通过uri来获取数据
                UriTools.upDataFileBeanListByUri(mActivity,UriTools.file2Uri(mCurrentPath),mFileList,mFileListAdapter,mShowFileTypes,mSortType);
                UriTools.upDataTabbarFileBeanListByUri(mTabbarFileList,
                        mTabbarFileListAdapter,
                        mCurrentPath,
                        BeanListManager.TypeInitTabbar,
                        mSdCardList
                );//初始化数据
            }else {
                //通过路径来获取数据
                BeanListManager.upDataFileBeanList(mFileList,mFileListAdapter,mCurrentPath,mShowFileTypes,mSortType);
                BeanListManager.upDataTabbarFileBeanList(mTabbarFileList,
                        mTabbarFileListAdapter,
                        mCurrentPath,
                        BeanListManager.TypeInitTabbar,
                        mSdCardList
                );//初始化数据
            }

//            BeanListManager.upDataFileBeanList(mFileList,mFileListAdapter,mCurrentPath,mShowFileTypes,mSortType);
//            mTabbarFileList=BeanListManager.upDataTabbarFileBeanList(mTabbarFileList,
//                    mTabbarFileListAdapter,
//                    mCurrentPath,
//                    BeanListManager.TypeInitTabbar,
//                    mSdCardList
//            );//初始化数据


        }

    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        //长按的适配器是FileListAdapter
        if (adapter instanceof FileListAdapter) {

            FileBean item = mFileList.get(position);

            if (mSelectOptions.fileItemListener!=null) {//如果设置了fileItemListener
                //fileitem回调
                boolean state = mSelectOptions.fileItemListener.onLongFileItemClick(view,
                        mCurrentPath,
                        mFileList,
                        getCallBackData(),
                        mTabbarFileListAdapter,
                        mFileListAdapter,
                        item
                );
                if (state) return true;//表示已经处理了
            }



            if (mSelectFileTypes.size()!=0){//筛选选择类型，没有设置就不筛选
                if (!mSelectFileTypes.contains(item.getFileExtension())){
                    Mtools.toast(mActivity,"选择类型不符合");
                    return true;
                }
            }

            //Mtools.log(choosedFileItemNumber);

            if (!item.isChecked()) {//计数已经选择的数量
                if (choosedFileItemNumber>=mSelectOptions.mMaxCount&&mSelectOptions.mMaxCount!=-1){//判断已经选择的数量是否超过规定
                    Mtools.toast(mActivity, String.format("最多选择%d项", mSelectOptions.mMaxCount));
                    return true;
                }else {
                    choosedFileItemNumber++;
                }
            }else {
                choosedFileItemNumber--;
            }

            //Mtools.log(choosedFileItemNumber);

            if (mToolbarFragment instanceof ToolbarFragment){
                ToolbarFragment toolbar = (ToolbarFragment) mToolbarFragment;
                if (choosedFileItemNumber==0){
                    toolbar.setTitles(mSelectOptions.toolbarMainTitle,mSelectOptions.toolbarSubtitleTitle);
                }else {

                    toolbar.setTitles(String.format("%s 已选(%d)",mSelectOptions.toolbarMainTitle, choosedFileItemNumber),mSelectOptions.toolbarSubtitleTitle);
                    toolbar.setSize(19,17,mSelectOptions.toolbarOptionSize);
                }
            }

            moreChooseCheckBox(position);

            return true;
        }


        return false;
    }

    /**
     * 返回授权状态并存储
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //保存这个uri目录的访问权限
        Uri uri;
        if (data == null) {
            return;
        }
        if (requestCode == 11 && (uri = data.getData()) != null) {
            mActivity.getContentResolver().takePersistableUriPermission(uri, data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));//关键是这里，这个就是保存这个目录的访问权限
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * fragment隐藏显示监听
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        choosedFileItemNumber=0;
        isShowMorechoose=false;
        super.onHiddenChanged(hidden);
    }

    /**
     * 当移除FragmentView时调用
     */
    @Override
    public void onDestroyView() {
        choosedFileItemNumber=0;
        isShowMorechoose=false;
        super.onDestroyView();
    }

    public PathSelectFragment setmIToolbarFragmentCallBack(IToolbarFragmentCallBack mIToolbarFragmentCallBack) {
        this.mIToolbarFragmentCallBack = mIToolbarFragmentCallBack;
        return this;
    }

    public PathSelectFragment setmIMoreChooseCheckBoxCallBack(IMoreChooseCheckBoxCallBack mIMoreChooseCheckBoxCallBack) {
        this.mIMoreChooseCheckBoxCallBack = mIMoreChooseCheckBoxCallBack;
        return this;
    }

    public List<FileBean> getCallBackFileBeanList() {
        BeanListManager.clearList(mCallBackFileBeanList);
        mCallBackFileBeanList=BeanListManager.getCallBackFileBeanList(mFileList);
        return mCallBackFileBeanList;
    }

    /**
     * 一些获取数据的接口
     */
    public List<String> getCallBackData() {
        BeanListManager.clearList(mCallBackData);
        mCallBackData=BeanListManager.getCallBackData(mFileList);
        return mCallBackData;
    }

    public String getCurrentPath() {
        return mCurrentPath;
    }
    public List<FileBean> getFileList() {
        return mFileList;
    }
    public TabbarFileListAdapter getTabbarFileListAdapter() {
        return mTabbarFileListAdapter;
    }
    public FileListAdapter getFileListAdapter() {
        return mFileListAdapter;
    }
}
