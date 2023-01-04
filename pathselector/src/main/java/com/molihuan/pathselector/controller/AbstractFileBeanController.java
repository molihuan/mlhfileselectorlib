package com.molihuan.pathselector.controller;

import com.molihuan.pathselector.entity.FileBean;

/**
 * @Class: AbstractFileBeanController
 * @Author: molihuan
 * @Date: 2022/12/31/17:46
 * @Description: FileBean Item 控制者
 */
public abstract class AbstractFileBeanController {
    /**
     * 自定义FileBeanItem图片资源
     *
     * @param isDir     是否是文件夹
     * @param extension 文件后缀,没有后缀的为""
     * @param fileBean  封装文件实体
     * @return 开发者自己图片资源id
     */
    public abstract int getFileBeanImageResource(boolean isDir, String extension, FileBean fileBean);
}
