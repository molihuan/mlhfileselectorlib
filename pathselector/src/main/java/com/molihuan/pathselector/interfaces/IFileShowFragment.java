package com.molihuan.pathselector.interfaces;

import androidx.annotation.Nullable;

import com.molihuan.pathselector.adapter.FileListAdapter;
import com.molihuan.pathselector.entity.FileBean;

import java.util.List;

/**
 * @ClassName: AbstractFileShowFragment
 * @Author: molihuan
 * @Date: 2022/11/22/17:39
 * @Description: 中间显示所以文件的Fragment抽象类
 */
public interface IFileShowFragment {
    /**
     * 改变初始路径
     *
     * @param initPath
     */
    void setInitPath(String initPath);

    /**
     * 获取当前路径
     *
     * @return
     */
    String getCurrentPath();

    /**
     * 获取选择的列表
     *
     * @return
     */
    List<FileBean> getSelectedFileList();

    List<FileBean> getFileList();

    /**
     * 更新当前路径
     *
     * @return
     */
    List<FileBean> updateFileList();

    /**
     * 根据路径更新列表
     *
     * @param currentPath
     * @return
     */
    List<FileBean> updateFileList(String currentPath);

    /**
     * 更新ui
     *
     * @return
     */
    void refreshFileList();


    /**
     * 获取FileListAdapter
     *
     * @return
     */
    FileListAdapter getFileListAdapter();

    /**
     * 全选或取消全选
     *
     * @param status
     */
    void selectAllFile(boolean status);

    /**
     * 开启或关闭多选模式
     *
     * @param fileBean 可以为null
     * @param status
     */
    void openCloseMultipleMode(@Nullable FileBean fileBean, boolean status);

    void openCloseMultipleMode(boolean status);

    /**
     * 是否是多选模式
     *
     * @return
     */
    boolean isMultipleSelectionMode();

}
