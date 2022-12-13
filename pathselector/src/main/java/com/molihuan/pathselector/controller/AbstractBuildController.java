package com.molihuan.pathselector.controller;


import com.molihuan.pathselector.dao.SelectConfigData;
import com.molihuan.pathselector.dialog.AbstractFragmentDialog;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.service.impl.ConfigDataBuilderImpl;

/**
 * @ClassName: AbstractBuildController
 * @Author: molihuan
 * @Date: 2022/11/22/23:07
 * @Description: 控制建造的模式 (Activity/Fragment/Dialog)
 */
//TODO 还需要进行抽象
public abstract class AbstractBuildController {

    protected SelectConfigData mConfigData = ConfigDataBuilderImpl.getInstance().getSelectConfigData();

    public abstract PathSelectFragment show();

    public abstract PathSelectFragment getPathSelectFragment();

    public abstract AbstractFragmentDialog getDialogFragment();
}
