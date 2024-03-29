package com.molihuan.pathselector.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import com.blankj.molihuan.utilcode.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UriTools
 * @Author: molihuan
 * @Date: 2022/12/09/12:31
 * @Description:
 */
public class UriTools {
    //(/storage/emulated/0/Android/data/)存储后的uri
    @Deprecated
    public static final String URI_STORAGE_SAVED_ANRROID_DATA = "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata";
    //(/storage/emulated/0/Android/obb/)存储后的uri
    @Deprecated
    public static final String URI_STORAGE_SAVED_ANRROID_OBB = "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fobb";
    //获取(/storage/emulated/0/Android/data/)目录权限跳转的uri
    @Deprecated
    public static final String URI_STORAGE_JUMP_ANRROID_DATA = "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata";
    //获取(/storage/emulated/0/Android/obb/)目录权限跳转的uri
    @Deprecated
    public static final String URI_STORAGE_JUMP_ANRROID_OBB = "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fobb";

    //uri请求权限构建前缀
    public static final String URI_PERMISSION_REQUEST_PREFIX = "com.android.externalstorage.documents";
    //uri请求权限构建完整前缀
    public static final String URI_PERMISSION_REQUEST_COMPLETE_PREFIX = "content://com.android.externalstorage.documents";
    //uri请求权限构建后缀主要特殊符号
    public static final String URI_PERMISSION_REQUEST_SUFFIX_SPECIAL_SYMBOL = "primary:";
    //uri路径分割符
    public static final String URI_SEPARATOR = "%2F";

    public static boolean exists(String uriPath, Activity activity, Fragment fragment) {
        if (fragment != null) {
            activity = fragment.getActivity();
        } else if (activity == null) {
            throw new NullPointerException("fragment and activity cannot both be null");
        }

        Uri uri = UriTools.path2Uri(uriPath, false);
        String existsPermission = PermissionsTools.existsGrantedUriPermission(uri, false, activity);
        Uri targetUri = Uri.parse(existsPermission + uri.toString().replaceFirst(UriTools.URI_PERMISSION_REQUEST_COMPLETE_PREFIX, ""));

        DocumentFile rootDocumentFile = DocumentFile.fromSingleUri(activity, targetUri);
        return rootDocumentFile == null ? false : rootDocumentFile.exists();
    }

    /**
     * 获取Android/Data下的软件包名
     *
     * @param context
     * @return
     */
    public static List<String> getAndroidDataPackageNames(Context context) {
        // 得到PackageManager对象
        PackageManager pm = context.getPackageManager();

        List<String> packageNameList = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);

        String packageName;
        for (ResolveInfo resolveInfo : resolveInfos) {
            packageName = resolveInfo.activityInfo.packageName;
            if (FileUtils.isFileExists(MConstants.PATH_ANRROID_DATA + File.separator + packageName)) {
                packageNameList.add(packageName);
            }
        }

        return packageNameList;
    }

    /**
     * path转uri
     *
     * @param path /storage/emulated/0/Android/data/moli/m3d/m5.txt
     * @param tree false
     * @return content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2Fmoli%2Fm3d%2Fm5.txt
     */
    public static Uri path2Uri(String path, boolean tree) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return null;
        }
        /**
         * DocumentsContract.buildTreeDocumentUri():
         * content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata
         * DocumentsContract.buildDocumentUri():
         * content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata
         */

        String uriSuf = URI_PERMISSION_REQUEST_SUFFIX_SPECIAL_SYMBOL + path.replaceFirst(MConstants.DEFAULT_ROOTPATH + File.separator, "");
        Uri uri;
        if (tree) {
            uri = DocumentsContract.buildTreeDocumentUri(URI_PERMISSION_REQUEST_PREFIX, uriSuf);
        } else {
            uri = DocumentsContract.buildDocumentUri(URI_PERMISSION_REQUEST_PREFIX, uriSuf);
        }
        return uri;
    }

    /**
     * 构建sd卡根目录的uri
     *
     * @param sdName 名称(如0BFD-0C17)
     * @return (content : / / com.android.externalstorage.documents / root / 0BFD - 0C17)
     */
    public static @Nullable Uri buildSdRootUri(String sdName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return null;
        }
        return DocumentsContract.buildRootUri(UriTools.URI_PERMISSION_REQUEST_PREFIX, sdName);
    }

}
