package com.molihuan.pathselector.listener;

import android.view.View;

import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.entity.FontBean;
import com.molihuan.pathselector.fragment.BasePathSelectFragment;

import java.util.List;

/**
 * @ClassName: CommonItemListener
 * @Author: molihuan
 * @Date: 2022/11/26/14:56
 * @Description:
 */
public abstract class CommonItemListener extends BaseItemListener {
    //是否离开返回
    protected Boolean needReturnNow;

    public CommonItemListener(String text) {
        this(text, false);
    }

    public CommonItemListener(String text, Boolean needReturnNow) {
        super(text);
        this.needReturnNow = needReturnNow;
    }

    public CommonItemListener(FontBean fontBean) {
        this(fontBean, false);
    }

    public CommonItemListener(FontBean fontBean, Boolean needReturnNow) {
        super(fontBean);
        this.needReturnNow = needReturnNow;
    }

    /**
     * @param v             点击的视图
     * @param selectedFiles 选择的fileBean
     * @param currentPath   当前路径
     * @return
     */
    public abstract boolean onClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment);

    public boolean onLongClick(View v, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
        return false;
    }
}
