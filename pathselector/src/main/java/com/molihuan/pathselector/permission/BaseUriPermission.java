package com.molihuan.pathselector.permission;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.molihuan.pathselector.utils.VersionTool;

/**
 * @Author: molihuan
 * @Date: 2023/9/24
 * @Github: https://github.com/molihuan
 * @Description:
 * @Doc:
 */


public interface BaseUriPermission {
    String EXTERNAL_STORAGE_PROVIDER_AUTHORITY = "com.android.externalstorage.documents";
    String EXTRA_SHOW_ADVANCED = "android.provider.extra.SHOW_ADVANCED";

    int SAVE_URI_PERMISSION_REQUEST_CODE = 54111;
    int SAVE_SD_URI_PERMISSION_REQUEST_CODE = 54112;

    enum BuildFullUriType {
        ROOTS,
        ROOT,
        RECENT_DOCUMENTS,
        TREE_DOCUMENT,
        DOCUMENT
    }

    /**
     * 构建完整可直接跳转的uri
     *
     * @param uriSuf           如果uriSuf = primary:Android/data
     *                         对应:/storage/emulated/0/Android/data/
     * @param buildFullUriType
     * @return
     */
    default @Nullable Uri buildFullUri(String uriSuf, BuildFullUriType buildFullUriType) {
        if (!VersionTool.isAndroid5()) {
            return null;
        }
        Uri uri = null;
        switch (buildFullUriType) {
            case ROOTS:
                uri = DocumentsContract.buildRootsUri(EXTERNAL_STORAGE_PROVIDER_AUTHORITY);
                break;
            case ROOT:
                /**
                 * 如果uriSuf = 0BFD-0C17
                 * 则uri = content://com.android.externalstorage.documents/root/0BFD-0C17
                 */
                uri = DocumentsContract.buildRootUri(EXTERNAL_STORAGE_PROVIDER_AUTHORITY, uriSuf);
                break;
            case RECENT_DOCUMENTS:
                uri = DocumentsContract.buildRecentDocumentsUri(EXTERNAL_STORAGE_PROVIDER_AUTHORITY, uriSuf);
                break;
            case TREE_DOCUMENT:
                /**如果uriSuf = primary:Android/data则
                 * uri = content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata
                 */
                uri = DocumentsContract.buildTreeDocumentUri(EXTERNAL_STORAGE_PROVIDER_AUTHORITY, uriSuf);
                break;
            case DOCUMENT:
                /**如果uriSuf = primary:Android/data则
                 * uri = content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata
                 */
                uri = DocumentsContract.buildDocumentUri(EXTERNAL_STORAGE_PROVIDER_AUTHORITY, uriSuf);
                break;
            default:
                throw new IllegalArgumentException("buildFullUriType argument error");
        }
        return uri;
    }


    default void goUriPermissionPage(Activity activity, Uri uri, boolean isSd, int requestCode, @Nullable PermissionRequestListener listener) {
        goUriPermissionPage(activity, null, uri, isSd, requestCode, listener);
    }

    default void goUriPermissionPage(Fragment fragment, Uri uri, boolean isSd, int requestCode, @Nullable PermissionRequestListener listener) {
        goUriPermissionPage(null, fragment, uri, isSd, requestCode, listener);
    }

    /**
     * 去SAF授权页面
     *
     * @param activity
     * @param fragment
     * @param uri
     * @param isSd
     * @param requestCode
     * @param listener
     * @return 并不能代表成功或失败, true代表使用了框架的uri权限请求跳转，false代表没有使用
     */
    default boolean goUriPermissionPage(Activity activity, Fragment fragment, Uri uri, boolean isSd, int requestCode, @Nullable PermissionRequestListener listener) {
        if (listener != null) {
            listener.beforeRequest(activity, fragment, uri, isSd, requestCode);
        }

        Uri saveUri = null;
        if (listener != null) {
            listener.afterRequest(saveUri, false);
        }
        return true;
    }

    default boolean checkUriPermission(Activity activity, Uri uri, boolean isSd) {
        return checkUriPermission(activity, null, uri, isSd);
    }

    default boolean checkUriPermission(Fragment fragment, Uri uri, boolean isSd) {
        return checkUriPermission(null, fragment, uri, isSd);
    }

    /**
     * uri权限是否存在
     *
     * @param activity
     * @param fragment
     * @param uri
     * @param isSd
     * @return
     */
    default boolean checkUriPermission(Activity activity, Fragment fragment, Uri uri, boolean isSd) {
        return existsUriPermission(activity, fragment, uri, isSd) != null;
    }

    /**
     * 是否存在uri权限，存在则返回保存的uri字符串
     *
     * @param activity
     * @param uri
     * @param isSd     是否是sd卡
     * @return
     */
    default String existsUriPermission(Activity activity, Uri uri, boolean isSd) {
        return existsUriPermission(activity, null, uri, isSd);
    }

    default String existsUriPermission(Fragment fragment, Uri uri, boolean isSd) {
        return existsUriPermission(null, fragment, uri, isSd);
    }

    /**
     * 是否存在权限
     *
     * @param activity
     * @param fragment
     * @param uri
     * @param isSd     是否是sd卡
     * @return 已经授权则返回已授权并已存储的uri字符串, 未授权返回null
     */
    String existsUriPermission(Activity activity, Fragment fragment, Uri uri, boolean isSd);

    /**
     * 处理权限请求前监听回调
     *
     * @param uri
     * @param listener
     */
    default boolean handleBeforeRequest(Activity activity, Fragment fragment, Uri uri, boolean isSd, int requestCode, @Nullable PermissionRequestListener listener) {
        if (listener != null) {
            return listener.beforeRequest(activity, fragment, uri, isSd, requestCode);
        }
        return true;
    }

    /**
     * 处理权限请求后监听回调
     *
     * @param uri
     * @param result
     * @param listener
     */
    default void handleAfterRequest(Uri uri, boolean result, @Nullable PermissionRequestListener listener) {
        if (listener != null) {
            listener.afterRequest(uri, result);
        }
    }

    /**
     * 保存uri权限
     */
    default void handleActivitySaveUriPermission(Activity activity, @Nullable Intent data, int requestCode, boolean isSd, @Nullable PermissionRequestListener listener) {
        if (!VersionTool.isAndroid4()) {
            handleAfterRequest(null, false, listener);
            return;
        }

        if (isSd) {
            if (requestCode != SAVE_SD_URI_PERMISSION_REQUEST_CODE) {
                handleAfterRequest(null, false, listener);
                return;
            }

            handleActivitySaveUriPermission(activity, data, listener);
            return;
        }

        if (requestCode != SAVE_URI_PERMISSION_REQUEST_CODE) {
            handleAfterRequest(null, false, listener);
            return;
        }
        handleActivitySaveUriPermission(activity, data, listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    default void handleActivitySaveUriPermission(Activity activity, @Nullable Intent data, @Nullable PermissionRequestListener listener) {

        // 处理结果数据
        if (data == null) {
            handleAfterRequest(null, false, listener);
            return;
        }
        //需要保存的uri
        Uri uri = data.getData();
        if (uri == null) {
            handleAfterRequest(null, false, listener);
            return;
        }

        if (activity == null) {
            handleAfterRequest(null, false, listener);
            return;
        }
        //存储权限
        activity.getContentResolver()
                .takePersistableUriPermission(uri,
                        (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                );

        handleAfterRequest(uri, true, listener);
    }

    /**
     * 保存uri权限
     *
     * @param fragment
     * @param intent
     * @param listener
     */
    default void handleFragmentSaveUriPermission(Fragment fragment, Intent intent, @Nullable PermissionRequestListener listener) {
        fragment.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (!VersionTool.isAndroid4()) {
                    handleAfterRequest(null, false, listener);
                    return;
                }
                if (result.getResultCode() != Activity.RESULT_OK) {
                    handleAfterRequest(null, false, listener);
                    return;
                }

                // 处理结果数据
                Intent data = result.getData();
                if (data == null) {
                    handleAfterRequest(null, false, listener);
                    return;
                }
                //需要保存的uri
                Uri uri = data.getData();
                if (uri == null) {
                    handleAfterRequest(null, false, listener);
                    return;
                }
                Activity activity = fragment.getActivity();
                if (activity == null) {
                    handleAfterRequest(null, false, listener);
                    return;
                }
                //存储权限
                activity.getContentResolver()
                        .takePersistableUriPermission(uri,
                                (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        );

                handleAfterRequest(uri, true, listener);

            }
        }).launch(intent);
    }

    /**
     * 权限请求监听器
     */
    interface PermissionRequestListener {
        /**
         * @param activity
         * @param fragment
         * @param uri         需要请求权限的uri(完整)
         * @param requestCode 请求码
         * @return 是否需要框架来请求权限 需要返回true 不需要返回false
         */
        boolean beforeRequest(Activity activity, Fragment fragment, Uri uri, boolean isSd, int requestCode);

        /**
         * @param saveUri 保存的uri
         * @param result  是否保存成功
         */
        void afterRequest(Uri saveUri, boolean result);
    }
}
