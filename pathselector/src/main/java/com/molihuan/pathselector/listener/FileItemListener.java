package com.molihuan.pathselector.listener;

import android.view.View;

import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.fragment.BasePathSelectFragment;


/**
 * @ClassName: FileItemListener
 * @Author: molihuan
 * @Date: 2022/11/26/14:21
 * @Description:
 */
public abstract class FileItemListener {

    public FileItemListener() {
    }

    /**
     * 点击回调
     *
     * @param v           点击的总视图(即视图容器,可以通过findViewById拿到其他视图)(更新UI)
     * @param file        文件实体(可以获取DocumentFile操作Android/data目录)
     * @param currentPath 当前路径
     * @return 不需要进行下一步处理则返回true, 一般都需要返回false
     */
    public abstract boolean onClick(View v, FileBean file, String currentPath, BasePathSelectFragment pathSelectFragment);

    /**
     * @param v           点击的总视图(即视图容器)(更新UI)
     * @param file
     * @param currentPath
     * @return 不需要进行下一步处理则返回true, 一般都需要返回false, 并不是已经处理完了不让单击监听处理，特别注意
     */
    public boolean onLongClick(View v, FileBean file, String currentPath, BasePathSelectFragment pathSelectFragment) {
        return false;
    }

}
