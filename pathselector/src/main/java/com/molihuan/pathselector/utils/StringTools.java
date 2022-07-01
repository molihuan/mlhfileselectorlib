package com.molihuan.pathselector.utils;

/**
 * @ClassName StringTools
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/24 13:34
 */
public class StringTools {
    /**
     * 判断字符串是否为空和仅仅是空格(有问题)
     * @param s
     * @return
     */
    public static boolean isEmpty(String s){
        return s==null|| s.trim().isEmpty() ? true : false;
    }

    /**
     * 字符串转Long(剔除字符串中不是数字的)
     * @param s
     * @return
     */
    public static Long getOnlyNumber(String s){
        String temp = s.replaceAll("[^0-9]", "");
        if (temp.equals("")){
            return -1L;
        }
        Long number = Long.valueOf(temp);
        if (number==null){
            return -1L;
        }
        return Long.valueOf(temp);
    }
}
