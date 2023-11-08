package com.molihuan.pathselector.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.blankj.molihuan.utilcode.util.LogUtils;
import com.blankj.molihuan.utilcode.util.ToastUtils;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.dialog.BaseDialog;
import com.molihuan.pathselector.dialog.impl.MessageDialog;
import com.molihuan.pathselector.entity.FontBean;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName PermissionsTools
 * @Description 权限工具
 * @Author molihuan
 * @Date 2022/12/2 15:59
 */
public class PermissionsTools {


    public static final int PERMISSION_REQUEST_CODE = 54111;

    public static final int SDCARD_URI_PERMISSION_REQUEST_CODE = 54112;

    //默认的uri构建后缀
    public static final int DEFAULT_URI_BUILD_SUFFIX_ANDROID_DATA = 156;
    public static final int DEFAULT_URI_BUILD_SUFFIX_ANDROID_OBB = 157;

    public OnPermissionCallback specialPermissionCallback;
    public OnPermissionCallback generalPermissionCallback;

    /**
     * 获取存储权限
     *
     * @param context
     * @param specialPermissionCallback
     * @param generalPermissionCallback
     */
    public static void getStoragePermissions(Context context, OnPermissionCallback specialPermissionCallback, OnPermissionCallback generalPermissionCallback) {

        //所有文件访问权限Android 11 +
        specialPermissionsOfStorage(context, specialPermissionCallback);

        //普通存储访问权限Android 11 -
        generalPermissionsOfStorage(context, generalPermissionCallback);

    }

    /**
     * 带弹窗的全文件读写权限申请
     *
     * @param context
     * @param useDialog                 是否使用弹窗
     * @param specialPermissionCallback 权限申请结果回调
     */
    public static void specialPermissionsOfStorageWithDialog(Context context, boolean useDialog, OnPermissionCallback specialPermissionCallback) {
        if (useDialog) {

            if (!VersionTool.isAndroid11()) {
                return;
            }

            boolean isGet = XXPermissions.isGranted(context, Permission.MANAGE_EXTERNAL_STORAGE);
            //已有权限则返回
            if (isGet) {
                Mtools.log("全文件读取权限------已获取");
                return;
            }

            new MessageDialog(context)
                    .setContent(new FontBean(context.getString(R.string.tip_dialog_get_special_permissions_mlh)))
                    .setConfirm(new FontBean(context.getString(R.string.option_confirm_mlh)), new BaseDialog.IOnConfirmListener() {
                        @Override
                        public boolean onClick(View v, BaseDialog dialog) {
                            PermissionsTools.specialPermissionsOfStorageNoCheck(context, specialPermissionCallback);
                            dialog.dismiss();
                            return false;
                        }
                    })
                    .setCancel(new FontBean(context.getString(R.string.option_cancel_mlh)), new BaseDialog.IOnCancelListener() {
                        @Override
                        public boolean onClick(View v, BaseDialog dialog) {
                            dialog.dismiss();
                            return false;
                        }
                    })
                    .show();

        } else {
            specialPermissionsOfStorage(context, specialPermissionCallback);
        }
    }


    /**
     * 获取一般读写权限
     * Android 11 -
     *
     * @param context
     * @param generalPermissionCallback 权限申请结果回调
     */
    public static void generalPermissionsOfStorage(Context context, OnPermissionCallback generalPermissionCallback) {

        boolean isGet = XXPermissions.isGranted(context, Permission.Group.STORAGE);
        //已有权限则返回
        if (isGet) {
            Mtools.log("一般存储权限------已获取");
            return;
        }
        XXPermissions.with(context)
                // 申请多个权限
                .permission(Permission.Group.STORAGE)
                // 设置不触发错误检测机制（局部设置）
                .unchecked()
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        Mtools.log("一般存储权限------获取成功");
                        generalPermissionCallback.onGranted(permissions, all);
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
//                            ToastUtils.make().show(R.string.tip_denial_authorization_mlh);
                            Mtools.toast("general" + context.getString(R.string.tip_denial_authorization_mlh));
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            //XXPermissions.startPermissionActivity(context, permissions);
                        } else {
                            ToastUtils.make().show(R.string.tip_permission_failed_mlh);
                        }

                        generalPermissionCallback.onDenied(permissions, never);

                    }
                });
    }


    /**
     * 获取全文件读取权限
     * Android 11 +
     *
     * @param context
     * @param specialPermissionCallback 权限申请结果回调
     */
    public static void specialPermissionsOfStorage(Context context, OnPermissionCallback specialPermissionCallback) {
        if (!VersionTool.isAndroid11()) {
            return;
        }

        boolean isGet = XXPermissions.isGranted(context, Permission.MANAGE_EXTERNAL_STORAGE);
        //已有权限则返回
        if (isGet) {
            Mtools.log("全文件读取权限------已获取");
            return;
        }

        specialPermissionsOfStorageNoCheck(context, specialPermissionCallback);

    }

    public static void specialPermissionsOfStorageNoCheck(Context context, OnPermissionCallback specialPermissionCallback) {
        XXPermissions.with(context)
                // 申请单个权限
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                // 设置不触发错误检测机制（局部设置）
                .unchecked()
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        Mtools.log("全文件读取权限------获取成功");
                        specialPermissionCallback.onGranted(permissions, all);
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
//                            ToastUtils.make().show(R.string.tip_denial_authorization_mlh);
                            Mtools.toast("special" + context.getString(R.string.tip_denial_authorization_mlh));
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(context, permissions);
                        } else {
                            ToastUtils.make().show(R.string.tip_permission_failed_mlh);
                        }

                        specialPermissionCallback.onDenied(permissions, never);
                    }
                });
    }

    /**
     * 判断是否已经获取uri权限
     *
     * @param uri
     * @param activity
     * @param fragment
     * @return
     */

    private static boolean isGrantedUriPermission(Uri uri, boolean isSd, Activity activity, Fragment fragment) {
        return existsGrantedUriPermission(uri, isSd, activity, fragment) != null;
    }


    public static String existsGrantedUriPermission(Uri uri, boolean isSd, Fragment fragment) {
        return existsGrantedUriPermission(uri, isSd, null, fragment);
    }


    public static String existsGrantedUriPermission(Uri uri, boolean isSd, Activity activity) {
        return existsGrantedUriPermission(uri, isSd, activity, null);
    }


    public static String existsGrantedUriPermission(Uri uri, boolean isSd, Activity activity, Fragment fragment) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return null;
        }

        if (fragment != null) {
            activity = fragment.getActivity();
        } else if (activity == null) {
            throw new IllegalArgumentException("fragment and activity cannot both be null");
        }
        Mtools.log("请求权限的原始uri是:" + uri);
        //请求权限的原始uri是:content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata
        //获取需要授权uri的字符串，还不能匹配，还需要进行处理
        String reqUri;
        if (isSd) {
            reqUri = uri.toString().replaceFirst(".documents/root/", ".documents/tree/");
        } else {
            reqUri = uri.toString().replace("documents/document/primary", "documents/tree/primary");
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
            if (reqUri.matches(tempUri + UriTools.URI_SEPARATOR + ".*") || (reqUri.equals(tempUri) && (uriP.isReadPermission() || uriP.isWritePermission()))) {
                Mtools.log(reqUri + "已经授权");
                return tempUri;
            }
        }
        Mtools.log(reqUri + "未授权");
        return null;
    }

    //********************       通过默认请求权限uri后缀构建完整的uri再进行跳转(不推荐)       ***************************
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void applyUriPermissionByDefault(int defaultUriBuildSuf, int requestCode, Activity activity) throws Exception {
        applyUriPermissionByDefault(defaultUriBuildSuf, requestCode, activity, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void applyUriPermissionByDefault(int defaultUriBuildSuf, int requestCode, Fragment fragment) throws Exception {
        applyUriPermissionByDefault(defaultUriBuildSuf, requestCode, null, fragment);
    }

    /**
     * (不推荐)
     * 提供两个默认的uri请求跳转(Android13无效，请使用其他方法)
     * 分别是:Android/data目录和Android/obb目录
     * 注意：Android13无法获得Android/data目录的读写权限，只能获得子目录的读写权限。使用另一种重载方法来获得Android/data子目录的读写权限
     *
     * @param defaultUriBuildSuf
     * @param activity
     * @param fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void applyUriPermissionByDefault(int defaultUriBuildSuf, int requestCode, Activity activity, Fragment fragment) throws Exception {
        if (VersionTool.isAndroid13()) {
            throw new Exception("Android13 cannot get read and write permissions for the Android/data or obb directory and can only get read and write permissions for subdirectories. Use another reload method to obtain read and write permissions for the Android/data or obb subdirectory");
        }
        String uriSuf;
        switch (defaultUriBuildSuf) {
            case DEFAULT_URI_BUILD_SUFFIX_ANDROID_DATA:
                uriSuf = UriTools.URI_PERMISSION_REQUEST_SUFFIX_SPECIAL_SYMBOL + "Android/data";
                break;
            case DEFAULT_URI_BUILD_SUFFIX_ANDROID_OBB:
                uriSuf = UriTools.URI_PERMISSION_REQUEST_SUFFIX_SPECIAL_SYMBOL + "Android/obb";
                break;
            default:
                throw new IllegalArgumentException("Parameter does not conform to a predefined value");
        }

        if (fragment != null) {
            goApplyUriPermissionPage(uriSuf, false, requestCode, fragment);
        } else if (activity != null) {
            goApplyUriPermissionPage(uriSuf, false, requestCode, activity);
        } else {
            throw new NullPointerException("fragment and activity cannot both be null");
        }
    }

    /**
     * 推荐使用此方法来获取权限(推荐使用)
     * 申请权限的路径:/storage/emulated/0/Android/data/bin.mt.plus
     * 跳转SAF授权页面的uri:content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2Fbin.mt.plus
     * 存储授权信息:UriPermission {uri=content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2Fbin.mt.plus, modeFlags=3, persistedTime=1670643363607}
     * 判断是uri是否已经授权只需要修改"跳转SAF授权页面的uri"为存储的uri并遍历已经存储的uri进行比较,注意判断权限
     * 如果/storage/emulated/0/Android/data/bin.mt.plus已经申请并存储了权限则其子目录和孙子目录无需再授权，
     * 如果需要操作/storage/emulated/0/Android/data/bin.mt.plus则构造uri:
     * content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2Fbin.mt.plus/document/primary%3AAndroid%2Fdata%2Fbin.mt.plus
     * 并将这个uri构造为DocumentFile对象即可操作
     * 如果需要操作/storage/emulated/0/Android/data/bin.mt.plus/cache则构造uri:
     * content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2Fbin.mt.plus/document/primary%3AAndroid%2Fdata%2Fbin.mt.plus%2Fcache
     * 并将这个uri构造为DocumentFile对象即可操作
     *
     * @param uriSuf
     * @param tree
     * @param activity
     */
    //********************       通过自定义请求权限uri后缀构建完整的uri再进行跳转       ***************************
    public static void goApplyUriPermissionPage(String uriSuf, boolean isSd, boolean tree, int requestCode, Activity activity) {
        goApplyUriPermissionPage(uriSuf, isSd, tree, requestCode, activity, null);
    }


    public static void goApplyUriPermissionPage(String uriSuf, boolean isSd, boolean tree, int requestCode, Fragment fragment) {
        goApplyUriPermissionPage(uriSuf, isSd, tree, requestCode, null, fragment);
    }

    /**
     * (推荐使用)
     *
     * @param uriSuf   请求目录权限：/storage/emulated/0/Android/data/
     *                 uriSuf = "primary:Android/data";
     *                 请求目录权限：/storage/emulated/0/Android/obb/
     *                 uriSuf = "primary:Android/obb";
     * @param tree
     * @param activity
     * @param fragment
     */

    private static void goApplyUriPermissionPage(String uriSuf, boolean isSd, boolean tree, int requestCode, Activity activity, Fragment fragment) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        /**
         * DocumentsContract.buildTreeDocumentUri():
         * content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata
         * DocumentsContract.buildDocumentUri():
         * content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata
         * buildFullUri
         */
        Uri uri;
        if (tree) {
            uri = DocumentsContract.buildTreeDocumentUri(UriTools.URI_PERMISSION_REQUEST_PREFIX, uriSuf);
        } else {
            uri = DocumentsContract.buildDocumentUri(UriTools.URI_PERMISSION_REQUEST_PREFIX, uriSuf);
        }


        if (fragment != null) {
            goApplyUriPermissionPage(uri, isSd, requestCode, fragment);
        } else if (activity != null) {
            goApplyUriPermissionPage(uri, isSd, requestCode, activity);
        } else {
            throw new NullPointerException("fragment and activity cannot both be null");
        }
    }

    //********************       通过完整的权限请求uri进行跳转(推荐使用)       ***************************
    private static void goApplyUriPermissionPage(String completeUri, boolean isSd, int requestCode, Fragment fragment) {
        Uri uri = Uri.parse(completeUri);
        goApplyUriPermissionPage(uri, isSd, requestCode, null, fragment);
    }

    public static void goApplyUriPermissionPage(Uri uri, boolean isSd, int requestCode, Fragment fragment) {
        goApplyUriPermissionPage(uri, isSd, requestCode, null, fragment);
    }

    public static void goApplyUriPermissionPage(Uri uri, Fragment fragment) {
        goApplyUriPermissionPage(uri, false, PERMISSION_REQUEST_CODE, null, fragment);
    }

    /**
     * 跳转请求权限SAF页面(推荐使用)
     *
     * @param completeUri 完整的请求Uri字符串
     * @param activity
     */
    private static void goApplyUriPermissionPage(String completeUri, boolean isSd, int requestCode, Activity activity) {
        Uri uri = Uri.parse(completeUri);
        goApplyUriPermissionPage(uri, isSd, requestCode, activity, null);
    }

    public static void goApplyUriPermissionPage(Uri uri, boolean isSd, int requestCode, Activity activity) {
        goApplyUriPermissionPage(uri, isSd, requestCode, activity, null);
    }

    public static void goApplyUriPermissionPage(Uri uri, Activity activity) {
        goApplyUriPermissionPage(uri, false, PERMISSION_REQUEST_CODE, activity, null);
    }

    /**
     * 跳转请求权限SAF页面(推荐使用)
     * 注意Android 13对 Android/data目录进行了更加严格的限制已经无法获取其权限了
     * 但是可以获取其子目录权限，我们可以对其子目录进行权限申请，从而达到操作Android/data目录的目的
     * 其子目录可以通过Android/data+本机安装软件包名来获得
     * 如获取谷歌浏览器包名:com.android.chrome
     * 拼接:Android/data/com.android.chrome
     * 我们只要申请这个目录的uri权限即可操作这个目录
     * 最终跳转的uir字符串为:content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2Fcom.android.chrome
     * 存储的uir字符串为:content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2Fcom.android.chrome
     * <p>
     * 如果你需要在Activity的onActivityResult(int requestCode, int resultCode, Intent data)中保存则使用带有Activity的方法
     * fragment反之
     *
     * @param uri      完整的请求Uri
     * @param activity
     * @param fragment
     */
    private static void goApplyUriPermissionPage(Uri uri, boolean isSd, int requestCode, Activity activity, Fragment fragment) {
//        if (!VersionTool.isAndroid11()) {
//            return;
//        }

        //获取所有已授权并存储的Uri列表，遍历并判断需要申请的uri是否在其中,在则说明已经授权了
        boolean isGet = isGrantedUriPermission(uri, isSd, activity, fragment);//这里会对activity重新赋值
        if (isGet) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.setFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
        );
        intent.putExtra("android.provider.extra.SHOW_ADVANCED", true)
                .putExtra("android.content.extra.SHOW_ADVANCED", true)
                .putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);

        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 获取SD卡权限
     *
     * @param activity
     * @param sdName      SD卡名称
     *                    可以通过{@link com.molihuan.pathselector.utils.ReflectTools#getStorageVolumes(Context)}获取StorageVolume列表再通过StorageVolume获取需要的内存卡名称
     * @param requestCode
     */
    public static void applyUriPermissionPageWithSDcard(Activity activity, Fragment fragment, @NonNull String sdName, int requestCode) {

        Uri uri = UriTools.buildSdRootUri(sdName);
        //获取所有已授权并存储的Uri列表，遍历并判断需要申请的uri是否在其中,在则说明已经授权了
        boolean isGet = isGrantedUriPermission(uri, true, activity, fragment);//这里会对activity重新赋值
        if (isGet) {
            return;
        }

        if (activity != null) {
            PermissionsTools.goApplyUriPermissionPage(uri, true, requestCode, activity);
        } else if (fragment != null) {
            PermissionsTools.goApplyUriPermissionPage(uri, true, requestCode, fragment);
        } else {
            throw new IllegalArgumentException("fragment and activity cannot both be null");
        }
    }

    public static void applyUriPermissionPageWithSDcard(Activity activity, @NonNull String sdName, int requestCode) {
        applyUriPermissionPageWithSDcard(activity, null, sdName, requestCode);
    }

    public static void applyUriPermissionPageWithSDcard(Fragment fragment, @NonNull String sdName, int requestCode) {
        applyUriPermissionPageWithSDcard(null, fragment, sdName, requestCode);
    }

    /**
     * 获取SD卡权限(请使用{@link com.molihuan.pathselector.utils.PermissionsTools#applyUriPermissionPageWithSDcard(Activity, Fragment, String, int)})
     *
     * @param activity
     * @param sdRootPath  sd卡的路径.可以通过{@link com.molihuan.pathselector.utils.ReflectTools#getStorageVolumes(Context)}获取StorageVolume列表再通过StorageVolume的getDirectory获取文件再通过文件获取路径
     *                    获取
     * @param requestCode 请求码
     */
    @Deprecated
    public static void goApplyUriPermissionPageWithSDcard(Activity activity, @NonNull String sdRootPath, int requestCode) {
        goApplyUriPermissionPageWithSDcard(activity, null, sdRootPath, requestCode);
    }

    @Deprecated
    public static void goApplyUriPermissionPageWithSDcard(Fragment fragment, @NonNull String sdRootPath, int requestCode) {
        goApplyUriPermissionPageWithSDcard(null, fragment, sdRootPath, requestCode);
    }

    @Deprecated
    public static void goApplyUriPermissionPageWithSDcard(Activity activity, @NonNull String sdRootPath) {
        goApplyUriPermissionPageWithSDcard(activity, null, sdRootPath, SDCARD_URI_PERMISSION_REQUEST_CODE);
    }

    @Deprecated
    public static void goApplyUriPermissionPageWithSDcard(Fragment fragment, @NonNull String sdRootPath) {
        goApplyUriPermissionPageWithSDcard(null, fragment, sdRootPath, SDCARD_URI_PERMISSION_REQUEST_CODE);
    }

    /**
     * @param activity
     * @param sdStorageVolume sd卡的StorageVolume,所有的StorageVolume可以通过{@link com.molihuan.pathselector.utils.ReflectTools#getStorageVolumes(Context)}获取
     * @param requestCode
     */
    @Deprecated
    public static void goApplyUriPermissionPageWithSDcard(Activity activity, @NonNull StorageVolume sdStorageVolume, int requestCode) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R) {
            return;
        }
        File volumeDirectory = sdStorageVolume.getDirectory();
        if (volumeDirectory == null) {
            return;
        }
        goApplyUriPermissionPageWithSDcard(activity, null, volumeDirectory.getAbsolutePath(), requestCode);
    }

    @Deprecated
    public static void goApplyUriPermissionPageWithSDcard(Fragment fragment, @NonNull StorageVolume sdStorageVolume, int requestCode) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R) {
            return;
        }
        File volumeDirectory = sdStorageVolume.getDirectory();
        if (volumeDirectory == null) {
            return;
        }

        goApplyUriPermissionPageWithSDcard(null, fragment, volumeDirectory.getAbsolutePath(), requestCode);
    }

    @Deprecated
    public static void goApplyUriPermissionPageWithSDcard(Activity activity, @NonNull StorageVolume sdStorageVolume) {

        goApplyUriPermissionPageWithSDcard(activity, sdStorageVolume, SDCARD_URI_PERMISSION_REQUEST_CODE);
    }

    @Deprecated
    public static void goApplyUriPermissionPageWithSDcard(Fragment fragment, @NonNull StorageVolume sdStorageVolume) {

        goApplyUriPermissionPageWithSDcard(fragment, sdStorageVolume, SDCARD_URI_PERMISSION_REQUEST_CODE);
    }

    /**
     * TODO 设置钩子函数在申请权限之前调用
     * 获取SD卡权限(只支持安卓10+)
     *
     * @param activity    上下文对象
     * @param fragment    上下文对象
     * @param sdRootPath  sd卡的路径.可以通过{@link ReflectTools#getStorageVolumes(Context)}获取StorageVolume列表再通过StorageVolume的getDirectory获取文件再通过文件获取路径
     *                    获取
     * @param requestCode 请求码
     */
    @Deprecated
    private static void goApplyUriPermissionPageWithSDcard(Activity activity, Fragment fragment, @NonNull String sdRootPath, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return;
        }

        Context context;
        if (fragment != null) {
            context = fragment.getContext();
        } else if (activity != null) {
            context = activity;
        } else {
            throw new NullPointerException("fragment and activity cannot both be null");
        }

        Objects.requireNonNull(context);

        List<StorageVolume> volume = ReflectTools.getStorageVolumes(context);

        for (int i = 0; i < volume.size(); i++) {
            StorageVolume storageVolume = volume.get(i);
            File volumeDirectory = storageVolume.getDirectory();
            if (volumeDirectory == null) {
                return;
            }

            String rootPath = volumeDirectory.getAbsolutePath();
            //判断是否为申请权限的路径
            if (sdRootPath.equals(rootPath)) {
                Intent intent = storageVolume.createOpenDocumentTreeIntent();
//                Bundle extras = intent.getExtras();
//                content://com.android.externalstorage.documents/root/0BFD-0C17
//                Uri parcelable = (Uri) extras.get(DocumentsContract.EXTRA_INITIAL_URI);
//                Mtools.log(parcelable.toString());

                if (fragment != null) {
                    fragment.startActivityForResult(intent, requestCode);
                } else {
                    activity.startActivityForResult(intent, requestCode);
                }
            }
        }

    }


    /**
     * TODO 这里好像解释有问题，大家自己辨别
     * 注意!!!!!:
     * 这是旧版本不兼容安卓13，请用{@link #goApplyUriPermissionPage(Uri, boolean, int Activity, Fragment)}替代
     * <p>
     * (注意)requestUri必须和savedUri配套使用，详情请看下面的解释
     *
     * @param requestUri 需要获取权限的uri字符串 如：content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata
     * @param savedUri   进行判断是否已经获取权限的uri字符串 如：content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata
     *                   更多请参考{@link com.molihuan.pathselector.utils.UriTools}下面的uri系列
     * @param activity   可以获取进行跳转的都可以其实为了调用:startActivityForResult进行跳转
     * @Author molihuan
     * <p>
     * 如果你想获取/storage/emulated/0/Android/data/路径下面的权限
     * 则requestUri为content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata
     * 怎么跳转到授权界面可以参考本类中的getAndroidUriPermission方法。传送门: {@link #getAndroidUriPermission(String, String, Activity)}
     * 使用(上下文) content.getContentResolver()获取解释器并调用takePersistableUriPermission(Uri uri,int modeFlags)存储。具体怎么存储请参考: {@link com.molihuan.pathselector.fragment.impl.PathSelectFragment#onActivityResult(int, int, Intent)}
     * 存储之后使用content.getContentResolver()获取解释器并调用getPersistedUriPermissions()来获取已经存储的路径Uri列表 List<UriPermission> uriPermissions
     * 存储的Uri封装在UriPermission的字段成员mUri中,mUri是一个Uri对象，可以通过toString转换成字符串
     * 此时存储的uri改变为content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata
     * 如果你需要判断是否已经获取了/storage/emulated/0/Android/data/路径下面的权限,你可以:
     * 注意：(content为上下文)
     * List<UriPermission> uriPermissions = content.getContentResolver().getPersistedUriPermissions();
     * String saveUri = "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata";
     * for (UriPermission uriP : uriPermissions) {
     * String tempUri = uriP.getUri().toString();
     * if (saveUri.equals(tempUri)) {
     * Log.e("相等","已经获取了/storage/emulated/0/Android/data/路径下面的权限");
     * }
     * }
     * 也可参考: {@link #getAndroidUriPermission(String, String, Activity)}
     * ************************************************************************************************
     */
    @Deprecated
    private static void getAndroidUriPermission(String requestUri, String savedUri, Activity activity, Fragment fragment) {
        //判断是否为空,为空则抛异常
        Objects.requireNonNull(requestUri, "requestUri is null");
        Objects.requireNonNull(savedUri, "savedUri is null");

        if (!VersionTool.isAndroid11()) {
            return;
        }


        if (fragment != null) {
            activity = fragment.getActivity();
        } else if (activity == null) {
            throw new NullPointerException("fragment and activity cannot both be null");
        }

        //获取已授权并已存储的uri列表
        List<UriPermission> uriPermissions = activity.getContentResolver().getPersistedUriPermissions();

        LogUtils.e(uriPermissions);

        String tempUri;
        //遍历并判断请求的uri字符串是否已经被授权
        for (UriPermission uriP : uriPermissions) {
            tempUri = uriP.getUri().toString();
            if (savedUri.equals(tempUri) && (uriP.isReadPermission() || uriP.isWritePermission())) {
                Mtools.log(requestUri + "已经授权");
                return;
            }
        }
        //执行到这里说明请求的uri字符串没有授权，跳转并请求权限
        Uri uri = Uri.parse(requestUri);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.setFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
        );
        intent.putExtra("android.provider.extra.SHOW_ADVANCED", true)
                .putExtra("android.content.extra.SHOW_ADVANCED", true)
                .putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);


        if (fragment != null) {
            fragment.startActivityForResult(intent, PERMISSION_REQUEST_CODE);
        } else if (activity != null) {
            activity.startActivityForResult(intent, PERMISSION_REQUEST_CODE);
        } else {
            throw new NullPointerException("fragment and activity cannot both be null");
        }

        /**
         * 请求后的结果需要在执行跳转的界面(Activity或Fragment等)的onActivityResult方法中存储
         * 具体怎么存储请参考: {@link com.molihuan.pathselector.fragment.impl.PathSelectFragment#onActivityResult(int, int, Intent)}
         */

    }

    @Deprecated
    public static void getAndroidUriPermission(String requestUri, String savedUri, Activity activity) {
        getAndroidUriPermission(requestUri, savedUri, activity, null);
    }

    @Deprecated
    public static void getAndroidUriPermission(String requestUri, String savedUri, Fragment fragment) {
        getAndroidUriPermission(requestUri, savedUri, null, fragment);
    }


}
