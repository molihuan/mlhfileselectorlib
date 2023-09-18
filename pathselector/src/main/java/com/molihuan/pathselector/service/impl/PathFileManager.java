package com.molihuan.pathselector.service.impl;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.molihuan.utilcode.util.FileUtils;
import com.molihuan.pathselector.adapter.FileListAdapter;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.service.BaseFileManager;
import com.molihuan.pathselector.utils.FileTools;

import java.io.File;
import java.util.List;

/**
 * @ClassName: PathFileManager
 * @Author: molihuan
 * @Date: 2022/12/01/9:10
 * @Description:
 */
public class PathFileManager extends BaseFileManager {

    @Override
    public List<FileBean> updateFileList(@Nullable Fragment fragment, String initPath, String currentPath, List<FileBean> fileList, FileListAdapter fileAdapter, List<String> fileTypeList) {

        fileList = initFileList(currentPath, fileList);

        mLifeCycle.onBeforeUpdateFileList(fileList, fileAdapter);

        //列表中存在但未初始化的FileBean个数，即列表中FileBean所有字段都为null的个数
        int cacheFileSize = fileList.size() - 1;

        File file = FileUtils.getFileByPath(currentPath);

        if (file == null) {
            return fileList;
        }

        File[] subFiles = file.listFiles();
        String extension;
        int addNumber = 0;//添加的数量
        FileBean fileBean;

        if (subFiles != null) {
            for (int i = 0; i < subFiles.length; i++) {
                //获取后缀
                extension = FileUtils.getFileExtension(subFiles[i]);
                //fileTypeList为null或者数量为0说明不限制类型.添加文件后缀符合要求的、添加文件夹、没有要求就都添加
                if (fileTypeList == null || fileTypeList.size() == 0 || fileTypeList.contains(extension) || subFiles[i].isDirectory()) {

                    if (addNumber < cacheFileSize) {
                        /**
                         * 如果还有缓存的FileBean就设置属性即可
                         * 0索引FileBean为返回按钮所以+1
                         */
                        fileBean = fileList.get(addNumber + 1);
                        fileBean.setPath(subFiles[i].getAbsolutePath())
                                .setName(subFiles[i].getName())
                                .setDir(subFiles[i].isDirectory())
                                .setFileExtension(extension)
                                .setChildrenFileNumber(FileTools.getChildrenNumber(subFiles[i])[0])
                                .setChildrenDirNumber(FileTools.getChildrenNumber(subFiles[i])[1])
                                .setBoxVisible(false)
                                .setBoxChecked(false)
                                .setModifyTime(subFiles[i].lastModified())
                                .setSize(subFiles[i].length())
                                .setSizeString(FileTools.computeFileSize(subFiles[i]))
                                .setUseUri(false)
                                //需要放在最后
                                .setFileIcoType(mFileBeanController.getFileBeanImageResource(
                                        subFiles[i].isDirectory(),
                                        extension,
                                        fileBean
                                ));

                    } else {
                        //如果不够就new
                        fileBean = new FileBean();
                        fileBean.setPath(subFiles[i].getAbsolutePath())
                                .setName(subFiles[i].getName())
                                .setDir(subFiles[i].isDirectory())
                                .setFileExtension(extension)
                                .setChildrenFileNumber(FileTools.getChildrenNumber(subFiles[i])[0])
                                .setChildrenDirNumber(FileTools.getChildrenNumber(subFiles[i])[1])
                                .setBoxVisible(false)
                                .setBoxChecked(false)
                                .setModifyTime(subFiles[i].lastModified())
                                .setSize(subFiles[i].length())
                                .setSizeString(FileTools.computeFileSize(subFiles[i]))
                                .setUseUri(false)
                                .setFileIcoType(mFileBeanController.getFileBeanImageResource(
                                        subFiles[i].isDirectory(),
                                        extension,
                                        fileBean
                                ));

                        fileList.add(fileBean);
                    }
                    addNumber++;//添加数量增加
                }


            }
        }

        mLifeCycle.onAfterUpdateFileList(fileList, fileAdapter);

        return fileList;
    }

}
