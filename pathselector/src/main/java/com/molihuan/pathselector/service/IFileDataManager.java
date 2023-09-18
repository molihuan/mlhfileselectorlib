package com.molihuan.pathselector.service;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.molihuan.pathselector.adapter.FileListAdapter;
import com.molihuan.pathselector.adapter.TabbarListAdapter;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.entity.TabbarFileBean;
import com.molihuan.pathselector.utils.MConstants;

import java.util.List;

/**
 * @Interface: IFileDataManager
 * @Author: molihuan
 * @Date: 2022/12/1/0:09
 * @Description: 文件数据管理者
 */
public interface IFileDataManager {
    /**
     * @param currentPath
     * @param fileList
     * @return
     */
    List<FileBean> initFileList(String currentPath, List<FileBean> fileList);

    /**
     * 清理掉没有使用的FileList item
     *
     * @param fileList
     * @return
     */
    List<FileBean> clearFileListCache(List<FileBean> fileList);

    /**
     * 获取或更新文件列表
     *
     * @param initPath
     * @param currentPath
     * @param fileList
     * @param fileAdapter
     * @param fileTypes
     * @return
     */
    List<FileBean> updateFileList(Fragment fragment, String initPath, String currentPath, List<FileBean> fileList, FileListAdapter fileAdapter, List<String> fileTypes);

    /**
     * 排序文件列表
     *
     * @param fileList
     * @param sortType
     * @return
     */
    List<FileBean> sortFileList(List<FileBean> fileList, MConstants.SortRules sortType, String currentPath);

    List<TabbarFileBean> initTabbarList(String initPath, List<TabbarFileBean> tabbarList);

    /**
     * 清理掉缓存TabbarList item
     *
     * @param tabbarList
     * @return
     */
    List<TabbarFileBean> clearTabbarListCache(List<TabbarFileBean> tabbarList);

    /**
     * 获取或更新tabbar
     * 初始化or添加：
     * 以最初的路径为基础，以/为分割，将当前路径分割，
     * 如：最初路径为：/storage/emulated/0当当前路径为/storage/emulated/0/Tencent/ams时应该
     * 分割成：1.（/storage/emulated/0）2.（/storage/emulated/0/Tencent）3.（/storage/emulated/0/Tencent/ams）
     *
     * @param currentPath   当前路径
     * @param tabbarList
     * @param tabbarAdapter
     * @return
     */
    List<TabbarFileBean> updateTabbarList(String initPath, String currentPath, List<TabbarFileBean> tabbarList, TabbarListAdapter tabbarAdapter);

    List<FileBean> initSelectedFileList(List<FileBean> selectedList);

    /**
     * 获取选择的列表
     *
     * @param allFileList
     * @param selectedList
     * @return
     */
    List<FileBean> getSelectedFileList(List<FileBean> allFileList, List<FileBean> selectedList);

    /**
     * 返回选择的文件数据列表给Activity的onActivityResult()
     *
     * @return
     */
    void returnDataToActivityResult(List<FileBean> selectedFileList, Activity activity);


    /**
     * 设置CheckBox显示、隐藏
     *
     * @param fileList
     * @param fileAdapter
     * @param state
     * @return
     */
    List<FileBean> setCheckBoxVisible(List<FileBean> fileList, FileListAdapter fileAdapter, boolean state);

    /**
     * 设置是否选中
     *
     * @param fileList
     * @param fileAdapter
     * @param state
     * @return
     */
    List<FileBean> setBoxChecked(List<FileBean> fileList, FileListAdapter fileAdapter, boolean state);

    /**
     * 刷新File、Tabbar
     *
     * @param level 0 1 2 3
     * @return
     */
    void refreshFileTabbar(FileListAdapter fileAdapter, TabbarListAdapter tabbarAdapter, int level);


}
