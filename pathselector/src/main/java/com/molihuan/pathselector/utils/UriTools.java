package com.molihuan.pathselector.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.molihuan.pathselector.R;
import com.molihuan.pathselector.adapters.FileListAdapter;
import com.molihuan.pathselector.adapters.TabbarFileListAdapter;
import com.molihuan.pathselector.entities.FileBean;
import com.molihuan.pathselector.entities.TabbarFileBean;
import com.molihuan.pathselector.service.BeanListManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UriTools
 * @Description TODO uri工具类
 * @Author molihuan
 * @Date 2022/6/30 17:59
 */

public class UriTools {
    //根目录 :一般是/storage/emulated/0
    public static final String URI_ROOT="content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary";
    //Android/data目录
    public static final String URI_ANRROID_DATA="content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata";
    //uri路径分割符
    public static final String URI_SEPARATOR="%2F";

    //测试数据    Android/data/moli目录
//    public static final String URI_MOLI="content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata%2Fmoli";
//    public static final String URI_M="content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata%2Fm.txt";
//    public static final String URI_M1="content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata%2Fmoli%2Fm1.txt";
//    public static final String URI_M2="content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata%2Fmoli%2Fm2.txt";
//    public static final String URI_M3D="content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata%2Fmoli%2Fm3d";
//    public static final String URI_M4D="content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata%2Fmoli%2Fm3d%2Fm4d";
//    public static final String URI_M5="content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata%2Fmoli%2Fm3d%2Fm5.txt";
//    public static final String URI_M6="content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata%2Fmoli%2Fm3d%2Fm6.txt";






    /**
     * 数据转文件uri类型
     * 建议在调用 {@link #handleDirSubfileByUri(ContentResolver, Context, Uri, int, List)} 方法之前调用
     * @param data
     * @return
     */
    public static List data2UriMimeTypeData(List<String> data){
        List<String> backData = null;
        if (data!=null){
            backData = new ArrayList<>();
            Map<String, String> map = Constants.mimeTypeMap;//获取类型map

            for (String s : data) {//遍历设置的数据
                if (map.containsKey(s)){//判断设置的数据是否匹配map中的key
                    if (!backData.contains(map.get(s))){//backData中不存在value
                        backData.add(map.get(s));//获取map中对应的value并添加到返回list中
                    }

                }
            }

        }
        return backData;
    }

    /**
     * 把Uri转为可操作的DocumentFile对象
     * @param context
     * @param uri
     * @return
     */
    public static DocumentFile uri2DocumentFile(Context context,Uri uri){
        DocumentFile rootDocumentFile = DocumentFile.fromSingleUri(context, uri);
        return rootDocumentFile;
    }
    public static DocumentFile uri2DocumentFile(Context context,String path){
        return uri2DocumentFile(context,file2Uri(path));
    }
    public static DocumentFile uri2DocumentFile(Context context,File file){
        return uri2DocumentFile(context,file2Uri(file.getAbsolutePath()));
    }

    /**
     *初始化tabbarFileBeanList数据
     * @param tabbarList
     * @param path
     */
    public static void getTabbarFileBeanList(List<TabbarFileBean> tabbarList,String path,List<String> SdCardList){
        if (SdCardList.contains(path)){
            int i = SdCardList.indexOf(path);
            if (i==0){
                tabbarList.add(0,new TabbarFileBean(path,"内部存储",true,uri2DocumentFile(Commons.getApplicationByReflect().getBaseContext(),path)));
            }else if (i>0){
                tabbarList.add(0,new TabbarFileBean(path, String.format("SD%d", i),true,uri2DocumentFile(Commons.getApplicationByReflect().getBaseContext(),path)));
            }else{
                tabbarList.add(0,new TabbarFileBean(path,"错误163",true,uri2DocumentFile(Commons.getApplicationByReflect().getBaseContext(),path)));
            }
            return;
        }
        tabbarList.add(0,new TabbarFileBean(path,true,uri2DocumentFile(Commons.getApplicationByReflect().getBaseContext(),path)));
        getTabbarFileBeanList(tabbarList,FileTools.getParentPath(path),SdCardList);
    }

    /**
     * 通过uri更新tabbarFileBeanList数据
     * @param tabbarList
     * @param tabbarAdapter
     * @param path
     * @param type
     * @return
     */
    public static List<TabbarFileBean> upDataTabbarFileBeanListByUri (List<TabbarFileBean> tabbarList, TabbarFileListAdapter tabbarAdapter, String path, int type, List<String> SdCardList){

        //选择模式
        switch (type){
            case BeanListManager.TypeAddTabbar :
                //添加数据
                tabbarList.add(new TabbarFileBean(path,true,uri2DocumentFile(Commons.getApplicationByReflect().getBaseContext(),path)));
                break;
            case BeanListManager.TypeDelTabbar:
                //移除数据
                for (int i = tabbarList.size() - 1; i >= 0; i--) {
                    if (tabbarList.get(i).getFilePath().length()>path.length()){//移除比当前路径还长的数据
                        tabbarList.remove(i);
                    }else {
                        break;
                    }
                }
                break;
            case BeanListManager.TypeInitTabbar:
                //创建初始化数据
                if (tabbarList==null){
                    tabbarList=new ArrayList<>();
                    getTabbarFileBeanList(tabbarList,path,SdCardList);
                }else {
                    tabbarList.clear();
                    getTabbarFileBeanList(tabbarList,path,SdCardList);
                }
                break;
        }

        //刷新数据
        if (tabbarAdapter!=null){
            tabbarAdapter.notifyDataSetChanged();
        }

        return tabbarList;
    }

    /**
     * 通过uri遍历文件夹更新fileBeanList数据
     * @param context
     * @param uri
     * @param
     * @param
     */
    public static List<FileBean> upDataFileBeanListByUri(Context context, Uri uri, List<FileBean> fileBeanList, FileListAdapter fileListAdapter, List<String> fileTypes, int sortType){
        //清除列表
        if (fileBeanList==null){
            fileBeanList=new ArrayList<>();
        }else if (fileBeanList.size()!=0){
            fileBeanList.clear();
        }
        //一些数据处理
        FileBean fileBean;

        //添加数据

        if (context == null || uri == null) return null;//判空
        //通过URI创建DocumentFile对象
        DocumentFile rootDocumentFile = DocumentFile.fromSingleUri(context, uri);
        //Mtools.log("fromSingleUri完毕");
        if (rootDocumentFile == null) return null;//判空
        //Mtools.log("rootDocumentFile有值");



        //创建一个 DocumentFile表示以给定的 Uri根的文档树。其实就是获取子目录的权限
        DocumentFile pickedDir = rootDocumentFile.fromTreeUri(context, uri);
        //Mtools.log("fromTreeUri完毕");
        if (pickedDir==null) return null;//判空
        //Mtools.log("pickedDir有值");
        for (DocumentFile i : pickedDir.listFiles()) {//遍历

            fileBean=new FileBean(UriTools.uri2File(i.getUri()),true,i);
            //只添加文件后缀符合要求的、文件夹添加、没有要求就都添加
            if (fileTypes==null||fileTypes.size()==0||fileBean.isDir()||fileTypes.contains(fileBean.getFileExtension())){
                fileBeanList.add(fileBean);
            }
        }

        //排序
        BeanListManager.sortFileBeanList(fileBeanList,sortType);

        //刷新数据
        if (fileListAdapter!=null){
            fileListAdapter.notifyDataSetChanged();
            if (fileBeanList.size()==0){

                //没有数据时显示空
                fileListAdapter.setEmptyView(R.layout.fragment_empty_files_list_mlh);

            }
        }

        return fileBeanList;

    }


    /**
     * 通过uri操作文件夹下面的所有文件
     * @param contentResolver
     * @param context
     * @param uri
     * @param handleType 操作类型
     * @param filerType 过滤文件类型
     */
    public static void handleDirSubfileByUri(ContentResolver contentResolver, Context context, Uri uri, int handleType, List<String> filerType){
        if (context == null || uri == null) return;//判空
        //通过URI创建DocumentFile对象
        DocumentFile rootDocumentFile = DocumentFile.fromSingleUri(context, uri);
        //Mtools.log("fromSingleUri完毕");
        if (rootDocumentFile == null) return;
        //Mtools.log("rootDocumentFile有值");
        //创建一个 DocumentFile表示以给定的 Uri根的文档树。其实就是获取子目录的权限
        DocumentFile pickedDir = rootDocumentFile.fromTreeUri(context, uri);
        //Mtools.log("fromTreeUri完毕");
        if (pickedDir==null) return;
        //Mtools.log("pickedDir有值");
        for (DocumentFile i : pickedDir.listFiles()) {//遍历
            if (i.isDirectory()) {//如果是一个目录
                handleDirSubfileByUri(contentResolver,context, i.getUri(), handleType,filerType);//递归
            } else {//如果是一个文件

                if (filerType!=null&&!filerType.contains(i.getType())){//过滤类型
                    continue;//不处理
                }

                Mtools.log("文件:"+i.getName());
                Mtools.log("类型:"+i.getType());
                switch (handleType){//选择操作
                    case 0://复制文件到指定目录
                        copyFileByUri(contentResolver,i.getUri(),"/storage/emulated/0/"+i.getName());
                        break;
                    default:
                }
            }
        }
    }

    public static void handleDirSubfileByUri(ContentResolver contentResolver, Context context, Uri uri, int handleType){
        handleDirSubfileByUri(contentResolver,context,uri,handleType,null);
    }

    /**
     * 复制data里面的文件到指定目录targetPath--------uri为文件的Uri，targetPath为指定目录的全路径
     * @param contentResolver
     * @param sourceUri
     * @param filePath
     * @return
     */
    private static boolean copyFileByUri(ContentResolver contentResolver, Uri sourceUri, String filePath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = contentResolver.openInputStream(sourceUri);//读取源文件转换为输入流

            File destFile = new File(filePath);
            out = new FileOutputStream(destFile);//目的路径文件转换为输出流

            if (out==null){
                return false;
            }

            byte[] flush = new byte[1024];
            int len = -1;
            while ((len = in.read(flush)) != -1) {//边读边写
                out.write(flush, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }



    /**
     * 根据file转换为uri(只对/storage/emulated/0/Android/data下面的有效)
     * 字符形式
     * @param path  /storage/emulated/0/Android/data   /moli/m3d/m5.txt
     * @return  "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata%2Fmoli%2Fm3d%2Fm5.txt"
     */
    public static Uri file2Uri(String path){
        if (path.matches(Constants.PATH_ANRROID_DATA+"(.*)")){//目录是Android/data孩子目录
            String lastPart = path.replace(Constants.PATH_ANRROID_DATA, "").replace(File.separator,URI_SEPARATOR);//处理后面的部分/moli/m3d/m5.txt
            return Uri.parse(URI_ANRROID_DATA+lastPart);
        }
        return null;
    }
    public static Uri file2Uri(File file){
        return file2Uri(file.getAbsolutePath());
    }


    /**
     * 方法一
     * 根据uri转换为file(只对/storage/emulated/0/Android/data下面的有效)
     * 字符形式
     * @param uri
     * @return
     */
    public static String uri2File(Uri uri){
        String s = uri.toString();
        if (s.matches(URI_ANRROID_DATA+"(.*)")) {//目录是Android/data孩子目录
            String lastPart = s.replace(URI_ANRROID_DATA, "").replace(URI_SEPARATOR, File.separator);
            return Constants.PATH_ANRROID_DATA+lastPart;
        }
        return null;
    }
    /**
     * 方法二
     * 根据uri转换为file(通用)
     * @param uri
     * @return
     */
    public static File uri2File(Uri uri,Context context,Application app){

        if (uri == null) {
            return null;
        } else {
            File file = uri2FileReal(uri,context,app);
            return file != null ? file : copyUri2Cache(uri,context,app);
        }
    }

    /**
     * 根据uri转换为file辅助方法
     * @param uri
     * @param context
     * @param app
     * @return
     */
    /****************根据uri转换为file辅助方法开始***************************************************/
    private static File copyUri2Cache(Uri uri,Context context,Application app) {
        Log.d("UriUtils", "copyUri2Cache() called");
        InputStream is = null;

        File var3;
        try {
            is =app.getContentResolver().openInputStream(uri);
            File file = new File(context.getCacheDir(), "" + System.currentTimeMillis());
            FileTools.writeFileFromIS(file, is,false);
            var3 = file;
            return var3;
        } catch (FileNotFoundException var13) {
            var13.printStackTrace();
            var3 = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

        }

        return var3;
    }


    private static File uri2FileReal(Uri uri,Context context,Application app) {
        Log.d("UriUtils", uri.toString());
        String authority = uri.getAuthority();
        String scheme = uri.getScheme();
        String path = uri.getPath();
        if (Build.VERSION.SDK_INT >= 24 && path != null) {
            String[] externals = new String[]{"/external/", "/external_path/"};
            File file = null;
            String[] var6 = externals;
            int var7 = externals.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String external = var6[var8];
                if (path.startsWith(external)) {
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + path.replace(external, "/"));
                    if (file.exists()) {
                        Log.d("UriUtils", uri.toString() + " -> " + external);
                        return file;
                    }
                }
            }

            file = null;
            if (path.startsWith("/files_path/")) {
                file = new File(context.getFilesDir().getAbsolutePath() + path.replace("/files_path/", "/"));
            } else if (path.startsWith("/cache_path/")) {
                file = new File(context.getCacheDir().getAbsolutePath() + path.replace("/cache_path/", "/"));
            } else if (path.startsWith("/external_files_path/")) {
                file = new File(context.getExternalFilesDir((String)null).getAbsolutePath() + path.replace("/external_files_path/", "/"));
            } else if (path.startsWith("/external_cache_path/")) {
                file = new File(context.getExternalCacheDir().getAbsolutePath() + path.replace("/external_cache_path/", "/"));
            }

            if (file != null && file.exists()) {
                Log.d("UriUtils", uri.toString() + " -> " + path);
                return file;
            }
        }

        if ("file".equals(scheme)) {
            if (path != null) {
                return new File(path);
            } else {
                Log.d("UriUtils", uri.toString() + " parse failed. -> 0");
                return null;
            }
        } else if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context, uri)) {
            String id;
            String type;
            String[] split;
            if ("com.android.externalstorage.documents".equals(authority)) {
                id = DocumentsContract.getDocumentId(uri);
                split = id.split(":");
                type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return new File(Environment.getExternalStorageDirectory() + "/" + split[1]);
                } else {
                    @SuppressLint("WrongConstant") StorageManager mStorageManager = (StorageManager)app.getSystemService("storage");

                    try {
                        Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
                        Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
                        Method getUuid = storageVolumeClazz.getMethod("getUuid");
                        Method getState = storageVolumeClazz.getMethod("getState");
                        Method getPath = storageVolumeClazz.getMethod("getPath");
                        Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
                        Method isEmulated = storageVolumeClazz.getMethod("isEmulated");
                        Object result = getVolumeList.invoke(mStorageManager);
                        int length = Array.getLength(result);

                        for(int i = 0; i < length; ++i) {
                            Object storageVolumeElement = Array.get(result, i);
                            boolean mounted = "mounted".equals(getState.invoke(storageVolumeElement)) || "mounted_ro".equals(getState.invoke(storageVolumeElement));
                            if (mounted && (!(Boolean)isPrimary.invoke(storageVolumeElement) || !(Boolean)isEmulated.invoke(storageVolumeElement))) {
                                String uuid = (String)getUuid.invoke(storageVolumeElement);
                                if (uuid != null && uuid.equals(type)) {
                                    return new File(getPath.invoke(storageVolumeElement) + "/" + split[1]);
                                }
                            }
                        }
                    } catch (Exception var23) {
                        Log.d("UriUtils", uri.toString() + " parse failed. " + var23.toString() + " -> 1_0");
                    }

                    Log.d("UriUtils", uri.toString() + " parse failed. -> 1_0");
                    return null;
                }
            } else if (!"com.android.providers.downloads.documents".equals(authority)) {
                if ("com.android.providers.media.documents".equals(authority)) {
                    id = DocumentsContract.getDocumentId(uri);
                    split = id.split(":");
                    type = split[0];
                    Uri contentUri;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else {
                        if (!"audio".equals(type)) {
                            Log.d("UriUtils", uri.toString() + " parse failed. -> 1_2");
                            return null;
                        }

                        contentUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{split[1]};
                    return getFileFromUri(contentUri, "_id=?", selectionArgs, "1_2",app);
                } else if ("content".equals(scheme)) {
                    return getFileFromUri(uri, "1_3",app);
                } else {
                    Log.d("UriUtils", uri.toString() + " parse failed. -> 1_4");
                    return null;
                }
            } else {
                id = DocumentsContract.getDocumentId(uri);
                if (TextUtils.isEmpty(id)) {
                    Log.d("UriUtils", uri.toString() + " parse failed(id is null). -> 1_1");
                    return null;
                } else if (id.startsWith("raw:")) {
                    return new File(id.substring(4));
                } else {
                    if (id.startsWith("msf:")) {
                        id = id.split(":")[1];
                    }

                    long availableId = 0L;

                    try {
                        availableId = Long.parseLong(id);
                    } catch (Exception var22) {
                        return null;
                    }

                    String[] contentUriPrefixesToTry = new String[]{"content://downloads/public_downloads", "content://downloads/all_downloads", "content://downloads/my_downloads"};
                    String[] var30 = contentUriPrefixesToTry;
                    int var34 = contentUriPrefixesToTry.length;

                    for(int var10 = 0; var10 < var34; ++var10) {
                        String contentUriPrefix = var30[var10];
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), availableId);

                        try {
                            File file = getFileFromUri(contentUri, "1_1",app);
                            if (file != null) {
                                return file;
                            }
                        } catch (Exception var21) {
                        }
                    }

                    Log.d("UriUtils", uri.toString() + " parse failed. -> 1_1");
                    return null;
                }
            }
        } else if ("content".equals(scheme)) {
            return getFileFromUri(uri, "2",app);
        } else {
            Log.d("UriUtils", uri.toString() + " parse failed. -> 3");
            return null;
        }
    }


    private static File getFileFromUri(Uri uri, String code,Application app) {
        return getFileFromUri(uri, (String)null, (String[])null, code,app);
    }

    private static File getFileFromUri(Uri uri, String selection, String[] selectionArgs, String code, Application app) {
        File fileDir;
        if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
            if (!TextUtils.isEmpty(uri.getLastPathSegment())) {
                return new File(uri.getLastPathSegment());
            }
        } else {
            String path;
            if ("com.tencent.mtt.fileprovider".equals(uri.getAuthority())) {
                path = uri.getPath();
                if (!TextUtils.isEmpty(path)) {
                    fileDir = Environment.getExternalStorageDirectory();
                    return new File(fileDir, path.substring("/QQBrowser".length(), path.length()));
                }
            } else if ("com.huawei.hidisk.fileprovider".equals(uri.getAuthority())) {
                path = uri.getPath();
                if (!TextUtils.isEmpty(path)) {
                    return new File(path.replace("/root", ""));
                }
            }
        }

        Cursor cursor = app.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, (String)null);
        if (cursor == null) {
            Log.d("UriUtils", uri.toString() + " parse failed(cursor is null). -> " + code);
            return null;
        } else {
            File var6;
            try {
                if (!cursor.moveToFirst()) {
                    Log.d("UriUtils", uri.toString() + " parse failed(moveToFirst return false). -> " + code);
                    fileDir = null;
                    return fileDir;
                }

                int columnIndex = cursor.getColumnIndex("_data");
                if (columnIndex <= -1) {
                    Log.d("UriUtils", uri.toString() + " parse failed(columnIndex: " + columnIndex + " is wrong). -> " + code);
                    var6 = null;
                    return var6;
                }

                var6 = new File(cursor.getString(columnIndex));
                return var6;
            } catch (Exception var10) {
                Log.d("UriUtils", uri.toString() + " parse failed. -> " + code);
                var6 = null;
            } finally {
                cursor.close();
            }

            return var6;
        }
    }
    /****************根据uri转换为file辅助方法结束***************************************************/


}
