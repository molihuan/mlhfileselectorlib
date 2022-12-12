package com.molihuan.pathselector.entity;

import androidx.documentfile.provider.DocumentFile;

import java.io.Serializable;

/**
 * @ClassName: TabbarFileBean
 * @Author: molihuan
 * @Date: 2022/11/22/21:51
 * @Description:
 */
public class TabbarFileBean implements Serializable {

    private String path;         //文件路径(必须)
    private String name;         //名称
    private Long flag;
    private Boolean useUri;         //是否使用uri地址
    /**
     * 通过DocumentFile可以对Android/data目录进行操作
     * 具体怎么操作可以百度
     */
    private DocumentFile documentFile;

    public TabbarFileBean() {
    }

    public TabbarFileBean(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public TabbarFileBean(String path, String name, Long flag) {
        this.flag = flag;
        this.path = path;
        this.name = name;
    }

    public TabbarFileBean(String path, String name, Long flag, Boolean useUri, DocumentFile documentFile) {
        this.flag = flag;
        this.path = path;
        this.name = name;
        this.useUri = useUri;
        this.documentFile = documentFile;
    }

    public void clear() {
        this.flag = null;
        this.path = null;
        this.name = null;
        this.useUri = null;
        this.documentFile = null;
    }

    public Long getFlag() {
        return flag;
    }

    public void setFlag(Long flag) {
        this.flag = flag;
    }

    public String getPath() {
        return path;
    }

    public TabbarFileBean setPath(String path) {
        this.path = path;
        return this;
    }

    public String getName() {
        return name;
    }

    public TabbarFileBean setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getUseUri() {
        return useUri;
    }

    public TabbarFileBean setUseUri(Boolean useUri) {
        this.useUri = useUri;
        return this;
    }

    public DocumentFile getDocumentFile() {
        return documentFile;
    }

    public TabbarFileBean setDocumentFile(DocumentFile documentFile) {
        this.documentFile = documentFile;
        return this;
    }

    @Override
    public String toString() {
        return "TabbarFileBean{" +
                "flag=" + flag +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", useUri=" + useUri +
                ", documentFile=" + documentFile +
                '}';
    }


}
