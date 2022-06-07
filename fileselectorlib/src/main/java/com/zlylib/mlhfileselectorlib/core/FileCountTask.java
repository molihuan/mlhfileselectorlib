package com.zlylib.mlhfileselectorlib.core;

import android.os.AsyncTask;

import com.zlylib.mlhfileselectorlib.bean.FileBean;
import com.zlylib.mlhfileselectorlib.interfaces.FileCountCallBack;
import com.zlylib.mlhfileselectorlib.bean.EssFileFilter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * EssFileCountTask
 * Created by zhangliyang on 2020/6/20.
 */

public class FileCountTask extends AsyncTask<Void,Void,Void>{

    private int position;
    private String queryPath;
    private String[] types;
    private FileCountCallBack countCallBack;
    private int childFileCount = 0;
    private int childFolderCount = 0;
    private Boolean isSelectFolder = false;

    public FileCountTask(int position, String queryPath, String[] types, Boolean isSelectFolder, FileCountCallBack countCallBack) {
        this.position = position;
        this.queryPath = queryPath;
        this.types = types;
        this.isSelectFolder = isSelectFolder;
        this.countCallBack = countCallBack;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        File file = new File(queryPath);
        File[] files = file.listFiles(new EssFileFilter(types));
        if(files == null){
            return null;
        }
        List<FileBean> fileList = FileBean.getEssFileList(Arrays.asList(files),isSelectFolder);
        for (FileBean fileBean :
                fileList) {
            if(fileBean.isDirectory()){
                childFolderCount++;
            }else {
                childFileCount++;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(countCallBack!=null){
            countCallBack.onFindChildFileAndFolderCount(position,String.valueOf(childFileCount),String.valueOf(childFolderCount));
        }
    }
}
