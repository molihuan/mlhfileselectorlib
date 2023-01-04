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
                resourceId = R.mipmap.apk_mlh;
                break;
            case "avi":
                resourceId = R.mipmap.avi_mlh;
                break;
            case "doc":
            case "docx":
                resourceId = R.mipmap.doc_mlh;
                break;
            case "exe":
                resourceId = R.mipmap.exe_mlh;
                break;
            case "flv":
                resourceId = R.mipmap.flv_mlh;
                break;
            case "gif":
                resourceId = R.mipmap.gif_mlh;
                break;
            case "jpg":
            case "jpeg":
            case "png":
                resourceId = R.mipmap.png_mlh;
                break;
            case "mp3":
                resourceId = R.mipmap.mp3_mlh;
                break;
            case "mp4":
            case "f4v":
                resourceId = R.mipmap.movie_mlh;
                break;
            case "pdf":
                resourceId = R.mipmap.pdf_mlh;
                break;
            case "ppt":
            case "pptx":
                resourceId = R.mipmap.ppt_mlh;
                break;
            case "wav":
                resourceId = R.mipmap.wav_mlh;
                break;
            case "xls":
            case "xlsx":
                resourceId = R.mipmap.xls_mlh;
                break;
            case "zip":
                resourceId = R.mipmap.zip_mlh;
                break;
            case "ext":
            default:
                if (isDir) {
                    resourceId = R.mipmap.folder_mlh;
                } else {
                    resourceId = R.mipmap.documents_mlh;
                }
                break;
        }
        return resourceId;
    }
}
