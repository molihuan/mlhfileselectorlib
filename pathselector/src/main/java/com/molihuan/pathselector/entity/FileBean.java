package com.molihuan.pathselector.entity;

import androidx.documentfile.provider.DocumentFile;

import java.io.Serializable;

/**
 * @ClassName: FileBean
 * @Author: molihuan
 * @Date: 2022/11/22/21:51
 * @Description: 封装的文件信息
 * 有三种状态：
 * 1、正常FileBean：所以的字段都基本上有值（不为null）
 * 2、返回FileBean：仅仅有path、name、size其他字段为null通过size=-5411来作为标志
 * 3、缓存FileBean：所有字段均为null,但存在FileBeanList中,用来复用
 */
public class FileBean implements Serializable {
    private String path;                //文件路径(必须)
    private String name;                //名称
    private Boolean dir;                    //是否是文件夹
    private String fileExtension;           //后缀
    private Integer fileIcoType;               //文件图片类型
    private Integer childrenFileNumber;     //子文件数量
    private Integer childrenDirNumber;      //子文件夹数量
    private Boolean boxVisible;                //checkbox是否显示
    private Boolean boxChecked;                //checkbox是否选择
    private Long modifyTime;                //文件修改时间
    private Long size;                      //占空间大小.还有一个作用就是如果size=-5411则说明是返回FileBean
    private String sizeString;                      //占空间大小.还有一个作用就是如果size=-5411则说明是返回FileBean
    /**
     * 通过DocumentFile可以对Android/data目录进行操作
     * 具体怎么操作可以百度
     */
    private DocumentFile documentFile;
    private Boolean useUri;                 //是否使用uri地址

    public FileBean() {
    }

    public FileBean(String path) {
        this.path = path;
    }


    public FileBean(String path, String name, Long size) {
        this.path = path;
        this.name = name;
        this.size = size;
    }

    public FileBean(String path, String name, Boolean dir, String fileExtension, Integer fileIcoType, Integer childrenFileNumber, Integer childrenDirNumber, Boolean boxVisible, Boolean boxChecked, Long modifyTime, Long size, DocumentFile documentFile, Boolean useUri) {
        this.path = path;
        this.name = name;
        this.dir = dir;
        this.fileExtension = fileExtension;
        this.fileIcoType = fileIcoType;
        this.childrenFileNumber = childrenFileNumber;
        this.childrenDirNumber = childrenDirNumber;
        this.boxVisible = boxVisible;
        this.boxChecked = boxChecked;
        this.modifyTime = modifyTime;
        this.size = size;
        this.documentFile = documentFile;
        this.useUri = useUri;
    }

    public String getPath() {
        return path;
    }

    public FileBean setPath(String path) {
        this.path = path;
        return this;
    }

    public FileBean clear() {
        this.path = null;
        this.name = null;
        this.dir = null;
        this.fileExtension = null;
        this.fileIcoType = null;
        this.childrenFileNumber = null;
        this.childrenDirNumber = null;
        this.boxVisible = null;
        this.boxChecked = null;
        this.modifyTime = null;
        this.size = null;
        this.documentFile = null;
        this.useUri = null;
        return this;
    }

    public String getName() {
        return name;
    }

    public FileBean setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean isDir() {
        return dir;
    }

    public FileBean setDir(Boolean dir) {
        this.dir = dir;
        return this;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public FileBean setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
        return this;
    }

    public Integer getFileIcoType() {
        return fileIcoType;
    }

    public FileBean setFileIcoType(Integer fileIcoType) {
        this.fileIcoType = fileIcoType;
        return this;
    }


    public Integer getChildrenFileNumber() {
        return childrenFileNumber;
    }

    public FileBean setChildrenFileNumber(Integer childrenFileNumber) {
        this.childrenFileNumber = childrenFileNumber;
        return this;
    }

    public Integer getChildrenDirNumber() {
        return childrenDirNumber;
    }

    public FileBean setChildrenDirNumber(Integer childrenDirNumber) {
        this.childrenDirNumber = childrenDirNumber;
        return this;
    }

    public Boolean getBoxVisible() {
        return boxVisible;
    }

    public FileBean setBoxVisible(Boolean boxVisible) {
        this.boxVisible = boxVisible;
        return this;
    }

    public Boolean getBoxChecked() {
        return boxChecked;
    }

    public FileBean setBoxChecked(Boolean boxChecked) {
        this.boxChecked = boxChecked;
        return this;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public FileBean setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public FileBean setSize(Long size) {
        this.size = size;
        return this;
    }

    public String getSizeString() {
        return sizeString;
    }

    public FileBean setSizeString(String sizeString) {
        this.sizeString = sizeString;
        return this;
    }

    public DocumentFile getDocumentFile() {
        return documentFile;
    }

    public FileBean setDocumentFile(DocumentFile documentFile) {
        this.documentFile = documentFile;
        return this;
    }

    public Boolean getUseUri() {
        return useUri;
    }

    public FileBean setUseUri(Boolean useUri) {
        this.useUri = useUri;
        return this;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", dir=" + dir +
                ", fileExtension='" + fileExtension + '\'' +
                ", fileIcoType=" + fileIcoType +
                ", childrenFileNumber=" + childrenFileNumber +
                ", childrenDirNumber=" + childrenDirNumber +
                ", boxVisible=" + boxVisible +
                ", boxChecked=" + boxChecked +
                ", modifyTime=" + modifyTime +
                ", size=" + size +
                ", documentFile=" + documentFile +
                ", useUri=" + useUri +
                '}';
    }
}
