package com.molihuan.pathselector.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;

import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author: molihuan
 * @Date: 2023/9/24
 * @Github: https://github.com/molihuan
 * @Description:
 * @Doc:
 */
public class DocumentFileTools {
    //uri请求权限构建前缀
    public static final String URI_PERMISSION_REQUEST_PREFIX = "com.android.externalstorage.documents";
    //uri请求权限构建后缀主要特殊符号
    public static final String URI_PERMISSION_REQUEST_SUFFIX_SPECIAL_SYMBOL = "primary:";

    public static DocumentFile path2documentFile(String uriPath, Activity activity) {
        return path2documentFile(uriPath, activity, null);
    }

    public static DocumentFile path2documentFile(String uriPath, Fragment fragment) {
        return path2documentFile(uriPath, null, fragment);
    }

    /**
     * 路径转documentFile
     * 路径："/storage/emulated/0/Android/data/com.molihuan.demo01/1.txt"
     * 转DocumentFile的Uri为:content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2Fcom.molihuan.demo01/document/primary%3AAndroid%2Fdata%2Fcom.molihuan.demo01%2F1.txt
     * <p>
     * 路径："/storage/emulated/0/Android/data/com.molihuan.demo01/files/1.txt"
     * 转DocumentFile的Uri为:content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2Fcom.molihuan.demo01/document/primary%3AAndroid%2Fdata%2Fcom.molihuan.demo01%2Ffiles%2F1.txt
     *
     * @param uriPath
     * @param activity
     * @param fragment
     * @return
     */
    public static DocumentFile path2documentFile(String uriPath, Activity activity, Fragment fragment) {
        if (fragment != null) {
            activity = fragment.getActivity();
        } else if (activity == null) {
            throw new NullPointerException("fragment and activity cannot both be null");
        }

        Uri uri = path2Uri(uriPath, false);

        return uri2documentFile(uri, activity);
    }

    /**
     * path转uri
     *
     * @param path /storage/emulated/0/Android/data/moli/m3d/m5.txt
     * @param tree false
     * @return content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2Fmoli%2Fm3d%2Fm5.txt
     */
    public static Uri path2Uri(String path, boolean tree) {
        if (!VersionTool.isAndroid5()) {
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

    public static String uri2Path(Uri uri) {
        Environment.getExternalStorageDirectory().getAbsolutePath();
        return "";
    }


    /**
     * uri转documentFile
     *
     * @param uri
     * @param activity
     * @return
     */
    public static DocumentFile uri2documentFile(Uri uri, Activity activity) {
        String existsPermission = PermissionsTools.existsGrantedUriPermission(uri, false, activity);
        if (existsPermission == null) {
            Mtools.log("no permissions");
            return null;
        }

        Uri targetUri = Uri.parse(existsPermission + uri.toString().replaceFirst(UriTools.URI_PERMISSION_REQUEST_COMPLETE_PREFIX, ""));

        DocumentFile rootDocumentFile = DocumentFile.fromTreeUri(activity, targetUri);
        return rootDocumentFile;
    }

    /**
     * 创建文件夹(递归)
     *
     * @param uriParentPath
     * @param dirName
     * @param activity
     * @return
     */
    public static DocumentFile createDirectory(String uriParentPath, String dirName, Activity activity) {
        DocumentFile documentFile = path2documentFile(uriParentPath, activity);
        if (!documentFile.exists()) {
            String parentPath = FileTools.getParentPath(uriParentPath);
            String directoryName = FileTools.getDirectoryName(uriParentPath);
            createDirectory(parentPath, directoryName, activity);
        }

        return documentFile.createDirectory(dirName);
    }

    /**
     * 创建文件
     *
     * @param uriParentPath 父目录
     * @param mimeType      文件类型
     * @param fileName      文件名
     * @param activity      上下文
     * @param data          数据
     * @param isCover       是否覆盖
     * @return
     */
    public static DocumentFile createFile(String uriParentPath, String mimeType, String fileName, Activity activity, byte[] data, boolean isCover) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("dada errer");
        }

        DocumentFile parentDocumentFile = path2documentFile(uriParentPath, activity);
        DocumentFile newDocumentFile = null;

        //文件夹不存在
        if (!parentDocumentFile.exists()) {
            String parentPath = FileTools.getParentPath(uriParentPath);
            String directoryName = FileTools.getDirectoryName(uriParentPath);
            createDirectory(parentPath, directoryName, activity);
        } else {
            //文件夹存在、判断文件是否存在
            boolean exists = exists(uriParentPath + File.separator + fileName, activity);
            if (exists) {
                if (!isCover) {
                    //不覆盖，直接返回
                    return parentDocumentFile.findFile(fileName);
                }
                //覆盖
                delete(uriParentPath + File.separator + fileName, activity);
            }
        }
        //新建
        newDocumentFile = parentDocumentFile.createFile(mimeType, fileName);
        //流操作
        OutputStream os = null;
        BufferedOutputStream output = null;
        try {
            os = activity.getContentResolver().openOutputStream(newDocumentFile.getUri());
            output = new BufferedOutputStream(os);
            output.write(data);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return newDocumentFile;
    }

    /**
     * 读取文件
     *
     * @param uriPath
     * @param activity
     * @return
     */
    public static byte[] read(String uriPath, Activity activity) {
        DocumentFile documentFile = path2documentFile(uriPath, activity);
        if (!documentFile.exists()) {
            return null;
        }
        if (!documentFile.isFile()) {
            return null;
        }

        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;

        try {
            inputStream = activity.getContentResolver().openInputStream(documentFile.getUri());
            if (inputStream != null) {
                outputStream = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }

                return outputStream.toByteArray();
            } else {
                throw new IOException("Failed to open input stream for URI: " + documentFile.getUri());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static DocumentFile copyFile2Uri(File sourceFile, String uriParentPath, Activity activity, boolean isCover) {
        String fileExtension = FileTools.getFileExtension(sourceFile).toLowerCase();
        String mimeType = MConstants.mimeTypeMap.get(fileExtension);
        return copyFile2Uri(sourceFile, uriParentPath, mimeType, activity, isCover);
    }

    /**
     * 将file复制到uri路径下
     * Copy File under the URI path
     *
     * @param sourceFile    源文件
     * @param uriParentPath 拷贝目录(文件夹)
     * @param mimeType      文件类型
     * @param activity      上下文
     * @param isCover       是否覆盖
     * @return
     */
    public static DocumentFile copyFile2Uri(File sourceFile, String uriParentPath, String mimeType, Activity activity, boolean isCover) {
        return createFile(uriParentPath, mimeType, sourceFile.getName(), activity, FileTools.fileToByteArray(sourceFile), isCover);
    }

    public static DocumentFile copyUri2Uri(String sourceUriPath, String uriParentPath, Activity activity, boolean isCover) {
        DocumentFile sourceDocumentFile = path2documentFile(sourceUriPath, activity);
        byte[] sourceByte = read(sourceUriPath, activity);
        return createFile(uriParentPath, sourceDocumentFile.getType(), sourceDocumentFile.getName(), activity, sourceByte, isCover);
    }

    public static File copyUri2File(String sourceUriPath, String filePath, Activity activity, boolean isCover) {
        File file = new File(filePath);
        if (file.exists()) {
            if (!isCover) {
                return file;
            }
            file.delete();
        }

        byte[] byteArray = read(sourceUriPath, activity);
        FileOutputStream fileOutputStream = null;
        try {

            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArray);
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    /**
     * 移动
     *
     * @param sourceFile
     * @param uriParentPath
     * @param mimeType
     * @param activity
     * @param isCover
     * @return
     */
    public static DocumentFile moveFile2Uri(File sourceFile, String uriParentPath, String mimeType, Activity activity, boolean isCover) {
        DocumentFile documentFile = copyFile2Uri(sourceFile, uriParentPath, mimeType, activity, isCover);
        if (documentFile.exists()) {
            sourceFile.delete();
        }
        return documentFile;
    }

    public static DocumentFile moveUri2Uri(String sourceUriPath, String uriParentPath, Activity activity, boolean isCover) {
        DocumentFile documentFile = copyUri2Uri(sourceUriPath, uriParentPath, activity, isCover);
        if (documentFile.exists()) {
            delete(sourceUriPath, activity);
        }
        return documentFile;
    }

    public static File moveUri2File(String sourceUriPath, String filePath, Activity activity, boolean isCover) {
        File file = copyUri2File(sourceUriPath, filePath, activity, isCover);
        if (file.exists()) {
            delete(sourceUriPath, activity);
        }
        return file;
    }

    /**
     * 重命名
     *
     * @param uriPath
     * @param newName
     * @param activity
     * @return
     */
    public static boolean rename(String uriPath, String newName, Activity activity) {
        DocumentFile documentFile = path2documentFile(uriPath, activity);
        return documentFile.renameTo(newName);
    }

    /**
     * 判断是否存在
     *
     * @param uriPath
     * @param activity
     * @return
     */
    public static boolean exists(String uriPath, Activity activity) {
        DocumentFile documentFile = path2documentFile(uriPath, activity);
        return documentFile.exists();
    }


    /**
     * 删除文件或文件夹
     * Delete a file or folder
     *
     * @param uriPath
     * @param activity
     * @return
     */
    public static boolean delete(String uriPath, Activity activity) {
        DocumentFile documentFile = path2documentFile(uriPath, activity);
        return documentFile.delete();
    }


}
