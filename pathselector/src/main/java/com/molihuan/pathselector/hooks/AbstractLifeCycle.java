package com.molihuan.pathselector.hooks;

import com.molihuan.pathselector.adapter.FileListAdapter;
import com.molihuan.pathselector.entity.FileBean;

import java.util.List;

/**
 * @ClassName: AbstractLifeCycle
 * @Author: molihuan
 * @Date: 2023/01/20/17:31
 * @Description: 生命周期钩子
 */
public abstract class AbstractLifeCycle {
    /**
     * 更新fileList之前
     *
     * @param fileList
     * @param fileAdapter
     */
    public void onBeforeUpdateFileList(List<FileBean> fileList, FileListAdapter fileAdapter) {

    }

    /**
     * 更新fileList之后
     *
     * @param fileList
     * @param fileAdapter
     */
    public abstract void onAfterUpdateFileList(List<FileBean> fileList, FileListAdapter fileAdapter);
}
