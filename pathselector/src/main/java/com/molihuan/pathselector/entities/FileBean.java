package com.molihuan.pathselector.entities;

import androidx.documentfile.provider.DocumentFile;

import com.molihuan.pathselector.R;
import com.molihuan.pathselector.utils.Commons;
import com.molihuan.pathselector.utils.FileTools;
import com.molihuan.pathselector.utils.UriTools;

import java.io.File;

/**
 * @ClassName FileBean
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/24 2:12
 */

public class FileBean {
    private String filePath;//文件路径(必须)
    private boolean dir;//是否是文件夹
    private boolean file;//是否文件
    private String fileName;//名称
    private String fileNameNoExtension;//名称(无后缀)
    private String fileExtension;//后缀
    private int fileImgType;//文件图片类型
    private String parentPath;//上一级目录/父目录
    private String parentName;//上一级文件夹名称
    private int childrenFileNumber;//子文件数量
    private int childrenDirNumber;//子文件夹数量
    private String size;//占空间大小
    private boolean visible;//checkbox是否显示
    private boolean checked;//checkbox是否选择

    private boolean useUri;//是否使用uri地址
    /**
     * 通过DocumentFile可以对Android/data目录进行操作
     * 具体怎么操作可以百度
     */
    private DocumentFile documentFile;//documentFile

    private long modifyTime;//文件修改时间
    private long simpleSize;//简单文件大小



    public FileBean(String filePath) {
        this(filePath,false);
    }
    public FileBean(String filePath, Boolean useUri) {
        this(filePath,useUri,null);
    }

    public FileBean(String filePath, Boolean useUri,DocumentFile documentFile) {
        this.filePath = filePath;
        this.useUri = useUri;
//        if (SelectOptions.getInstance().mSingle) {
//            visible=true;
//        }else {
//            visible=false;
//        }
        visible=false;
        checked=false;

        if (this.useUri){//使用Uri
            this.documentFile=documentFile;
            //获取file对象通过uri
            File uriFile = UriTools.uri2File(documentFile.getUri(), Commons.getApplicationByReflect().getBaseContext(), Commons.getApplicationByReflect());

            if (documentFile.isFile()){
                file=true;
                dir=false;
            }else {
                file=false;
                dir=true;
            }
            fileName= FileTools.getFileName(uriFile);
            fileNameNoExtension= FileTools.getFileNameNoExtension(uriFile);
            fileExtension=FileTools.getFileExtension(uriFile);
            fileImgType=setImageResourceByExtension(fileExtension);
            parentPath=null;
            parentName=FileTools.getDirName(uriFile);
            childrenFileNumber=0;
            childrenDirNumber=0;

            modifyTime=documentFile.lastModified();
            if (file){//非常耗时
                size=FileTools.getSize(uriFile);
                simpleSize =FileTools.getSimpleSize(uriFile);
            }
        }else {//直接使用路径

            if (FileTools.isFile(filePath)){
                file=true;
                dir=false;
            }else {
                file=false;
                dir=true;
            }
            fileName= FileTools.getFileName(filePath);
            fileNameNoExtension= FileTools.getFileNameNoExtension(filePath);
            fileExtension=FileTools.getFileExtension(filePath);
            fileImgType=setImageResourceByExtension(fileExtension);
            parentPath=FileTools.getParentPath(filePath);
            parentName=FileTools.getDirName(filePath);
            childrenFileNumber=FileTools.getChildrenNumber(filePath)[0];
            childrenDirNumber=FileTools.getChildrenNumber(filePath)[1];

            modifyTime=FileTools.getFileLastModified(filePath);
            if (file) {//非常耗时
                size=FileTools.getSize(filePath);
                simpleSize =FileTools.getSimpleSize(filePath);
            }

        }




    }



    /**
     * 根据后缀选择显示图片
     * @param extension
     * @return
     */

    public int setImageResourceByExtension(String extension) {
        int resourceId;
        switch (extension){
            case "apk":
                resourceId=R.mipmap.apk;
                break;
            case "avi":
                resourceId=R.mipmap.avi;
                break;
            case "doc":
            case "docx":
                resourceId=R.mipmap.doc;
                break;
            case "exe":
                resourceId=R.mipmap.exe;
                break;
            case "flv":
                resourceId=R.mipmap.flv;
                break;
            case "gif":
                resourceId=R.mipmap.gif;
                break;
            case "jpg":
            case "jpeg":
            case "png":
                resourceId=R.mipmap.png;
                break;
            case "mp3":
                resourceId=R.mipmap.mp3;
                break;
            case "mp4":
            case "f4v":
                resourceId=R.mipmap.movie;
                break;
            case "pdf":
                resourceId=R.mipmap.pdf;
                break;
            case "ppt":
            case "pptx":
                resourceId=R.mipmap.ppt;
                break;
            case "wav":
                resourceId=R.mipmap.wav;
                break;
            case "xls":
            case "xlsx":
                resourceId=R.mipmap.xls;
                break;
            case "zip":
                resourceId=R.mipmap.zip;
                break;
            case "ext":
            default:
                if (dir) {
                    resourceId=R.mipmap.folder;
                } else {
                    resourceId=R.mipmap.documents;
                }
                break;
        }
        return resourceId;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setUseUri(boolean useUri) {
        this.useUri = useUri;
    }

    public void setDocumentFile(DocumentFile documentFile) {
        this.documentFile = documentFile;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isDir() {
        return dir;
    }

    public boolean isFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileNameNoExtension() {
        return fileNameNoExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public int getFileImgType() {
        return fileImgType;
    }

    public String getParentPath() {
        return parentPath;
    }

    public String getParentName() {
        return parentName;
    }

    public int getChildrenFileNumber() {
        return childrenFileNumber;
    }

    public int getChildrenDirNumber() {
        return childrenDirNumber;
    }

    public String getSize() {
        return size;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isUseUri() {
        return useUri;
    }

    public DocumentFile getDocumentFile() {
        return documentFile;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public long getSimpleSize() {
        return simpleSize;
    }

    public void setIsDir(Boolean dir) {
        this.dir = dir;
    }

    public void setIsFile(Boolean file) {
        this.file = file;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setSimpleSize(long simpleSize) {
        this.simpleSize = simpleSize;
    }
}
