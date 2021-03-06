package com.zlylib.mlhfileselectorlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.zlylib.mlhfileselectorlib.activity.FileSelectorActivity;

/**
 * SelectCreator
 * Created by zhangliyang on 2020/6/20.
 * Updata by molihuan on 2022/6/5
 */

public final class SelectCreator {

    private  FileSelector filePicker;
    private  SelectOptions selectOptions;



    public SelectCreator(FileSelector filePicker ) {
        selectOptions = SelectOptions.getCleanInstance(filePicker.getActivity());
        this.filePicker = filePicker;
    }


    /**
     * 设置一些Toolbar选项
     * @param optionsName
     * @param onOptionClicks
     * @return
     */
    public SelectCreator setMoreOPtions(String[] optionsName ,SelectOptions.IToolbarOptionsListener ...onOptionClicks){
        return setMoreOPtions(optionsName,new boolean [optionsName.length],onOptionClicks);
    }
    /**
     * 设置一些Toolbar选项
     * @param optionsName
     * @param onOptionClicks
     * @return
     */
    public SelectCreator setMoreOPtions(String[] optionsName , boolean [] optionsNeedCallBack ,SelectOptions.IToolbarOptionsListener ...onOptionClicks){
        if (optionsName.length!=onOptionClicks.length&&optionsName.length!=optionsNeedCallBack.length){
            throw new IllegalArgumentException("选项名和点击响应必须一一对应");
        }
        else {
            selectOptions.setNeedMoreOptions(true);
            selectOptions.setOptionsName(optionsName);
            selectOptions.setOptionsNeedCallBack(optionsNeedCallBack);
            selectOptions.setOnOptionClicks(onOptionClicks);
        }
        return this;
    }

    public SelectCreator setMoreChooseItems(String[] ItemsName ,SelectOptions.IMoreChooseItemsListener ...onItemClicks){
        return setMoreChooseItems(ItemsName,new boolean [ItemsName.length],onItemClicks);
    }
    /**
     * 设置一些MoreChooseItems选项
     * @param ItemsName
     * @param onItemClicks
     * @return
     */
    public SelectCreator setMoreChooseItems(String[] ItemsName , boolean [] moreChooseItemNeedCallBack ,SelectOptions.IMoreChooseItemsListener ...onItemClicks){
        if (ItemsName.length!=onItemClicks.length){
            throw new IllegalArgumentException("选项名和点击响应必须一一对应");
        }
        else {
            selectOptions.setNeedMoreChoose(true);
            selectOptions.setMoreChooseItemName(ItemsName);
            selectOptions.setMoreChooseItemNeedCallBack(moreChooseItemNeedCallBack);
            selectOptions.setOnItemClicks(onItemClicks);
        }
        return this;
    }

    /**
     * FileItem点击/长按回调
     * @param onFileItem
     * @return
     */
    public SelectCreator setFileItemDispose(SelectOptions.IOnFileItemListener onFileItem){
        selectOptions.setOnFileItem(onFileItem);
        return this;
    }



    /**
     * 设置最大选择数量
     * @param maxCount
     * @return
     */
    public SelectCreator setMaxCount(int maxCount) {
        selectOptions.maxCount = maxCount;
        if (maxCount <= 1) {
            selectOptions.maxCount = 1;
            selectOptions.isSingle = true;
        } else {
            selectOptions.isSingle = false;
        }
        return this;
    }
    /**
     * 设置标题栏
     * @param toolbarFragment
     * @return
     */
    public SelectCreator setToolbar(Fragment toolbarFragment) {
        selectOptions.toolbarFragment = toolbarFragment;
        return this;
    }

    /**
     * 设置开始默认路径
     * @param path
     * @return
     */
    public SelectCreator setTargetPath(String path){
        selectOptions.targetPath = path;
        return this;
    }


    /**
     * 设置选择文件类型
     * @param fileTypes
     * @return
     */
    public SelectCreator setFileTypes(String... fileTypes) {
        selectOptions.mFileTypes = fileTypes;
        return this;
    }

    /**
     * 设置排序规则
     * @param sortType
     * @return
     */
    public SelectCreator setSortType(int sortType) {
        selectOptions.setSortType(sortType);
        return this;
    }

    /**
     * 设置单选
     * @return
     */
    public SelectCreator isSingle() {
        selectOptions.isSingle = true;
        selectOptions.maxCount = 1;
        return this;
    }

    /**
     * 设置只显示文件夹
     * @return
     */
    public SelectCreator onlyShowFolder() {
        selectOptions.setOnlyShowFolder(true);
        selectOptions.setOnlySelectFolder(true);
        return this;
    }

    /**
     * 设置只选择文件夹
     * @return
     */
    public SelectCreator onlySelectFolder() {
        selectOptions.setOnlySelectFolder(true);
        return this;
    }


    /**
     * 标题相关选项
     * @param toolbarTitle
     * @return
     */
    public SelectCreator setToolbarTitle(String toolbarTitle){
        selectOptions.setToolbarTitle(toolbarTitle);
        return this;
    }

    public SelectCreator setTilteBg(int color) {
        selectOptions.setToolbarColor(color);
        return this;
    }

    public SelectCreator setTitleColor(int color) {
        selectOptions.setTitleColor(color);
        return this;
    }
    public SelectCreator setOneOptionColor(int color) {
        selectOptions.setOneOptionColor(color);
        return this;
    }

    public SelectCreator setOneOptionSize(int size) {
        selectOptions.setOneOptionSize(size);
        return this;
    }







    public SelectCreator onlyShowImages() {
        selectOptions.onlyShowImages = true;
        return this;
    }

    public SelectCreator onlyShowVideos() {
        selectOptions.onlyShowVideos = true;
        return this;
    }

    /**
     * 设置请求码
     * @param requestCode
     * @return
     */
    public SelectCreator requestCode(int requestCode) {
        selectOptions.request_code = requestCode;
        return this;
    }


    /**
     * 开始打开文件选择器
     */
    public void start() {
        final Context activity = filePicker.getActivity();
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, FileSelectorActivity.class);
        Fragment fragment = filePicker.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, selectOptions.request_code);//设置返回码
        } else {
            ((Activity)activity).startActivityForResult(intent, selectOptions.request_code);
        }
    }

}
