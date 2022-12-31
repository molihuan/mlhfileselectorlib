package com.molihuan.pathselector.interfaces;

import com.molihuan.pathselector.adapter.HandleListAdapter;
import com.molihuan.pathselector.listener.CommonItemListener;

import java.util.List;

/**
 * @ClassName: AbstractHandleFragment
 * @Author: molihuan
 * @Date: 2022/11/22/17:40
 * @Description: 底部按钮显示的Fragment
 */
public interface IHandleFragment {
    List<CommonItemListener> getHandleItemListeners();

    HandleListAdapter getHandleListAdapter();

    /**
     * 刷新 HandleList ui
     *
     * @return
     */
    void refreshHandleList();
}
