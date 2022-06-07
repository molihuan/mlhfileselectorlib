package com.zlylib.mlhfileselectorlib.interfaces;

import android.content.Context;

import com.zlylib.mlhfileselectorlib.adapter.FileListAdapter;
import com.zlylib.mlhfileselectorlib.bean.FileBean;

import java.util.ArrayList;

/**
 * author: molihuan
 * 2022.6.1
 * Item接口
 */
public interface IItemCallBack {
    /**
     *
     * @param context
     * @param position 选择的option
     * @param fileAbsolutePath 文件路径
     * @param selectedFileList 选择的文件列表
     * @param selectedFilePathList 选择的文件路径列表
     * @param adapter 文件列表适配器
     */
    void onFileItemClick(Context context, int position, String fileAbsolutePath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter);
    void onLongFileItemClick(Context context, int position, String fileAbsolutePath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter);
}
