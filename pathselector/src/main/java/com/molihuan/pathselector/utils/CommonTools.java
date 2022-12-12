package com.molihuan.pathselector.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: CommonTools
 * @Author: molihuan
 * @Date: 2022/12/08/19:23
 * @Description: 通用工具类
 */
public class CommonTools {

    /**
     * 简单的将数组转换为list,注意不能修改
     *
     * @param array
     * @return
     */
    public static List<String> asStringList(String[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return Arrays.asList(array);
    }

    public static <T> ArrayList<T> asArrayList(T... array) {
        ArrayList<T> list = new ArrayList<>(array.length);
        if (array == null || array.length == 0) {
            return list;
        }
        for (T t : array) {
            list.add(t);
        }
        return list;
    }


}
