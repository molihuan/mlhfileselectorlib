package com.zlylib.mlhfileselectorlib.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Const
 * Created by zhangliyang on 2020/6/20.
 */

public class Const {

    public static Map<String, String> mimeTypeMap = null;

    static {
        mimeTypeMap = new HashMap<>();
        mimeTypeMap.put("apk","application/vnd.android.package-archive");
        mimeTypeMap.put("asf", "video/x-ms-asf");
        mimeTypeMap.put("avi", "video/x-msvideo");
        mimeTypeMap.put("bin", "application/octet-stream");
        mimeTypeMap.put("bmp", "image/bmp");
        mimeTypeMap.put("c", "text/plain");
        mimeTypeMap.put("class", "application/octet-stream");
        mimeTypeMap.put("conf", "text/plain");
        mimeTypeMap.put("cpp", "text/plain");
        mimeTypeMap.put("doc", "application/msword");
        mimeTypeMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        mimeTypeMap.put("xls", "application/vnd.ms-excel");
        mimeTypeMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        mimeTypeMap.put("exe", "application/octet-stream");
        mimeTypeMap.put("gif", "image/gif");
        mimeTypeMap.put("gtar", "application/x-gtar");
        mimeTypeMap.put("gz", "application/x-gzip");
        mimeTypeMap.put("h", "text/plain");
        mimeTypeMap.put("htm", "text/html");
        mimeTypeMap.put("html", "text/html");
        mimeTypeMap.put("jar", "application/java-archive");
        mimeTypeMap.put("java", "text/plain");
        mimeTypeMap.put("jpeg", "image/jpeg");
        mimeTypeMap.put("jpg", "image/jpeg");
        mimeTypeMap.put("js", "application/x-javascript");
        mimeTypeMap.put("log", "text/plain");
        mimeTypeMap.put("m3u", "audio/x-mpegurl");
        mimeTypeMap.put("m4a", "audio/mp4a-latm");
        mimeTypeMap.put("m4b", "audio/mp4a-latm");
        mimeTypeMap.put("m4p", "audio/mp4a-latm");
        mimeTypeMap.put("m4u", "video/vnd.mpegurl");
        mimeTypeMap.put("m4v", "video/x-m4v");
        mimeTypeMap.put("mov", "video/quicktime");
        mimeTypeMap.put("mp2", "audio/x-mpeg");
        mimeTypeMap.put("mp3", "audio/mpeg");
        mimeTypeMap.put("mp4", "video/mp4");
        mimeTypeMap.put("mpc", "application/vnd.mpohun.certificate");
        mimeTypeMap.put("mpe", "video/mpeg");
        mimeTypeMap.put("mpeg", "video/mpeg");
        mimeTypeMap.put("mpg", "video/mpeg");
        mimeTypeMap.put("mpg4", "video/mp4");
        mimeTypeMap.put("mpga", "audio/mpeg");
        mimeTypeMap.put("msg", "application/vnd.ms-outlook");
        mimeTypeMap.put("ogg", "audio/ogg");
        mimeTypeMap.put("pdf", "application/pdf");
        mimeTypeMap.put("png", "image/png");
        mimeTypeMap.put("pps", "application/vnd.ms-powerpoint");
        mimeTypeMap.put("ppt", "application/vnd.ms-powerpoint");
        mimeTypeMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        mimeTypeMap.put("prop", "text/plain");
        mimeTypeMap.put("rc", "text/plain");
        mimeTypeMap.put("rmvb", "audio/x-pn-realaudio");
        mimeTypeMap.put("rtf", "application/rtf");
        mimeTypeMap.put("sh", "text/plain");
        mimeTypeMap.put("tar", "application/x-tar");
        mimeTypeMap.put("tgz", "application/x-compressed");
        mimeTypeMap.put("txt", "text/plain");
        mimeTypeMap.put("wav", "audio/x-wav");
        mimeTypeMap.put("wma", "audio/x-ms-wma");
        mimeTypeMap.put("wmv", "audio/x-ms-wmv");
        mimeTypeMap.put("wps", "application/vnd.ms-works");
        mimeTypeMap.put("xml", "text/plain");
        mimeTypeMap.put("z", "application/x-compress");
        mimeTypeMap.put("zip", "application/x-zip-compressed");
        mimeTypeMap.put("", "*/*");
    }

    /*文件类型*/
    public static final String EXTRA_KEY_FILE_TYPE = "EXTRA_KEY_FILE_TYPE";
    /*排序类型*/
    public static final String EXTRA_KEY_SORT_TYPE = "EXTRA_KEY_SORT_TYPE";
    /*是否多选*/
    public static final String EXTRA_KEY_IS_SINGLE = "EXTRA_KEY_IS_SINGLE";
    /*最大可选择数量*/
    public static final String EXTRA_KEY_MAX_COUNT = "EXTRA_KEY_MAX_COUNT";
    public static final String EXTRA_KEY_OnSelectFileListener = "EXTRA_KEY_OnSelectFileListener";

    public static final String EXTRA_RESULT_SELECTION = "extra_result_selection";



}
