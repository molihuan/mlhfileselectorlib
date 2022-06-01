package com.z.fileselectorlib.Objects;

import com.z.fileselectorlib.Utils.FileUtil;

public class FileInfo {
    public enum FileType{Folder,Video,Audio,Image,Text,Unknown,Parent}
    private String FileName;
    private long FileCount;//如果是文件夹则表示子目录项数,如果不是文件夹则表示文件大小，-1不显示
    private String FileLastUpdateTime;
    private String FilePath;
    private FileType fileType;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileCount() {
        if (fileType== FileType.Parent)
            return "";
        else if (FileCount == -1 && fileType== FileType.Folder)
            return "文件夹不可访问";
        else if (fileType== FileType.Folder)
            return "共"+ FileCount +"项";
        else {
            return FileUtil.getFileSize(FileCount);
        }
    }

    public void setFileCount(long fileCount) {
        FileCount = fileCount;
    }

    public String getFileLastUpdateTime() {
        return FileLastUpdateTime;
    }

    public void setFileLastUpdateTime(String fileLastUpdateTime) {
        FileLastUpdateTime = fileLastUpdateTime;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public boolean FileFilter(FileType[] types){
        for (FileType type:types) {
            if (this.fileType==type)return true;
        }
        return false;
    }
}
