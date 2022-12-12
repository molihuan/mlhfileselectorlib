package com.molihuan.pathselector.entity;

/**
 * @ClassName: StorageBean
 * @Author: molihuan
 * @Date: 2022/12/11/16:26
 * @Description:
 */
public class StorageBean {
    private String rootPath;
    private Boolean selected;

    public StorageBean() {
    }

    public StorageBean(String rootPath, Boolean selected) {
        this.rootPath = rootPath;
        this.selected = selected;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "StorageBean{" +
                "rootPath='" + rootPath + '\'' +
                ", selected=" + selected +
                '}';
    }
}
