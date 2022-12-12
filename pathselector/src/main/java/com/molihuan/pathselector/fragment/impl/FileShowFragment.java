package com.molihuan.pathselector.fragment.impl;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.adapter.FileListAdapter;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.fragment.AbstractFileShowFragment;
import com.molihuan.pathselector.listener.FileItemListener;
import com.molihuan.pathselector.service.IFileDataManager;
import com.molihuan.pathselector.service.impl.PathFileManager;
import com.molihuan.pathselector.utils.CommonTools;
import com.molihuan.pathselector.utils.FileTools;
import com.molihuan.pathselector.utils.Mtools;
import com.xuexiang.xtask.XTask;
import com.xuexiang.xtask.core.ITaskChainEngine;
import com.xuexiang.xtask.core.param.ITaskResult;
import com.xuexiang.xtask.core.step.impl.TaskChainCallbackAdapter;
import com.xuexiang.xtask.core.step.impl.TaskCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: FileShowFragment
 * @Author: molihuan
 * @Date: 2022/11/22/18:19
 * @Description:
 */
public class FileShowFragment extends AbstractFileShowFragment implements OnItemClickListener, OnItemLongClickListener {
    protected RecyclerView mRecView;

    //最初路径
    protected String initPath;
    //当前路径
    protected String currentPath;

    //List和Adapter
    private List<FileBean> selectedFileList;
    private List<FileBean> allFileList;
    private FileListAdapter fileListAdapter;

    //单选
    private Boolean radio;
    //排序类型
    private Integer sortType;
    //文件显示类型
    private List<String> showFileTypes;
    //选择类型
    private List<String> selectFileTypes;
    //路径管理者
    private IFileDataManager pathFileManager;
    //uri管理者
    private IFileDataManager uriFileManager;
    //当前是否为多选模式
    private boolean multipleSelectionMode = false;
    //多选数量
    private int selectedNumber = 0;
    //fileItem监听
    private FileItemListener fileItemListener;

    @Override
    public void setInitPath(String initPath) {
        this.initPath = initPath;
    }

    @Override
    public int setFragmentViewId() {
        return R.layout.fragment_file_show_mlh;
    }

    @Override
    public void getComponents(View view) {
        mRecView = view.findViewById(R.id.recv_file_show);
    }

    @Override
    public void initData() {
        super.initData();
        //初始化选择列表
        selectedFileList = new ArrayList<>();
        //获取路径管理者
        pathFileManager = psf.getPathFileManager();
        //获取uri管理者
        uriFileManager = psf.getUriFileManager();
        //获取初始路径并设置当前路径
        initPath = mConfigData.rootPath;
        currentPath = initPath;
        //获取配置数据
        radio = mConfigData.radio;
        sortType = mConfigData.sortType;

        showFileTypes = CommonTools.asStringList(mConfigData.showFileTypes);
        selectFileTypes = CommonTools.asStringList(mConfigData.selectFileTypes);
        //获取监听器
        fileItemListener = mConfigData.fileItemListener;

        //获取文件列表数据
        allFileList = initFileList();

    }

    @Override
    public void initView() {
        //第一次则设置Adapter和监听
        if (fileListAdapter == null) {
            mRecView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));//设置布局管理者
            fileListAdapter = new FileListAdapter(R.layout.item_file_mlh, allFileList);//适配器添加数据
            mRecView.setAdapter(fileListAdapter);//RecyclerView设置适配器
            fileListAdapter.setOnItemClickListener(this);
            fileListAdapter.setOnItemLongClickListener(this);
        }
        //更新
        updateFileList();

    }

    @Override
    public void setListeners() {

    }

    @Override
    public String getCurrentPath() {
        return currentPath;
    }

    @Override
    public List<FileBean> getSelectedFileList() {
        selectedFileList = pathFileManager.getSelectedFileList(allFileList, selectedFileList);
        return selectedFileList;
    }

    @Override
    public List<FileBean> getFileList() {
        return allFileList;
    }

    private List<FileBean> initFileList() {

        if (FileTools.needUseUri(currentPath)) {
            allFileList = uriFileManager.initFileList(currentPath, allFileList);
        } else {
            allFileList = pathFileManager.initFileList(currentPath, allFileList);
        }

        return allFileList;
    }

    @Override
    public List<FileBean> updateFileList() {
        return updateFileList(currentPath);
    }

    @Override
    public List<FileBean> updateFileList(String path) {

        //更新当前路径
        this.currentPath = path;

        //开始异步获取文件列表数据
        XTask.getTaskChain()
                .addTask(XTask.getTask(new TaskCommand() {
                    @Override
                    public void run() throws Exception {
                        //是否需要使用uri
                        if (FileTools.needUseUri(path)) {
                            allFileList = uriFileManager.updateFileList(psf, initPath, path, allFileList, fileListAdapter, showFileTypes);
                            //排序
                            allFileList = uriFileManager.sortFileList(allFileList, sortType, currentPath);

                        } else {
                            allFileList = pathFileManager.updateFileList(psf, initPath, path, allFileList, fileListAdapter, showFileTypes);
                            //排序
                            allFileList = pathFileManager.sortFileList(allFileList, sortType, currentPath);
                        }

                    }
                }))
                .setTaskChainCallback(new TaskChainCallbackAdapter() {
                    @Override
                    public void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result) {

                        //更新ui
                        if (FileTools.needUseUri(path)) {
                            //刷新
                            uriFileManager.refreshFileTabbar(fileListAdapter, null, PathFileManager.TYPE_REFRESH_FILE);
                        } else {
                            //刷新
                            pathFileManager.refreshFileTabbar(fileListAdapter, null, PathFileManager.TYPE_REFRESH_FILE);
                        }

                    }
                })
                .start();


        return allFileList;
    }

    @Override
    public FileListAdapter getFileListAdapter() {
        return this.fileListAdapter;
    }


    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View v, int position) {
        if (adapter instanceof FileListAdapter) {

            FileBean item = allFileList.get(position);
            //如果当前已经是多选模式
            if (multipleSelectionMode && (!mConfigData.radio)) {
                //多选模式下不能点击返回item
                if (position == 0) {
                    return;
                }

                //选择类型正确
                if (FileTools.selectTypeCompliance(item.getFileExtension(), selectFileTypes)) {

                    //如果只选择一个
                    if (mConfigData.maxCount == 1) {
                        pathFileManager.setBoxChecked(allFileList, null, false);
                        item.setBoxChecked(true);
                    } else {

                        //如果已经勾选了
                        if (item.getBoxChecked()) {
                            item.setBoxChecked(false);
                            selectedNumber--;
                        } else if (selectedNumber + 1 <= mConfigData.maxCount || mConfigData.maxCount == -1) {
                            //没有勾选且没有超过最大数量、或最大数量是-1则不限制
                            item.setBoxChecked(true);
                            selectedNumber++;
                        } else {
                            //超过选择的最大数量
                            Mtools.toast(getString(R.string.tip_filebeanitem_select_limit_exceeded));

                        }

                    }

                } else {
                    Mtools.toast(getString(R.string.tip_filebeanitem_select_error_type_mlh));
                }

                pathFileManager.refreshFileTabbar(fileListAdapter, null, PathFileManager.TYPE_REFRESH_FILE);

            } else {

                //如果是返回FileBean
                if (position == 0) {
                    updateFileList(item.getPath());//更新当前路径
                    //刷新面包屑
                    psf.updateTabbarList();
                    return;
                }

                //如果是文件夹
                if (item.isDir()) {
                    updateFileList(item.getPath());//更新当前路径
                    //刷新面包屑
                    psf.updateTabbarList();

                } else {
                    //选择类型正确
                    if (FileTools.selectTypeCompliance(item.getFileExtension(), selectFileTypes)) {

                        //如果设置了fileItem监听
                        if (fileItemListener != null) {
                            boolean handled = fileItemListener.onClick(v, item, currentPath, psf);
                            //已经处理完了就不需要再处理了
                            if (handled) {
                                return;
                            }
                        }
                        pathFileManager.setBoxChecked(allFileList, null, false);
                        item.setBoxChecked(true);
                    }

                }

            }

        }
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View v, int position) {
        if (adapter instanceof FileListAdapter) {
            FileBean fileBean = allFileList.get(position);
            //根据配置判断是否可以是使用多选,返回item不能长按
            if (!mConfigData.radio && position != 0) {

                //如果设置了fileItem监听
                if (fileItemListener != null) {
                    boolean handled = fileItemListener.onLongClick(v, fileBean, currentPath, psf);
                    //已经处理完了就不需要再处理了
                    if (handled) {
                        return true;
                    }
                }

                openCloseMultipleMode(fileBean, !multipleSelectionMode);
                return true;
            }
        }
        return false;
    }

    @Override
    public void selectAllFile(boolean status) {
        //只有允许多选并且当前是多选的情况下才可以
        if (!mConfigData.radio && multipleSelectionMode) {
            pathFileManager.setBoxChecked(allFileList, null, status);
            pathFileManager.refreshFileTabbar(fileListAdapter, null, PathFileManager.TYPE_REFRESH_FILE);
        }
    }

    @Override
    public void openCloseMultipleMode(@Nullable FileBean fileBean, boolean status) {
        //长按进行多选模式切换
        multipleSelectionMode = status;
        //显示隐藏checkbox
        pathFileManager.setCheckBoxVisible(allFileList, null, multipleSelectionMode);

        psf.handleShowHide(multipleSelectionMode);

        //如果是多选模式则勾选当前长按的选项
        if (multipleSelectionMode) {
            //选择类型正确
            if (fileBean != null && FileTools.selectTypeCompliance(fileBean.getFileExtension(), selectFileTypes)) {
                fileBean.setBoxChecked(true);
                selectedNumber++;
            }
        }
        //刷新
        pathFileManager.refreshFileTabbar(fileListAdapter, null, PathFileManager.TYPE_REFRESH_FILE);
    }

    @Override
    public void openCloseMultipleMode(boolean status) {
        openCloseMultipleMode(null, status);
    }

    @Override
    public boolean isMultipleSelectionMode() {
        return multipleSelectionMode;
    }

    @Override
    public boolean onBackPressed() {
        //如果当前是多选模式则先退出多选模式
        if (multipleSelectionMode) {
            openCloseMultipleMode(false);
            return true;
        }

        String path = allFileList.get(0).getPath();
        //路径超过了最初的路径则直接返回
        if (!path.startsWith(initPath)) {
            return false;
        } else {
            //更新当前路径
            currentPath = path;
            updateFileList(currentPath);
            //刷新面包屑
            psf.updateTabbarList();
            return true;
        }

    }
}
