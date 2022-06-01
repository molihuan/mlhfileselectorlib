package com.zlylib.mlhfileselectorlib.bean;

import java.io.File;
import java.io.FileFilter;

/**
 * EssFileFilter
 * 不显示隐藏文件、文件夹
 */
public class EssFileFilter implements FileFilter {
    private String[] mTypes;

    public EssFileFilter(String[] types) {
        this.mTypes = types;
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory() && !file.isHidden()) {
            return true;
        }
        if (mTypes != null && mTypes.length > 0) {
            for (String mType : mTypes) {
                if ((file.getName().endsWith(mType.toLowerCase()) || file.getName().endsWith(mType.toUpperCase())) && !file.isHidden()) {
//                if (FileUtils.getMimeType(file.getAbsolutePath()).equalsIgnoreCase(MimeTypeMap.getSingleton().getMimeTypeFromExtension(mType))) {
                    return true;
                }
            }
        }else {
            if (file.isHidden()) {
                return false;
            }else{
                return true;
            }
        }
        return false;
    }
}
