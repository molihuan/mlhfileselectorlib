package com.molihuan.pathselector.listener;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    public CommonItemListener(CharSequence text) {
        this(text, false);
    }

    public CommonItemListener(CharSequence text, Boolean needReturnNow) {
        super(text);
        this.needReturnNow = needReturnNow;
    }

    /**
     * 可以设置字的样式
     *
     * @param fontBean
     */
    public CommonItemListener(FontBean fontBean) {
        this(fontBean, false);
    }

    public CommonItemListener(FontBean fontBean, Boolean needReturnNow) {
        super(fontBean);
        this.needReturnNow = needReturnNow;
    }

    /**
     * 重新此方法可以自定义item样式
     *
     * @param leftImg
     * @param textView
     * @return 如果返回false则自定义样式和默认样式都会生效, 且默认样式的优先级高
     * 如果返回true则只会生效自定义样式
     */
    public boolean setViewStyle(RelativeLayout container, ImageView leftImg, TextView textView) {
        return false;
    }

    /**
     * 重写此方法可以进行回调
     *
     * @param v             点击的总视图(即视图容器)
     * @param tv            从总视图中findViewById拿到的TextView(其他视图的获取也可以参照此思路)
     * @param selectedFiles 选择的fileBean
     * @param currentPath   当前路径
     * @return
     */
    public abstract boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment);

    public boolean onLongClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
        return false;
    }


}
