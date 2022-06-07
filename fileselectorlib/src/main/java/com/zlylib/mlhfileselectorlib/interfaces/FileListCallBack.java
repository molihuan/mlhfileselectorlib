package com.zlylib.mlhfileselectorlib.interfaces;

import com.zlylib.mlhfileselectorlib.bean.FileBean;

import java.util.List;

/**
 * EssFileListCallBack
 * Created by zhangliyang on 2018/3/5.
 */

public interface FileListCallBack {
    void onFindFileList(String queryPath, List<FileBean> fileBeanList);
}
