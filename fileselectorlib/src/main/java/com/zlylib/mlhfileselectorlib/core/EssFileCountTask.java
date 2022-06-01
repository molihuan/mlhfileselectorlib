package com.zlylib.mlhfileselectorlib.core;

import android.os.AsyncTask;

import com.zlylib.mlhfileselectorlib.bean.EssFile;
import com.zlylib.mlhfileselectorlib.bean.EssFileCountCallBack;
import com.zlylib.mlhfileselectorlib.bean.EssFileFilter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * EssFileCountTask
 * Created by zhangliyang on 2020/6/20.
 */

public class EssFileCountTask extends AsyncTask<Void,Void,Void>{

    private int position;
    private String queryPath;
    private String[] types;
    private EssFileCountCallBack countCallBack;
    private int childFileCount = 0;
    private int childFolderCount = 0;
    private Boolean isSelectFolder = false;

    public EssFileCountTask(int position, String queryPath, String[] types, Boolean isSelectFolder, EssFileCountCallBack countCallBack) {
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
        List<EssFile> fileList = EssFile.getEssFileList(Arrays.asList(files),isSelectFolder);
        for (EssFile essFile :
                fileList) {
            if(essFile.isDirectory()){
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
