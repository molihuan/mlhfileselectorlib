package com.molihuan.pathselector.controller.impl;

import androidx.annotation.CallSuper;

import com.molihuan.pathselector.R;
import com.molihuan.pathselector.controller.AbstractFileBeanController;
import com.molihuan.pathselector.entity.FileBean;

/**
 * @ClassName: FileBeanControllerImpl
 * @Author: molihuan
 * @Date: 2022/12/31/18:59
 * @Description: 默认的FileBean控制器
 */
public class FileBeanControllerImpl extends AbstractFileBeanController {
    @Override
    @CallSuper
    public int getFileBeanImageResource(boolean isDir, String extension, FileBean fileBean) {
        int resourceId;
        switch (extension) {
            case "apk":
                resourceId = R.mipmap.apk;
                break;
            case "avi":
                resourceId = R.mipmap.avi;
                break;
            case "doc":
            case "docx":
                resourceId = R.mipmap.doc;
                break;
            case "exe":
                resourceId = R.mipmap.exe;
                break;
            case "flv":
                resourceId = R.mipmap.flv;
                break;
            case "gif":
                resourceId = R.mipmap.gif;
                break;
            case "jpg":
            case "jpeg":
            case "png":
                resourceId = R.mipmap.png;
                break;
            case "mp3":
                resourceId = R.mipmap.mp3;
                break;
            case "mp4":
            case "f4v":
                resourceId = R.mipmap.movie;
                break;
            case "pdf":
                resourceId = R.mipmap.pdf;
                break;
            case "ppt":
            case "pptx":
                resourceId = R.mipmap.ppt;
                break;
            case "wav":
                resourceId = R.mipmap.wav;
                break;
            case "xls":
            case "xlsx":
                resourceId = R.mipmap.xls;
                break;
            case "zip":
                resourceId = R.mipmap.zip;
                break;
            case "ext":
            default:
                if (isDir) {
                    resourceId = R.mipmap.folder;
                } else {
                    resourceId = R.mipmap.documents;
                }
                break;
        }
        return resourceId;
    }
}
