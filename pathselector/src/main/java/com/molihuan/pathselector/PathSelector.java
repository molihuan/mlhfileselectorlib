package com.molihuan.pathselector;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.molihuan.pathselector.configs.PathSelectorConfig;
import com.molihuan.pathselector.service.IConfigDataBuilder;
import com.molihuan.pathselector.service.impl.ConfigDataBuilderImpl;

import java.util.Objects;

/**
 * @ClassName: PathSelector
 * @Author: molihuan
 * @Date: 2022/11/22/22:09
 * @Description: 以数据为驱动, 先初始化数据，再由数据控制构建
 * 1、初始化DataBuilder(数据构建者)
 * 2、再由DataBuilder初始化AbstractBuildController(构建控制者)并构建
 */

public class PathSelector {

    private static Fragment fragment;

    public static Fragment getFragment() {
        return fragment;
    }

    public static void setDebug(boolean var) {
        PathSelectorConfig.setDebug(var);
    }

    public static IConfigDataBuilder build(Fragment fragment, int buildType) {
        PathSelector.fragment = fragment;
        Context context = fragment.getContext();
        Objects.requireNonNull(context, "context is null");
        return finalBuild(context, buildType);
    }

    public static IConfigDataBuilder build(Context context, int buildType) {
        PathSelector.fragment = null;
        return finalBuild(context, buildType);
    }

    private static IConfigDataBuilder finalBuild(Context context, int buildType) {
        IConfigDataBuilder builder = ConfigDataBuilderImpl.getInstance();
        builder.setContext(context)
                .setBuildType(buildType);
        return builder;
    }
}
