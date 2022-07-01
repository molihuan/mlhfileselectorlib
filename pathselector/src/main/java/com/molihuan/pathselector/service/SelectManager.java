package com.molihuan.pathselector.service;

import androidx.fragment.app.Fragment;

import com.molihuan.pathselector.PathSelector;
import com.molihuan.pathselector.controllers.BuildControl;
import com.molihuan.pathselector.dao.SelectOptions;
import com.molihuan.pathselector.fragments.PathSelectFragment;
import com.molihuan.pathselector.utils.Constants;

/**
 * @ClassName SelectManager
 * @Description TODO 管理者:数据设置
 * @Author molihuan
 * @Date 2022/6/22 3:05
 */
public class SelectManager {
    private PathSelector mPathSelector;
    private SelectOptions mSelectOptions;

    /**
     * 获取两个对象
     * @param pathSelector
     */
    public SelectManager(PathSelector pathSelector) {
        this.mPathSelector=pathSelector;
        this.mSelectOptions=SelectOptions.getResetInstance(pathSelector.getActivity());
    }

    /**
     * 设置加载位置FrameLayoutID
     * 注意 只有当PathSelector.buidlType为Constant.BUILD_FRAGMENT才有效
     * @param id
     * @return
     */
    public SelectManager frameLayoutID(int id){
        mSelectOptions.frameLayoutID=id;
        return this;
    }
    /**
     * 设置请求码
     * @param code
     * @return
     */
    public SelectManager requestCode(int code){
        mSelectOptions.requestCode=code;
        return this;
    }

    /**
     * 设置开始默认路径
     * @param path
     * @return
     */
    public SelectManager setRootPath(String path){
        mSelectOptions.rootPath = path;
        return this;
    }

    /**
     * 设置最大选择数量
     * 不设置默认为-1 即无限
     * @param maxCount
     * @return
     */
    public SelectManager setMaxCount(int maxCount) {
        if (maxCount <= 1) {
            mSelectOptions.mMaxCount=1;
            mSelectOptions.mSingle = true;
        } else {
            mSelectOptions.mMaxCount=maxCount;
            mSelectOptions.mSingle = false;
        }
        return this;
    }

    /**
     * 设置自定义标题栏
     * @param fragment
     * @return
     */
    public SelectManager setToolbarFragment(Fragment fragment) {
        mSelectOptions.mToolbarFragment = fragment;
        mSelectOptions.typeLoadCustomView = Constants.TYPE_CUSTOM_VIEW_TOOLBAR;
        return this;
    }
    /**
     * 设置自定义多选
     * @param fragment
     * @return
     */
    public SelectManager setMoreChooseFragment(Fragment fragment) {
        mSelectOptions.mMoreChooseFragment = fragment;
        return this;
    }



    /**
     * 设置显示文件类型
     * @param fileTypes 如果文件没有后缀请使用 "" 其他参考：{@link com.molihuan.pathselector.entities.FileBean#setImageResourceByExtension(String) }
     * @return
     */
    public SelectManager setShowFileTypes(String... fileTypes) {
        mSelectOptions.mShowFileTypes = fileTypes;
        return this;
    }
    /**
     * 设置选择文件类型
     * @param fileTypes 如果文件没有后缀请使用 "" 其他参考：{@link com.molihuan.pathselector.entities.FileBean#setImageResourceByExtension(String) }
     * @return
     */
    public SelectManager setSelectFileTypes(String... fileTypes) {
        mSelectOptions.mSelectFileTypes = fileTypes;
        return this;
    }

    /**
     * 设置排序规则
     * @param sortType 规则参考: {@link com.molihuan.pathselector.utils.Constants}
     * @return
     */
    public SelectManager setSortType(int sortType) {
        mSelectOptions.mSortType=sortType;
        return this;
    }

    /**
     * 设置单选
     * @return
     */
    public SelectManager isSingle() {
        mSelectOptions.mSingle = true;
        mSelectOptions.mMaxCount = 1;
        return this;
    }


    /**
     * 设置ToolBarFragment是否显示
     * @return
     */
    public SelectManager showToolBarFragment(boolean var) {
        mSelectOptions.showToolBarFragment=var;
        return this;
    }

    /**
     * 设置一些Toolbar选项
     * @param optionsName
     * @param optionListeners
     * @return
     */
    public SelectManager setMoreOPtions(String[] optionsName ,SelectOptions.onToolbarOptionsListener ...optionListeners){
        return setMoreOPtions(optionsName,new boolean [optionsName.length],optionListeners);
    }
    /**
     * 设置一些Toolbar选项
     * @param optionsName
     * @param optionListeners
     * @return
     */
    public SelectManager setMoreOPtions(String[] optionsName , boolean [] optionsNeedCallBack ,SelectOptions.onToolbarOptionsListener ...optionListeners){
        if (optionsName.length!=optionListeners.length||optionsName.length!=optionsNeedCallBack.length||optionListeners.length!=optionsNeedCallBack.length){
            //不合法参数异常
            throw new IllegalArgumentException("选项名和点击响应必须一一对应");
        }
        else {
            mSelectOptions.needMoreOptions=true;
            mSelectOptions.optionsName=optionsName;
            mSelectOptions.optionsNeedCallBack=optionsNeedCallBack;
            mSelectOptions.optionListeners=optionListeners;
        }
        return this;
    }

    /**
     * 设置一些Toolbar点击
     * 一般用于自定义视图
     * @param
     * @param
     * @return
     */
    public SelectManager setToolbarViewClickers(SelectOptions.onToolbarListener ...listeners){
        return setToolbarViewClickers(new boolean [listeners.length],listeners);
    }
    public SelectManager setToolbarViewClickers( boolean [] needCallBack ,SelectOptions.onToolbarListener ...listeners){
        if (listeners.length!=needCallBack.length){
            //不合法参数异常
            throw new IllegalArgumentException("必须一一对应");
        }
        else {
            mSelectOptions.toolbarViewNeedCallBack=needCallBack;
            mSelectOptions.toolbarListeners=listeners;
        }
        return this;
    }
    /**
     * 设置一些MoreChooseItems选项
     * @param ItemsName
     * @param itemListeners
     * @return
     */
    public SelectManager setMoreChooseItems(String[] ItemsName ,SelectOptions.onMoreChooseItemsListener ...itemListeners){
        return setMoreChooseItems(ItemsName,new boolean [ItemsName.length],itemListeners);
    }
    /**
     * 设置一些MoreChooseItems选项
     * @param ItemsName
     * @param itemListeners
     * @return
     */
    public SelectManager setMoreChooseItems(String[] ItemsName , boolean [] moreChooseItemNeedCallBack ,SelectOptions.onMoreChooseItemsListener ...itemListeners){
        if (ItemsName.length!=itemListeners.length){
            throw new IllegalArgumentException("选项名和点击响应必须一一对应");
        }
        else {
            mSelectOptions.needMoreChoose=true;
            mSelectOptions.MoreChooseItemName=ItemsName;
            mSelectOptions.moreChooseItemNeedCallBack=moreChooseItemNeedCallBack;
            mSelectOptions.moreChooseItemListeners=itemListeners;
        }
        return this;
    }


    /**
     * FileItem点击/长按回调
     * @param onFileItem
     * @return
     */
    public SelectManager setFileItemListener(SelectOptions.onFileItemListener onFileItem){
        mSelectOptions.fileItemListener=onFileItem;
        return this;
    }



    /**
     * 标题相关选项
     * @param title
     * @return
     */
    public SelectManager setToolbarMainTitle(String title){
        mSelectOptions.toolbarMainTitle=title;
        return this;
    }
    public SelectManager setToolbarSubtitleTitle(String title){
        mSelectOptions.toolbarSubtitleTitle=title;
        return this;
    }

    public SelectManager setToolbarBG(int color) {
        mSelectOptions.toolbarBG=color;
        return this;
    }

    public SelectManager setToolbarMainTitleColor(int color) {
        mSelectOptions.toolbarMainTitleColor=color;
        return this;
    }
    public SelectManager setToolbarSubtitleColor(int color) {
        mSelectOptions.toolbarSubtitleColor=color;
        return this;
    }
    public SelectManager setToolbarOptionColor(int color) {
        mSelectOptions.toolbarOptionColor=color;
        return this;
    }

    public SelectManager setToolbarOptionSize(int size) {
        mSelectOptions.toolbarOptionSize=size;
        return this;
    }

    /**
     * 开始打开文件选择器
     */
    public PathSelectFragment start() {
        return BuildControl.show(mPathSelector,mSelectOptions);
    }

}
