package com.molihuan.pathselector.entities;

import androidx.documentfile.provider.DocumentFile;

import com.molihuan.pathselector.utils.Commons;
import com.molihuan.pathselector.utils.FileTools;
import com.molihuan.pathselector.utils.PermissionsTools;
import com.molihuan.pathselector.utils.UriTools;

import java.io.File;

/**
 * @ClassName FileBean
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/24 2:12
 */

public class TabbarFileBean {
    private String filePath;//文件路径(必须)
    private String fileName;//名称
    private String fileNameNoExtension;//名称(无后缀)
    private String parentPath;//上一级目录/父目录
    private String parentName;//上一级文件夹名称

    private Boolean useUri;//是否使用uri地址
    /**
     * 通过DocumentFile可以对Android/data目录进行操作
     * 具体怎么操作可以百度
     *
     */
    private DocumentFile documentFile;//documentFile

    public TabbarFileBean(String filePath) {
        this(filePath,false);
    }
    public TabbarFileBean(String filePath, Boolean useUri) {
        this(filePath,useUri,null);
    }

    public TabbarFileBean(String filePath, Boolean useUri,DocumentFile documentFile) {
        this.filePath = filePath;
        this.useUri = useUri;

        if (PermissionsTools.isAndroid11()){
            this.useUri = useUri;
        }else {
            this.useUri = false;
        }

        if (this.useUri){
            this.documentFile=documentFile;
            File uriFile = UriTools.uri2File(documentFile.getUri(), Commons.getApplicationByReflect().getBaseContext(), Commons.getApplicationByReflect());
            fileName= FileTools.getFileName(uriFile);
            fileNameNoExtension= FileTools.getFileNameNoExtension(uriFile);
            parentPath=null;
            parentName=FileTools.getDirName(uriFile);

        }else {
            fileName= FileTools.getFileName(filePath);
            fileNameNoExtension= FileTools.getFileNameNoExtension(filePath);
            parentPath=FileTools.getParentPath(filePath);
            parentName=FileTools.getDirName(filePath);
        }




    }



    public TabbarFileBean(String filePath,String fileName, Boolean useUri,DocumentFile documentFile) {
        this.filePath = filePath;
        this.useUri = useUri;
        this.fileName= fileName;

        if (this.useUri){
            this.documentFile=documentFile;
            File uriFile = UriTools.uri2File(documentFile.getUri(), Commons.getApplicationByReflect().getBaseContext(), Commons.getApplicationByReflect());
            fileNameNoExtension= FileTools.getFileNameNoExtension(uriFile);
            parentPath=null;
            parentName=FileTools.getDirName(uriFile);

        }else {

            fileNameNoExtension = fileName;
            parentPath = FileTools.getParentPath(filePath);
            parentName = FileTools.getDirName(filePath);

        }

    }



    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setUseUri(Boolean useUri) {
        this.useUri = useUri;
    }

    public void setDocumentFile(DocumentFile documentFile) {
        this.documentFile = documentFile;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileNameNoExtension() {
        return fileNameNoExtension;
    }

    public String getParentPath() {
        return parentPath;
    }

    public String getParentName() {
        return parentName;
    }

    public Boolean isUseUri() {
        return useUri;
    }

    public DocumentFile getDocumentFile() {
        return documentFile;
    }
}
