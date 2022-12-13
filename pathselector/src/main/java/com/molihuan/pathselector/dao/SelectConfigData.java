package com.molihuan.pathselector.dao;

import android.content.Context;
import android.graphics.Color;

import androidx.fragment.app.FragmentManager;

import com.blankj.molihuan.utilcode.util.LogUtils;
import com.blankj.molihuan.utilcode.util.ScreenUtils;
import com.molihuan.pathselector.controller.AbstractBuildController;
import com.molihuan.pathselector.entity.FontBean;
import com.molihuan.pathselector.fragment.AbstractFileShowFragment;
import com.molihuan.pathselector.fragment.AbstractHandleFragment;
import com.molihuan.pathselector.fragment.AbstractTabbarFragment;
import com.molihuan.pathselector.fragment.AbstractTitlebarFragment;
import com.molihuan.pathselector.fragment.impl.FileShowFragment;
import com.molihuan.pathselector.fragment.impl.HandleFragment;
import com.molihuan.pathselector.fragment.impl.TabbarFragment;
import com.molihuan.pathselector.fragment.impl.TitlebarFragment;
import com.molihuan.pathselector.listener.CommonItemListener;
import com.molihuan.pathselector.listener.FileItemListener;
import com.molihuan.pathselector.utils.MConstants;
import com.molihuan.pathselector.utils.Mtools;

/**
 * @ClassName: SelectConfigData
 * @Author: molihuan
 * @Date: 2022/11/22/21:44
 * @Description: 配置数据(数据的存储)
 */
public class SelectConfigData {

    public Context context;//上下文
    public Integer buildType;//构建类型
    public AbstractBuildController buildController;//构建控制
    public Integer requestCode;//请求码
    public Integer frameLayoutId;//添加fragment地方
    public Integer sortType;//排序类型
    public Boolean radio;//是否是单选非单选则是多选，多选必须设为false
    public Integer maxCount;//多选的个数
    public String rootPath;//默认目录
    public FragmentManager fragmentManager;//fragment管理者
    public String[] showFileTypes;//显示文件类型
    public String[] selectFileTypes;//选择文件类型

    public Boolean showSelectStorageBtn;//是否显示选择内部存储按钮

    /******************   PathSelectDialog   **************************/
    public Integer pathSelectDialogWidth;
    public Integer pathSelectDialogHeight;

    /******************   TitlebarFragment   **************************/
    public AbstractTitlebarFragment titlebarFragment;//标题Fragment
    public Boolean showTitlebarFragment;//是否显示标题Fragment
    public FontBean titlebarMainTitle;//主标题字体
    public FontBean titlebarSubtitleTitle;//副标题字体
    public Integer titlebarBG;//标题背景色
    //TODO 将数组转成 list处理
    public CommonItemListener[] morePopupItemListeners;//更多popup Item监听器

    /******************   TabbarFragment   **************************/
    public AbstractTabbarFragment tabbarFragment;//面包屑Fragment
    public Boolean showTabbarFragment;//是否显示面包屑Fragment

    /******************   FileShowFragment   **************************/
    public AbstractFileShowFragment fileShowFragment;//文件显示列表Fragment
    public FileItemListener fileItemListener;//文件item监听器

    /******************   HandleFragment   **************************/
    public AbstractHandleFragment handleFragment;//最下方按钮Fragment
    public Boolean showHandleFragment;//是否显示最下方按钮Fragment
    public Boolean alwaysShowHandleFragment;//总是显示HandleFragment
    public CommonItemListener[] handleItemListeners;//最下方按钮 Item监听器


    //TODO 构建顺序 需要优化，减少不必要类的创建和一些赋值操作，减少开销

    /**
     * 初始化默认配置
     */
    public void initDefaultConfig() {

        Mtools.log("默认配置SelectConfigData  init  start");
        context = null;//必须要设置
        buildType = null;//必须要设置
        //buildController = null;//buildType设置即可，会自动覆盖
        requestCode = null;//非必须(activity模式必须)
        frameLayoutId = null;//非必须(fragment模式必须)

        sortType = MConstants.SORT_NAME_ASC;
        radio = false;//默认多选
        maxCount = -1;//不限制
        rootPath = MConstants.DEFAULT_ROOTPATH;
        fragmentManager = null;
        showFileTypes = null;//null表示所有类型
        selectFileTypes = null;//null表示所有类型
        showSelectStorageBtn = true;
        pathSelectDialogWidth = ScreenUtils.getScreenWidth() * 80 / 100;
        pathSelectDialogHeight = ScreenUtils.getScreenHeight() * 80 / 100;
        showTitlebarFragment = true;
        titlebarMainTitle = null;//没有标题
        titlebarSubtitleTitle = null;//没有副标题
        titlebarBG = Color.rgb(255, 165, 0);//橙色
        morePopupItemListeners = null;//空即没有
        showTabbarFragment = true;
        fileItemListener = null;//空即没有
        showHandleFragment = true;
        alwaysShowHandleFragment = false;
        handleItemListeners = null;//空即没有
        Mtools.log("默认配置SelectConfigData  init  end");


    }

    /**
     * TODO 用到了反射
     * 初始化各种Fragment
     * 自定义的fragment必须通过反射获取
     * TODO 可以优化    思路:判断一个对象是一个类的子类的实例
     * <p>
     * 必须通过反射获取新的实例，可以处理用户设置的视图和已经存在的视图
     * <p>
     * class A {
     * }
     * class B extends A {
     * }
     * class C extends A {
     * }
     * class D extends B {
     * }
     * <p>
     * A a = new A();
     * B b = new B();
     * C c = new C();
     * D d = new D();
     * <p>
     * System.out.println(b.getClass().isAssignableFrom(B.class));//true
     * System.out.println(c.getClass().isAssignableFrom(B.class));//false
     * System.out.println(d.getClass().isAssignableFrom(B.class));//false
     */
    public void initAllFragment() throws IllegalAccessException, InstantiationException {
        Mtools.log("各种Fragment  init  start");
        //当对象是空或者对象一定是FileShowFragment类的实例（不是其子类实例）时可以通过new来实例化否则只能通过反射来获取(增加性能)
        if (fileShowFragment == null || fileShowFragment.getClass().isAssignableFrom(FileShowFragment.class)) {
            //一般来说FileShowFragment必须用默认的， 特殊情况可以特殊处理
            fileShowFragment = new FileShowFragment();//必须先初始化
        } else {
            //通过反射来获取自定义Fragment实例
            fileShowFragment = fileShowFragment.getClass().newInstance();
        }

        //使用自定义视图  或者  不需要显示则不创建
        if (showTitlebarFragment) {
            LogUtils.e("titlebarFragment");
            if (titlebarFragment == null || titlebarFragment.getClass().isAssignableFrom(TitlebarFragment.class)) {
                titlebarFragment = new TitlebarFragment();
            } else {
                titlebarFragment = titlebarFragment.getClass().newInstance();
            }
        }

        if (showTabbarFragment) {
            if (tabbarFragment == null || tabbarFragment.getClass().isAssignableFrom(TabbarFragment.class)) {
                tabbarFragment = new TabbarFragment();
            } else {
                tabbarFragment = tabbarFragment.getClass().newInstance();
            }
        }

        if (showHandleFragment) {
            if (handleFragment == null || handleFragment.getClass().isAssignableFrom(HandleFragment.class)) {
                handleFragment = new HandleFragment();
            } else {
                handleFragment = handleFragment.getClass().newInstance();
            }
        }
        Mtools.log("各种Fragment  init  end");
    }

}
