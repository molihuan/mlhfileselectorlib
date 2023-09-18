package com.molihuan.pathselector.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import com.molihuan.pathselector.adapter.FileListAdapter;
import com.molihuan.pathselector.adapter.TabbarListAdapter;
import com.molihuan.pathselector.controller.AbstractFileBeanController;
import com.molihuan.pathselector.dao.SelectConfigData;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.entity.TabbarFileBean;
import com.molihuan.pathselector.hooks.AbstractLifeCycle;
import com.molihuan.pathselector.service.impl.ConfigDataBuilderImpl;
import com.molihuan.pathselector.utils.FileTools;
import com.molihuan.pathselector.utils.MConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: BaseFileManager
 * @Author: molihuan
 * @Date: 2022/12/09/19:49
 * @Description:
 */
public abstract class BaseFileManager implements IFileDataManager {
    public static final int TYPE_REFRESH_FILE = 1;
    public static final int TYPE_REFRESH_TABBAR = 2;
    public static final int TYPE_REFRESH_FILE_TABBAR = 3;

    protected SelectConfigData mConfigData = ConfigDataBuilderImpl.getInstance().getSelectConfigData();

    protected AbstractFileBeanController mFileBeanController = mConfigData.fileBeanController;
    protected AbstractLifeCycle mLifeCycle = mConfigData.lifeCycle;

    @Override
    public List<FileBean> initFileList(String currentPath, List<FileBean> fileList) {
        //TODO 这里会缓存一些进行复用(会自动扩容),如果不需要频繁操作或长时间使用可以回收掉过多没有使用的缓存item,
        if (fileList == null) {
            fileList = new ArrayList<>();
        }

        //获取当前路径的上一级目录
        String parentPath = FileTools.getParentPath(currentPath);
        switch (fileList.size()) {
            case 0://如果列表个数为0则需要添加一个充当返回的FileBean item提供点击就可以返回到上一级目录这个FileBean.path应设置为上一级目录的路径
                FileBean fileBean = new FileBean(parentPath, "...", MConstants.FILEBEAN_BACK_FLAG);

                fileBean.setFileIcoType(mFileBeanController.getFileBeanImageResource(
                        true,
                        "This is back filebean item",
                        fileBean
                ));

                fileList.add(fileBean);
                break;
            default://如果已经有了就修改,并且把0索引后面的实例都进行赋值null初始化
                fileList.get(0).setPath(parentPath);
                //TODO 这里选择的是直接移除因为太耗时了，后面再进行优化
                for (int i = fileList.size() - 1; i >= 1; i--) {
                    fileList.remove(i);
                }
        }

        return fileList;
    }

    @Override
    public List<FileBean> clearFileListCache(List<FileBean> fileList) {
        Objects.requireNonNull(fileList, "fileList is null");
        for (int i = fileList.size() - 1; i >= 0; i--) {
            if (fileList.get(i).getPath() == null) {
                fileList.remove(i);
            }
        }
        return fileList;
    }

    @Override
    public List<FileBean> sortFileList(List<FileBean> fileList, int sortType, String currentPath) {
        Collections.sort(fileList, new Comparator<FileBean>() {
            /**
             * 注意返回值
             * @param o1
             * @param o2
             * @return 1交换 -1不交换
             */
            @Override
            public int compare(FileBean o1, FileBean o2) {

                //如果是空fileBean就换
                if (o1.getPath() == null) {
                    return 1;
                }
                if (o2.getPath() == null) {
                    return -1;
                }

                //如果是返回fileBean就不换
                if (o1.getSize() == MConstants.FILEBEAN_BACK_FLAG) {
                    return -1;
                }
                if (o2.getSize() == MConstants.FILEBEAN_BACK_FLAG) {
                    return 1;
                }

                //如果前面的是文件夹就不换,下面相反
                if (o1.isDir() && (!o2.isDir())) {
                    return -1;
                }
                if ((!o1.isDir()) && o2.isDir()) {
                    return 1;
                }

                switch (sortType) {
                    case MConstants.SORT_NAME_ASC:
                        return o1.getName().compareToIgnoreCase(o2.getName());//根据名称字符串ASCLL码进行比较(忽略大小写)
                    case MConstants.SORT_NAME_DESC:
                        return o2.getName().compareToIgnoreCase(o1.getName());
                    case MConstants.SORT_TIME_ASC:
                        long diff = o1.getModifyTime() - o2.getModifyTime();
                        if (diff > 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    case MConstants.SORT_TIME_DESC:
                        diff = o2.getModifyTime() - o1.getModifyTime();
                        if (diff > 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    case MConstants.SORT_SIZE_ASC:
                        diff = o1.getSize() - o2.getSize();
                        if (diff > 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    case MConstants.SORT_SIZE_DESC:
                        diff = o2.getSize() - o1.getSize();
                        if (diff > 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    default:
                        return 0;
                }
            }
        });
        return fileList;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void refreshFileTabbar(FileListAdapter fileAdapter, TabbarListAdapter tabbarAdapter, int level) {
        switch (level) {
            case TYPE_REFRESH_FILE:
                if (fileAdapter == null) {
                    return;
                }
                fileAdapter.notifyDataSetChanged();
                break;
            case TYPE_REFRESH_TABBAR:
                if (tabbarAdapter == null) {
                    return;
                }
                tabbarAdapter.notifyDataSetChanged();
                break;
            case TYPE_REFRESH_FILE_TABBAR:
                if (fileAdapter == null || tabbarAdapter == null) {
                    return;
                }
                fileAdapter.notifyDataSetChanged();
                tabbarAdapter.notifyDataSetChanged();
                break;
            default:
                throw new IllegalArgumentException("Parameter does not conform to a predefined value");

        }
    }

    @Override
    public List<TabbarFileBean> initTabbarList(String initPath, List<TabbarFileBean> tabbarList) {
        if (tabbarList == null) {
            tabbarList = new ArrayList<>();
        }

        switch (tabbarList.size()) {
            case 0://如果列表个数为0则需要添加一个充当最初路径的TabbarFileBean item提供点击就可以返回到最初的目录，这个TabbarFileBean.path应设置最初路径
                //tabbarList.add(new TabbarFileBean(initPath, "Storage", MConstants.TABBARFILEBEAN_INIT_FLAG));
                break;
            default://如果已经有了就修改,并且把0索引后面的实例都进行赋值null初始化
                for (int i = tabbarList.size() - 1; i >= 0; i--) {
                    tabbarList.remove(i);
                }
        }
        return tabbarList;
    }

    @Override
    public List<TabbarFileBean> clearTabbarListCache(List<TabbarFileBean> tabbarList) {
        Objects.requireNonNull(tabbarList, "tabbarList is null");
        for (int i = tabbarList.size() - 1; i >= 0; i--) {
            if (tabbarList.get(i).getPath() == null) {
                tabbarList.remove(i);
            }
        }
        return tabbarList;
    }

    @Override
    public List<TabbarFileBean> updateTabbarList(String initPath, String currentPath, List<TabbarFileBean> tabbarList, TabbarListAdapter tabbarAdapter) {

        //currentPath = "/storage/emulated/0";
        tabbarList = initTabbarList(currentPath, tabbarList);
        //通过/分割
        String[] parts = currentPath.split(File.separator);
        /**
         * (空)
         * storage
         * emulated
         * 0
         */

        if (parts.length == 0) {
            return tabbarList;
        }

        StringBuilder builder = new StringBuilder();
        /**组合成分级
         *  parts[0] = (null)
         *  parts[1] = /storage
         *  parts[2] = /storage/emulated
         *  parts[3] = /storage/emulated/0
         */
        for (int i = 1; i < parts.length; i++) {
            parts[i] = builder.append(File.separator + parts[i]).toString();
        }

        TabbarFileBean tabbarBean;
        for (int i = 1; i < parts.length; i++) {
            if (false) {
                /**
                 * 如果还有缓存的FileBean就设置属性即可
                 * 0索引FileBean为返回按钮所以+1
                 */
                tabbarList.get(i)
                        .setPath(parts[i])
                        .setName(FileTools.getFileName(parts[i]))
                        .setUseUri(false);
            } else {
                tabbarBean = new TabbarFileBean()
                        .setPath(parts[i])
                        .setName(FileTools.getFileName(parts[i]))
                        .setUseUri(false);
                tabbarList.add(tabbarBean);
            }
        }

        return tabbarList;
    }

    @Override
    public List<FileBean> initSelectedFileList(List<FileBean> selectedList) {
        if (selectedList == null) {
            selectedList = new ArrayList<>();
        } else {
            selectedList.clear();
        }
        return selectedList;
    }

    @Override
    public List<FileBean> getSelectedFileList(List<FileBean> allFileList, List<FileBean> selectedList) {
        selectedList = initSelectedFileList(selectedList);
        Objects.requireNonNull(allFileList, "allFileList is null");
        for (FileBean fileBean : allFileList) {
            if (fileBean.getPath() != null && fileBean.getBoxChecked() != null && fileBean.getBoxChecked()) {
                selectedList.add(fileBean);
            }
        }
        return selectedList;
    }


    @Override
    public void returnDataToActivityResult(List<FileBean> selectedFileList, Activity activity) {
        ArrayList<String> selectedPath = new ArrayList<>();
        for (FileBean bean : selectedFileList) {
            selectedPath.add(bean.getPath());
        }
        Intent result = new Intent();
        result.putStringArrayListExtra(MConstants.CALLBACK_DATA_ARRAYLIST_STRING, selectedPath);
        activity.setResult(activity.RESULT_OK, result);//设置返回原界面的结果
        activity.finish();
    }


    @Override
    public List<FileBean> setCheckBoxVisible(List<FileBean> fileList, FileListAdapter fileAdapter, boolean state) {
        FileBean fileBean;
        for (int i = 0; i < fileList.size(); i++) {
            fileBean = fileList.get(i);
            //判断是不是未使用的item
            if (fileBean.getPath() == null) {
                break;
            }
            //判断是不是返回item
            if (fileBean.getSize() == MConstants.FILEBEAN_BACK_FLAG) {
                continue;
            }
            fileBean.setBoxVisible(state);
            //显示和不显示时都设为不选中防止有缓存
            fileBean.setBoxChecked(false);
        }
        return fileList;
    }

    @Override
    public List<FileBean> setBoxChecked(List<FileBean> fileList, FileListAdapter fileAdapter, boolean state) {

        FileBean fileBean;
        for (int i = 0; i < fileList.size(); i++) {
            fileBean = fileList.get(i);
            //判断是不是未使用的item
            if (fileBean.getPath() == null) {
                break;
            }
            //判断是不是返回item
            if (fileBean.getSize() == MConstants.FILEBEAN_BACK_FLAG) {
                continue;
            }
            fileBean.setBoxChecked(state);
        }
        return fileList;
    }


}
