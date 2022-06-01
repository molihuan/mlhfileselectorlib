package com.zlylib.mlhfileselectorlib;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.zlylib.mlhfileselectorlib.ui.FileSelectorActivity;

/**
 * SelectCreator
 * Created by zhangliyang on 2020/6/20.
 */

public final class SelectCreator {

    private  FileSelector filePicker;
    private  SelectOptions selectOptions;

    public SelectCreator(FileSelector filePicker ) {
        selectOptions = SelectOptions.getCleanInstance();
        this.filePicker = filePicker;
    }

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


    public SelectCreator setTargetPath(String path){
        selectOptions.targetPath = path;
        return this;
    }



    public SelectCreator setFileTypes(String... fileTypes) {
        selectOptions.mFileTypes = fileTypes;
        return this;
    }

    public SelectCreator setSortType(int sortType) {
        selectOptions.setSortType(sortType);
        return this;
    }

    public SelectCreator isSingle() {
        selectOptions.isSingle = true;
        selectOptions.maxCount = 1;
        return this;
    }

    public SelectCreator onlyShowFolder() {
        selectOptions.setOnlyShowFolder(true);
        selectOptions.setOnlySelectFolder(true);
        return this;
    }
    public SelectCreator onlySelectFolder() {
        selectOptions.setOnlySelectFolder(true);
        return this;
    }
    public SelectCreator setTilteBg(int color) {
        selectOptions.setTitleBg(color);
        return this;
    }
    public SelectCreator setTitleColor(int color) {
        selectOptions.setTitleColor(color);
        return this;
    }
    public SelectCreator setTitleLiftColor(int color) {
        selectOptions.setTitleLiftColor(color);
        return this;
    }
    public SelectCreator setTitleRightColor(int color) {
        selectOptions.setTitleRightColor(color);
        return this;
    }
   /* public SelectCreator onlyShowImages() {
        selectOptions.onlyShowImages = true;
        return this;
    }

    public SelectCreator onlyShowVideos() {
        selectOptions.onlyShowVideos = true;
        return this;
    }
*/

    public SelectCreator requestCode(int requestCode) {
        selectOptions.request_code = requestCode;
        return this;
    }

    public void start() {
        final Context activity = filePicker.getActivity();
        if (activity == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(activity, FileSelectorActivity.class);
        Fragment fragment = filePicker.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, selectOptions.request_code);
        } else {
            ((AppCompatActivity)activity).startActivityForResult(intent, selectOptions.request_code);
        }
    }

}
