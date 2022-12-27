package com.molihuan.pathselector.configs;

import com.molihuan.pathselector.utils.Mtools;
import com.xuexiang.xtask.XTask;


/**
 * @ClassName: MLogConfig
 * @Author: molihuan
 * @Date: 2022/11/27/13:13
 * @Description: 总体配置设置
 */
//TODO 先要解决：关于这个库的升级提示问题：开启调试模式的时候会向服务器获取库的版本信息并判断本地库的版本是不是小于服务器的版本，如果是则向开发者的控制台打印该库可以升级，如果需要请获取新的版本
public class PathSelectorConfig {

    //是否需要自动获取权限
    public static boolean AUTO_GET_PERMISSION = true;

    /**
     * 是否开启debug模式
     * 默认开启debug模式，开发时可以开启，生产模式请务必关闭
     *
     * @param var
     */
    public static void setDebug(boolean var) {
        Mtools.setDebug(var);
        XTask.debug(var);
    }

    public static void setAutoGetPermission(boolean need) {
        AUTO_GET_PERMISSION = need;
    }
}
