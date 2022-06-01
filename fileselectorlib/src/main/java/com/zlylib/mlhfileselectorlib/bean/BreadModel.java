package com.zlylib.mlhfileselectorlib.bean;


import java.util.ArrayList;
import java.util.List;

/**
 * BreadModel
 * Created by zhangliyang on 2018/2/5.
 */

public class BreadModel {

    /*当前Path*/
    private String mCurPath;

    /*之前的浏览位置，默认0*/
    private int mPrePosition = 0;

    /*当前面包屑的名称*/
    private String mCurName;

    public String getCurName() {
        return mCurName;
    }

    public void setCurName(String mCurName) {
        this.mCurName = mCurName;
    }

    public int getPrePosition() {
        return mPrePosition;
    }

    public void setPrePosition(int mPrePosition) {
        this.mPrePosition = mPrePosition;
    }

    public String getCurPath() {
        return mCurPath;
    }

    public void setmCurPath(String mCurPath) {
        this.mCurPath = mCurPath;
    }

    /**
     * 获取需要添加的路径列表
     * @param oldList
     * @param newList
     * @return
     */
    public static List<BreadModel> getNewBreadModel(List<BreadModel> oldList, List<BreadModel> newList) {
        List<BreadModel> newModelList = new ArrayList<>();
        if (oldList == null || newList == null) {
            return newModelList;
        }
        if (oldList.size() >= newList.size()) {
            return newModelList;
        }
        for (int i = 0; i < newList.size(); i++) {
            if (i < oldList.size()) {
                continue;
            }
            newModelList.add(newList.get(i));
        }
        return newModelList;
    }

    /**
     * 获取路径List需要裁剪的位置
     * @param oldList
     * @param newList
     * @return
     */
    public static int getRemovedBreadModel(List<BreadModel> oldList, List<BreadModel> newList) {
        int removeFirstPosition = 0;
        if (oldList == null || newList == null) {
            return removeFirstPosition;
        }
        if (oldList.size() <= newList.size()) {
            return removeFirstPosition;
        }
        for (int i = 0; i < oldList.size(); i++) {
            if (i == newList.size()) {
                return i;
            }
        }
        return removeFirstPosition;
    }


}
