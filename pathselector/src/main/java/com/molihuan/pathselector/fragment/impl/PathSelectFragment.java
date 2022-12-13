package com.molihuan.pathselector.fragment.impl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.molihuan.pathselector.R;
import com.molihuan.pathselector.adapter.FileListAdapter;
import com.molihuan.pathselector.adapter.TabbarListAdapter;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.entity.TabbarFileBean;
import com.molihuan.pathselector.fragment.AbstractFileShowFragment;
import com.molihuan.pathselector.fragment.AbstractHandleFragment;
import com.molihuan.pathselector.fragment.AbstractTabbarFragment;
import com.molihuan.pathselector.fragment.AbstractTitlebarFragment;
import com.molihuan.pathselector.fragment.BasePathSelectFragment;
import com.molihuan.pathselector.service.IFileDataManager;
import com.molihuan.pathselector.service.impl.PathFileManager;
import com.molihuan.pathselector.service.impl.UriFileManager;
import com.molihuan.pathselector.utils.FragmentTools;
import com.molihuan.pathselector.utils.MConstants;
import com.molihuan.pathselector.utils.Mtools;
import com.molihuan.pathselector.utils.PermissionsTools;
import com.molihuan.pathselector.utils.VersionTool;

import java.util.List;

/**
 * @ClassName: PathSelectFragment
 * @Author: molihuan
 * @Date: 2022/11/22/15:27
 * @Description: 组件Fragment容器, 可以进行任何你想要的操作,可以这样说:拥有了她就拥有了它就拥有了全世界
 */
public class PathSelectFragment extends BasePathSelectFragment {

    private AbstractTitlebarFragment titlebarFragment;
    private AbstractTabbarFragment tabbarFragment;
    private AbstractFileShowFragment fileShowFragment;
    private AbstractHandleFragment handleFragment;

    private FragmentManager fragmentManager;
    //路径管理者
    private IFileDataManager pathFileManager;
    //uri管理者
    private IFileDataManager uriFileManager;

    @Override
    public int setFragmentViewId() {
        return R.layout.fragment_path_select_mlh;
    }

    @Override
    public void getComponents(View view) {

    }

    @Override
    public void initData() {
        //存储权限的申请
        PermissionsTools.getStoragePermissions(mActivity);

        //获取Fragment
        titlebarFragment = mConfigData.titlebarFragment;
        tabbarFragment = mConfigData.tabbarFragment;
        fileShowFragment = mConfigData.fileShowFragment;
        handleFragment = mConfigData.handleFragment;

        //初始化路径管理者
        pathFileManager = new PathFileManager();
        //初始化uri管理者
        uriFileManager = new UriFileManager();

    }

    @Override
    public void initView() {
        showAllFragment();
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void setInitPath(String initPath) {
        fileShowFragment.setInitPath(initPath);
    }

    @Override
    public String getCurrentPath() {
        return fileShowFragment.getCurrentPath();
    }

    @Override
    public List<FileBean> getSelectedFileList() {
        return fileShowFragment.getSelectedFileList();
    }

    @Override
    public List<FileBean> getFileList() {
        return fileShowFragment.getFileList();
    }

    @Override
    public List<FileBean> updateFileList() {
        return fileShowFragment.updateFileList();
    }

    @Override
    public List<FileBean> updateFileList(String currentPath) {
        return fileShowFragment.updateFileList(currentPath);
    }

    @Override
    public FileListAdapter getFileListAdapter() {
        return fileShowFragment.getFileListAdapter();
    }

    @Override
    public void selectAllFile(boolean status) {
        fileShowFragment.selectAllFile(status);
    }

    @Override
    public void openCloseMultipleMode(@Nullable FileBean fileBean, boolean status) {
        fileShowFragment.openCloseMultipleMode(fileBean, status);
    }

    @Override
    public void openCloseMultipleMode(boolean status) {
        fileShowFragment.openCloseMultipleMode(status);
    }

    @Override
    public boolean isMultipleSelectionMode() {
        return fileShowFragment.isMultipleSelectionMode();
    }

    @Override
    public TabbarListAdapter getTabbarListAdapter() {
        return tabbarFragment.getTabbarListAdapter();
    }

    @Override
    public List<TabbarFileBean> getTabbarList() {
        return tabbarFragment.getTabbarList();
    }

    @Override
    public List<TabbarFileBean> updateTabbarList() {
        //设置了不显示TabbarFragment则返回null
        if (mConfigData.showTabbarFragment) {
            return tabbarFragment.updateTabbarList();
        } else {
            return null;
        }
    }

    @Override
    public IFileDataManager getPathFileManager() {
        return pathFileManager;
    }

    @Override
    public IFileDataManager getUriFileManager() {
        return uriFileManager;
    }

    /**
     * 返回数据给onActivityResult(int requestCode, int resultCode, Intent data)
     * 不建议使用,建议在一些按钮点击回调中获取选择的数据
     */
    @Override
    @Deprecated
    public void returnDataToActivityResult() {
        pathFileManager.returnDataToActivityResult(getSelectedFileList(), mActivity);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //保存这个uri目录的访问权限
        if (VersionTool.isAndroid11()) {
            if (requestCode == PermissionsTools.PERMISSION_REQUEST_CODE) {
                Uri uri;
                if ((uri = data.getData()) != null) {
                    mActivity.getContentResolver()
                            .takePersistableUriPermission(uri,
                                    data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                            );
                }
                //更新列表数据
                fileShowFragment.updateFileList();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void showAllFragment() {
        fragmentManager = getChildFragmentManager();

        Mtools.log("各种Fragment  show  start");
        //必须最先加载 FileShowFragment
        FragmentTools.fragmentShowHide(
                fragmentManager,
                R.id.frameLayout_file_show_area,
                fileShowFragment,
                MConstants.TAG_FRAGMENT_FILE_SHOW,
                true
        );
        //加载 TitlebarFragment
        FragmentTools.fragmentShowHide(
                fragmentManager,
                R.id.frameLayout_titlebar_area,
                titlebarFragment,
                MConstants.TAG_FRAGMENT_TITLEBAR,
                mConfigData.showTitlebarFragment
        );
        //加载 TabbarFragment
        FragmentTools.fragmentShowHide(
                fragmentManager,
                R.id.frameLayout_tabbar_area,
                tabbarFragment,
                MConstants.TAG_FRAGMENT_TABBAR,
                mConfigData.showTabbarFragment
        );

        if (mConfigData.alwaysShowHandleFragment) {
            //加载 HandleFragment
            handleShowHide(mConfigData.showHandleFragment);
        }

        Mtools.log("各种Fragment  show  end");
    }

    @Override
    public void handleShowHide(boolean isShow) {
        //总是显示
        if (mConfigData.alwaysShowHandleFragment) {
            isShow = true;
        }

        if (mConfigData.showHandleFragment && mConfigData.handleItemListeners != null) {
            FragmentTools.fragmentShowHide(
                    fragmentManager,
                    R.id.frameLayout_handle_area,
                    handleFragment,
                    MConstants.TAG_FRAGMENT_HANDLE,
                    isShow
            );
        }
    }

    @Override
    public boolean onBackPressed() {
        return fileShowFragment.onBackPressed();
    }
}
