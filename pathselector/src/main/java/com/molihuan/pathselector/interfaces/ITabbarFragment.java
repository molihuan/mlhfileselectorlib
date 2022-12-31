package com.molihuan.pathselector.interfaces;

import com.molihuan.pathselector.adapter.TabbarListAdapter;
import com.molihuan.pathselector.entity.TabbarFileBean;

import java.util.List;

/**
 * @ClassName: AbstractTabbarFragment
 * @Author: molihuan
 * @Date: 2022/11/22/17:38
 * @Description: 面包屑Fragment
 */
public interface ITabbarFragment {

    TabbarListAdapter getTabbarListAdapter();

    List<TabbarFileBean> getTabbarList();

    List<TabbarFileBean> updateTabbarList();

    /**
     * 刷新 TabbarList ui
     *
     * @return
     */
    void refreshTabbarList();

}
