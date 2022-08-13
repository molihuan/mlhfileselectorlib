package com.molihuan.pathselector.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;

import androidx.fragment.app.Fragment;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.Iterator;
import java.util.List;

public class PermissionsTools {


    public static void getAllNeedPermissions(Activity context, Fragment fragment, ContentResolver contentResolver){
        generalPermissionsOfStorage(context);//普通存储访问权限
        specialPermissionsOfStorage(context);//特殊存储访问权限
        getAndroidDataPermissionDialog(context,fragment,contentResolver);
//
//        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager())) {
//            //表明已经有这个权限了
//        } else {
//
//            new MaterialDialog.Builder(context)
//                    .iconRes(R.drawable.xui_ic_default_tip_btn)
//                    .title("提示")
//                    .content("需要所有文件访问权限(用于创建文件和文件夹)")
//                    .positiveText("爷给")
//                    .onPositive(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            specialPermissionsOfStorage(context);//特殊存储访问权限
//                            getAndroidDataPermissionDialog(context,contentResolver);
//                        }
//                    })
//                    .negativeText("就不给")
//                    .onNegative(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            ToastUtils.make().show("不给所有文件访问权限软件无法运行");
//                        }
//                    })
//                    .show();
//        }






    }

    /**
     * 获取Android/data目录访问权限弹窗
     * @param activity
     * @param contentResolver
     */
    public static void getAndroidDataPermissionDialog(Activity activity,Fragment fragment, ContentResolver contentResolver){
        //安卓11data目录访问权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Iterator<UriPermission> it = contentResolver.getPersistedUriPermissions().iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().isReadPermission()) {
                        break;
                    }
                } else {
                    getAndroidDataPermission(activity,fragment,contentResolver);//沙盒存储访问权限

//                    new MaterialDialog.Builder(activity)
//                            .iconRes(R.drawable.xui_ic_default_tip_btn)
//                            .title("提示")
//                            .content("需要Android/data访问权限(用于读取B站缓存文件)")
//                            .positiveText("爷给")
//                            .onPositive(new MaterialDialog.SingleButtonCallback() {
//                                @Override
//                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                    getAndroidDataPermission(activity,contentResolver);//沙盒存储访问权限
//                                }
//                            })
//                            .negativeText("就不给")
//                            .onNegative(new MaterialDialog.SingleButtonCallback() {
//                                @Override
//                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                    ToastUtils.make().show("不给Android/data访问权限软件无法运行");
//                                }
//                            })
//                            .show();


                    break;
                }
            }
        }
    }



    /**
     * 获取一般读写权限
     */
    public static void generalPermissionsOfStorage(Context context) {

        //PermissionUtils.permission(PERMISSIONS_STORAGE).request();//读写权限动态获取

        boolean isGet = XXPermissions.isGranted(context, Permission.Group.STORAGE);
        if (isGet) return;//已有权限则返回

        //获取基本读取权限
        XXPermissions.with(context)
                // 申请单个权限
                //.permission(Permission.MANAGE_EXTERNAL_STORAGE)
                // 申请多个权限
                .permission(Permission.Group.STORAGE)
                // 设置不触发错误检测机制（局部设置）
                .unchecked()
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            //ToastUtils.make().show("基本读取权限获取成功");
                            if (!isAndroid11()){
                                //((MainActivity)context).refreshMainShowListView();
                            }
                        } else {
                            //ToastUtils.make().show("获取部分权限成功，但部分权限未正常授予");
                        }
                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            //ToastUtils.make().show("被永久拒绝授权，请手动授予读取权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            //XXPermissions.startPermissionActivity(context, permissions);
                        } else {
                            //ToastUtils.make().show("获取读取权限失败");
                        }
                    }
                });
    }

    /**
     * 获取全文件读取权限
     * @param context
     */
    public static void specialPermissionsOfStorage(Context context) {
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager())) {
            //表明已经有这个权限了
        } else {

            //获取全文件读取权限
            XXPermissions.with(context)
                    // 申请单个权限
                    .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                    // 申请多个权限
                    //.permission(Permission.Group.STORAGE)
                    // 设置不触发错误检测机制（局部设置）

                    .unchecked()
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            if (all) {
                                //ToastUtils.make().show("所以文件访问权限获取成功");
                                if (isAndroid11()){
                                    //PathTools.initCreateDir();//初始化创建temp目录
                                }
                            } else {
                                //ToastUtils.make().show("获取部分权限成功，但部分权限未正常授予");
                            }
                        }
                        @Override
                        public void onDenied(List<String> permissions, boolean never) {
                            if (never) {
                                //ToastUtils.make().show("被永久拒绝授权，请手动授予读取权限");
                                // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                //XXPermissions.startPermissionActivity(context, permissions);
                            } else {
                                //ToastUtils.make().show("获取读取权限失败");
                            }
                        }
                    });
        }
    }

    public static final String URI_ANRROID_DATA="content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata";//Android/data目录

    /**
     *获取Android/data目录访问权限
     * @param activity  this
     * @param contentResolver   getContentResolver()
     */
    public static void getAndroidDataPermission(Activity activity, Fragment fragment,ContentResolver contentResolver){
        //安卓11data目录访问权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Iterator<UriPermission> it = contentResolver.getPersistedUriPermissions().iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().isReadPermission()) {
                        break;
                    }
                } else {

                    Uri uri = Uri.parse(URI_ANRROID_DATA);
                    Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                            | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                    intent1.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);

                    if (fragment!=null){
                        fragment.startActivityForResult(intent1, 11);
                    }else {
                        activity.startActivityForResult(intent1, 11);
                    }

                    break;
                }
            }
        }
    }


    //content://com.android.externalstorage.documents/tree/418A-1D08%3AAndroid%2Fdata%2Fcom.amazon.mp3

    /**
     *获取SD目录访问权限
     * @param activity  this
     * @param contentResolver   getContentResolver()
     */

    public static void getSDPermission(Activity activity, Fragment fragment, ContentResolver contentResolver, String path){
        //安卓SD目录访问权限

//        Intent intent = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            StorageManager sm =activity.getSystemService(StorageManager.class);
//            StorageVolume volume = sm.getStorageVolume(new File(path));
//            if (volume != null) {
//                intent = volume.createOpenDocumentTreeIntent();
//            }
//        }
//        if (intent == null) {
//            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//        }
//        Mtools.log(intent);
//        if (fragment!=null){
//            fragment.startActivityForResult(intent, 12);
//        }else {
//            activity.startActivityForResult(intent, 12);
//        }

        //{act=android.intent.action.OPEN_DOCUMENT_TREE flg=0xc3 cmp=com.google.android.documentsui/com.android.documentsui.picker.PickActivity (has extras)}
        //{act=android.intent.action.OPEN_DOCUMENT_TREE flg=0xc3 cmp=com.google.android.documentsui/com.android.documentsui.picker.PickActivity (has extras)}


        Uri uri = Uri.parse(UriTools.getSdcardRootUriByPath(path));
        Mtools.log(uri);//uri: content://com.android.externalstorage.documents/tree/1020-1A01
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);

        if (fragment!=null){
            fragment.startActivityForResult(intent, 12);
        }else {
            activity.startActivityForResult(intent, 12);
        }


            //Iterator<UriPermission> it = contentResolver.getPersistedUriPermissions().iterator();
//            while (true) {
//                if (it.hasNext()) {
//                    if (it.next().isWritePermission()) {
//                        break;
//                    }
//                } else {
//
//                    Uri uri = Uri.parse(UriTools.getSdcardRootUriByPath(path));
//                    Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//                    intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//                            | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
//                            | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
//                    intent1.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
//
//                    if (fragment!=null){
//                        fragment.startActivityForResult(intent1, 11);
//                    }else {
//                        activity.startActivityForResult(intent1, 11);
//                    }
//
//                    break;
//                }
//            }

        }




    public static boolean isAndroid11 (){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ? true :false;
    }




}
