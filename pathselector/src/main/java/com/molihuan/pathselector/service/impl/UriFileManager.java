package com.molihuan.pathselector.service.impl;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import com.blankj.molihuan.utilcode.util.FileUtils;
import com.molihuan.pathselector.adapter.FileListAdapter;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.service.BaseFileManager;
import com.molihuan.pathselector.utils.FileTools;
import com.molihuan.pathselector.utils.MConstants;
import com.molihuan.pathselector.utils.PermissionsTools;
import com.molihuan.pathselector.utils.UriTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: UriFileManager
 * @Author: molihuan
 * @Date: 2022/12/01/9:10
 * @Description:
 */
public class UriFileManager extends BaseFileManager {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public List<FileBean> updateFileList(Fragment fragment, String initPath, String currentPath, List<FileBean> fileList, FileListAdapter fileAdapter, List<String> fileTypeList) {
        //获取上下文
        Context context = fragment.getContext();

        fileList = initFileList(currentPath, fileList);
        //列表中存在但未初始化的FileBean个数，即列表中FileBean所有字段都为null的个数
        int cacheFileSize = fileList.size() - 1;

        //是否是Android/data、obb
        if (MConstants.PATH_ANRROID_DATA.equals(currentPath) || MConstants.PATH_ANRROID_OBB.equals(currentPath)) {
            //获取Android/data下面的所有包名,不是包名无法获取
            List<String> packageNames = UriTools.getAndroidDataPackageNames(context);
            List<File> subFiles = new ArrayList<>();

            File tempSubFile;
            for (int i = 0; i < packageNames.size(); i++) {
                tempSubFile = new File(currentPath, packageNames.get(i));
                //不存在就不添加
                if (tempSubFile.exists()) {
                    subFiles.add(tempSubFile);
                }
            }

            String extension;
            int addNumber = 0;//添加的数量
            for (int i = 0; i < subFiles.size(); i++) {
                tempSubFile = subFiles.get(i);
                //获取后缀
                extension = FileUtils.getFileExtension(tempSubFile);
                //fileTypeList为null或者数量为0说明不限制类型.添加文件后缀符合要求的、添加文件夹、没有要求就都添加
                if (fileTypeList == null || fileTypeList.size() == 0 || fileTypeList.contains(extension) || tempSubFile.isDirectory()) {

                    if (addNumber < cacheFileSize) {
                        /**
                         * 如果还有缓存的FileBean就设置属性即可
                         * 0索引FileBean为返回按钮所以+1
                         */
                        fileList.get(addNumber + 1)
                                .setPath(tempSubFile.getAbsolutePath())
                                .setName(tempSubFile.getName())
                                .setDir(tempSubFile.isDirectory())
                                .setFileExtension(extension)
                                .setFileIcoType(FileTools.getImageResourceByExtension(
                                        tempSubFile.isDirectory(),
                                        extension
                                ))
                                .setChildrenFileNumber(FileTools.getChildrenNumber(tempSubFile)[0])
                                .setChildrenDirNumber(FileTools.getChildrenNumber(tempSubFile)[1])
                                .setBoxVisible(false)
                                .setBoxChecked(false)
                                .setModifyTime(tempSubFile.lastModified())
                                .setSize(tempSubFile.length())
                                .setSizeString(FileTools.computeFileSize(tempSubFile))
                                .setUseUri(false);
                    } else {
                        //如果不够就new
                        FileBean fileBean = new FileBean()
                                .setPath(tempSubFile.getAbsolutePath())
                                .setName(tempSubFile.getName())
                                .setDir(tempSubFile.isDirectory())
                                .setFileExtension(extension)
                                .setFileIcoType(FileTools.getImageResourceByExtension(
                                        tempSubFile.isDirectory(),
                                        extension
                                ))
                                .setChildrenFileNumber(FileTools.getChildrenNumber(tempSubFile)[0])
                                .setChildrenDirNumber(FileTools.getChildrenNumber(tempSubFile)[1])
                                .setBoxVisible(false)
                                .setBoxChecked(false)
                                .setModifyTime(tempSubFile.lastModified())
                                .setSize(tempSubFile.length())
                                .setSizeString(FileTools.computeFileSize(tempSubFile))
                                .setUseUri(false);

                        fileList.add(fileBean);
                    }
                    addNumber++;//添加数量增加
                }
            }

            return fileList;

        } else if (FileTools.underAndroidDataUseUri(currentPath) || FileTools.underAndroidObbUseUri(currentPath)) {
            //Android/data、obb的子目录

            Uri uri = UriTools.path2Uri(currentPath, false);
            //获取权限,没有权限返回null有权限返回授权uri字符串
            String existsPermission = PermissionsTools.existsGrantedUriPermission(uri, fragment);

            if (existsPermission == null) {
                //没有权限申请权限
                PermissionsTools.goApplyUriPermissionPage(uri, fragment);
                return fileList;
            }

            /**
             * content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2Fbin.mt.plus(这是你申请权限的目录,其子目录可以不用申请权限)
             * 操作/storage/emulated/0/Android/data/bin.mt.plus
             * content://com.android.externalstorage.documents   (/document/primary%3AAndroid%2Fdata%2Fbin.mt.plus)(需要操作哪个子目录就加上类似的就可以了)
             * 操作/storage/emulated/0/Android/data/bin.mt.plus/cache
             * content://com.android.externalstorage.documents   (/document/primary%3AAndroid%2Fdata%2Fbin.mt.plus%2Fcache)(需要操作哪个子目录就加上类似的就可以了)
             */
            Uri targetUri = Uri.parse(existsPermission + uri.toString().replaceFirst(UriTools.URI_PERMISSION_REQUEST_COMPLETE_PREFIX, ""));

            //Mtools.log(targetUri);

            DocumentFile rootDocumentFile = DocumentFile.fromSingleUri(context, targetUri);
            Objects.requireNonNull(rootDocumentFile, "rootDocumentFile is null");

            //创建一个 DocumentFile表示以给定的 Uri根的文档树。其实就是获取子目录的权限
            DocumentFile pickedDir = rootDocumentFile.fromTreeUri(context, targetUri);
            Objects.requireNonNull(pickedDir, "pickedDir is null");

            DocumentFile[] documentFiles = pickedDir.listFiles();

            String extension;
            int addNumber = 0;//添加的数量
            for (int i = 0; i < documentFiles.length; i++) {
                //获取后缀
                extension = FileTools.getFileExtension(documentFiles[i].getName());
                //fileTypeList为null或者数量为0说明不限制类型.添加文件后缀符合要求的、添加文件夹、没有要求就都添加
                if (fileTypeList == null || fileTypeList.size() == 0 || fileTypeList.contains(extension) || documentFiles[i].isDirectory()) {

                    if (addNumber < cacheFileSize) {
                        /**
                         * 如果还有缓存的FileBean就设置属性即可
                         * 0索引FileBean为返回按钮所以+1
                         */
                        fileList.get(addNumber + 1)
                                .setPath(currentPath + File.separator + documentFiles[i].getName())
                                .setName(documentFiles[i].getName())
                                .setDir(documentFiles[i].isDirectory())
                                .setFileExtension(extension)
                                .setFileIcoType(FileTools.getImageResourceByExtension(
                                        documentFiles[i].isDirectory(),
                                        extension
                                ))
                                .setChildrenFileNumber(FileTools.getChildrenNumber(documentFiles[i])[0])
                                .setChildrenDirNumber(FileTools.getChildrenNumber(documentFiles[i])[1])
                                .setBoxVisible(false)
                                .setBoxChecked(false)
                                .setModifyTime(documentFiles[i].lastModified())
                                .setSize(-1L)
                                .setSizeString(FileTools.ERROR_GETTING_FILE_SIZE)
                                .setUseUri(true)
                                .setDocumentFile(documentFiles[i]);

                    } else {
                        //如果不够就new
                        FileBean fileBean = new FileBean()
                                .setPath(currentPath + File.separator + documentFiles[i].getName())
                                .setName(documentFiles[i].getName())
                                .setDir(documentFiles[i].isDirectory())
                                .setFileExtension(extension)
                                .setFileIcoType(FileTools.getImageResourceByExtension(
                                        documentFiles[i].isDirectory(),
                                        extension
                                ))
                                .setChildrenFileNumber(FileTools.getChildrenNumber(documentFiles[i])[0])
                                .setChildrenDirNumber(FileTools.getChildrenNumber(documentFiles[i])[1])
                                .setBoxVisible(false)
                                .setBoxChecked(false)
                                .setModifyTime(documentFiles[i].lastModified())
                                .setSize(-1L)
                                .setSizeString(FileTools.ERROR_GETTING_FILE_SIZE)
                                .setUseUri(true)
                                .setDocumentFile(documentFiles[i]);

                        fileList.add(fileBean);
                    }
                    addNumber++;//添加数量增加
                }
            }


        }


        return fileList;
    }

}
