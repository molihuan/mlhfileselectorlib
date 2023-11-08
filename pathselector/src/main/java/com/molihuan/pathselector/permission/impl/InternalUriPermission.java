package com.molihuan.pathselector.permission.impl;

import android.app.Activity;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.provider.DocumentsContract;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.molihuan.pathselector.permission.BaseUriPermission;
import com.molihuan.pathselector.utils.Mtools;
import com.molihuan.pathselector.utils.UriTools;
import com.molihuan.pathselector.utils.VersionTool;

import java.util.List;
import java.util.Objects;

/**
 * @Author: molihuan
 * @Date: 2023/9/24
 * @Github: https://github.com/molihuan
 * @Description:
 * @Doc: 构建uri-判断授权情况-跳转申请
 */
public class InternalUriPermission implements BaseUriPermission {

    @Override
    public boolean goUriPermissionPage(Activity activity, Fragment fragment, Uri uri, boolean isSd, int requestCode, @Nullable PermissionRequestListener listener) {
        //必须在第一行申请权限前回调
        boolean resu = handleBeforeRequest(activity, fragment, uri, isSd, requestCode, listener);
        //是否继续
        if (!resu) {
            return false;
        }


        //获取所有已授权并存储的Uri列表，遍历并判断需要申请的uri是否在其中,在则说明已经授权了
        boolean isGet = checkUriPermission(activity, fragment, uri, isSd);
        if (isGet) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.setFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
        );
        intent.putExtra(EXTRA_SHOW_ADVANCED, true)
                .putExtra("android.content.extra.SHOW_ADVANCED", true)
                .putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);

        if (fragment != null) {
            handleFragmentSaveUriPermission(fragment, intent, listener);
            //fragment.startActivityForResult(intent, requestCode);
        } else if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            throw new IllegalArgumentException("fragment and activity cannot both be null");
        }

        return true;
    }


    @Override
    public String existsUriPermission(Activity activity, Fragment fragment, Uri uri, boolean isSd) {
        if (!VersionTool.isAndroid4()) {
            return null;
        }

        if (fragment != null) {
            activity = fragment.getActivity();
            Objects.requireNonNull(activity);
        } else if (activity == null) {
            throw new IllegalArgumentException("fragment and activity cannot both be null");
        }

        Mtools.log("请求权限的原始uri是:" + uri);
        /**
         * 请求权限的原始uri是:content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata
         * 获取需要授权uri的字符串，还不能匹配，还需要进行处理
         */

        String reqUri;
        if (isSd) {
            reqUri = uri.toString().replaceFirst(".documents/root/", ".documents/tree/");
        } else {
            reqUri = uri.toString().replaceFirst("documents/document/primary", "documents/tree/primary");
        }


        Mtools.log("请求权限处理后的uri(为了进行判断是否已经授权)是:" + reqUri);

        //获取已授权并已存储的uri列表
        List<UriPermission> uriPermissions = activity.getContentResolver().getPersistedUriPermissions();

        Mtools.log("已经授权的uri集合是:" + uriPermissions);
        //已经授权的uri集合是:[UriPermission {uri=content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata, modeFlags=3, persistedTime=1669980673302}]

        String tempUri;
        //遍历并判断请求的uri字符串是否已经被授权
        for (UriPermission uriP : uriPermissions) {
            tempUri = uriP.getUri().toString();
            //如果父目录已经授权就返回已经授权
            if (reqUri.matches(tempUri + UriTools.URI_SEPARATOR + ".*") ||
                    (reqUri.equals(tempUri) && (uriP.isReadPermission() || uriP.isWritePermission()))) {

                Mtools.log(reqUri + "已经授权");
                return tempUri;
            }
        }
        Mtools.log(reqUri + "未授权");
        return null;
    }
}
