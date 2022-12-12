package com.molihuan.pathselector.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.blankj.molihuan.utilcode.util.LogUtils;
import com.blankj.molihuan.utilcode.util.ToastUtils;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.molihuan.pathselector.R;

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

    //默认的uri构建后缀
    public static final int DEFAULT_URI_BUILD_SUFFIX_ANDROID_DATA = 156;
    public static final int DEFAULT_URI_BUILD_SUFFIX_ANDROID_OBB = 157;


    public static void getStoragePermissions(Context context) {
        //普通存储访问权限Android 11 -
        generalPermissionsOfStorage(context);
        //所有文件访问权限Android 11 +
        specialPermissionsOfStorage(context);
    }


    /**
     * 获取一般读写权限
     * Android 11 -
     */
    public static void generalPermissionsOfStorage(Context context) {

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
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            ToastUtils.make().show(R.string.tip_denial_authorization);
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(context, permissions);
                        } else {
                            ToastUtils.make().show(R.string.tip_permission_failed);
                        }
                    }
                });
    }

    /**
     * 获取全文件读取权限
     * Android 11 +
     *
     * @param context
     */
    public static void specialPermissionsOfStorage(Context context) {
        if (!VersionTool.isAndroid11()) {
            return;
        }

        boolean isGet = XXPermissions.isGranted(context, Permission.MANAGE_EXTERNAL_STORAGE);
        //已有权限则返回
        if (isGet) {
            Mtools.log("全文件读取权限------已获取");
            return;
        }

        XXPermissions.with(context)
                // 申请单个权限
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                // 设置不触发错误检测机制（局部设置）
                .unchecked()
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        Mtools.log("全文件读取权限------获取成功");
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            ToastUtils.make().show(R.string.tip_denial_authorization);
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(context, permissions);
                        } else {
                            ToastUtils.make().show(R.string.tip_permission_failed);
                        }
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean isGrantedUriPermission(Uri uri, Activity activity, Fragment fragment) {
        return existsGrantedUriPermission(uri, activity, fragment) != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String existsGrantedUriPermission(Uri uri, Fragment fragment) {
        return existsGrantedUriPermission(uri, null, fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String existsGrantedUriPermission(Uri uri, Activity activity) {
        return existsGrantedUriPermission(uri, activity, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String existsGrantedUriPermission(Uri uri, Activity activity, Fragment fragment) {
        if (fragment != null) {
            activity = fragment.getActivity();
        } else if (activity == null) {
            throw new NullPointerException("fragment and activity cannot both be null");
        }
        Mtools.log("请求权限的原始uri是:" + uri);
        //请求权限的原始uri是:content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata
        //获取需要授权uri的字符串，还不能匹配，还需要进行处理
        String reqUri = uri.toString().replace("documents/document/primary", "documents/tree/primary");

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

    //********************       通过默认请求权限uri后缀构建完整的uri再进行跳转       ***************************
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void applyUriPermissionByDefault(int defaultUriBuildSuf, Activity activity) throws Exception {
        applyUriPermissionByDefault(defaultUriBuildSuf, activity, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void applyUriPermissionByDefault(int defaultUriBuildSuf, Fragment fragment) throws Exception {
        applyUriPermissionByDefault(defaultUriBuildSuf, null, fragment);
    }

    /**
     * 提供两个默认的uri请求跳转(Android13无效，请使用其他方法)
     * 分别是:Android/data目录和Android/obb目录
     * 注意：Android13无法获得Android/data目录的读写权限，只能获得子目录的读写权限。使用另一种重载方法来获得Android/data子目录的读写权限
     *
     * @param defaultUriBuildSuf
     * @param activity
     * @param fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void applyUriPermissionByDefault(int defaultUriBuildSuf, Activity activity, Fragment fragment) throws Exception {
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
            goApplyUriPermissionPage(uriSuf, false, fragment);
        } else if (activity != null) {
            goApplyUriPermissionPage(uriSuf, false, activity);
        } else {
            throw new NullPointerException("fragment and activity cannot both be null");
        }
    }

    /**
     * 推荐使用此方法来获取权限
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void goApplyUriPermissionPage(String uriSuf, boolean tree, Activity activity) {
        goApplyUriPermissionPage(uriSuf, tree, activity, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void goApplyUriPermissionPage(String uriSuf, boolean tree, Fragment fragment) {
        goApplyUriPermissionPage(uriSuf, tree, null, fragment);
    }

    /**
     * @param uriSuf   请求目录权限：/storage/emulated/0/Android/data/
     *                 uriSuf = "primary:Android/data";
     *                 请求目录权限：/storage/emulated/0/Android/obb/
     *                 uriSuf = "primary:Android/obb";
     * @param tree
     * @param activity
     * @param fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void goApplyUriPermissionPage(String uriSuf, boolean tree, Activity activity, Fragment fragment) {
        /**
         * DocumentsContract.buildTreeDocumentUri():
         * content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata
         * DocumentsContract.buildDocumentUri():
         * content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata
         */
        Uri uri;
        if (tree) {
            uri = DocumentsContract.buildTreeDocumentUri(UriTools.URI_PERMISSION_REQUEST_PREFIX, uriSuf);
        } else {
            uri = DocumentsContract.buildDocumentUri(UriTools.URI_PERMISSION_REQUEST_PREFIX, uriSuf);
        }


        if (fragment != null) {
            goApplyUriPermissionPage(uri, fragment);
        } else if (activity != null) {
            goApplyUriPermissionPage(uri, activity);
        } else {
            throw new NullPointerException("fragment and activity cannot both be null");
        }
    }

    //********************       通过完整的权限请求uri进行跳转       ***************************
    private static void goApplyUriPermissionPage(String completeUri, Fragment fragment) {
        Uri uri = Uri.parse(completeUri);
        goApplyUriPermissionPage(uri, null, fragment);
    }

    public static void goApplyUriPermissionPage(Uri uri, Fragment fragment) {
        goApplyUriPermissionPage(uri, null, fragment);
    }

    /**
     * 跳转请求权限SAF页面
     *
     * @param completeUri 完整的请求Uri字符串
     * @param activity
     */
    private static void goApplyUriPermissionPage(String completeUri, Activity activity) {
        Uri uri = Uri.parse(completeUri);
        goApplyUriPermissionPage(uri, activity, null);
    }

    public static void goApplyUriPermissionPage(Uri uri, Activity activity) {
        goApplyUriPermissionPage(uri, activity, null);
    }

    /**
     * 跳转请求权限SAF页面
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
    private static void goApplyUriPermissionPage(Uri uri, Activity activity, Fragment fragment) {
        if (!VersionTool.isAndroid11()) {
            return;
        }

        //获取所有已授权并存储的Uri列表，遍历并判断需要申请的uri是否在其中,在则说明已经授权了
        boolean isGet = isGrantedUriPermission(uri, activity, fragment);//这里会对activity重新赋值
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
            fragment.startActivityForResult(intent, PERMISSION_REQUEST_CODE);
        } else {
            activity.startActivityForResult(intent, PERMISSION_REQUEST_CODE);
        }
    }


    /**
     * TODO 这里好像解释有问题，大家自己辨别
     * 注意!!!!!:
     * 这是旧版本不兼容安卓13，请用{@link #goApplyUriPermissionPage(Uri, Activity, Fragment)}替代
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
