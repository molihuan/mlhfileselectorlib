

<p align="center">
<img src="https://s2.loli.net/2022/12/14/WoYwfehDNHbMzIZ.png" alt="Banner" />
</p>

<h1 align="center">mlhfileselector</h1>

[![Maven Central](https://img.shields.io/maven-central/v/io.github.molihuan/pathselector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.molihuan%22%20AND%20a:%22pathselector%22)[![jitpack](https://jitpack.io/v/molihuan/mlhfileselectorlib.svg)](https://jitpack.io/#molihuan/mlhfileselectorlib)![API: 19-33 (shields.io)](https://img.shields.io/badge/API-19--33-green)[![license: Apache-2.0 (shields.io)](https://img.shields.io/badge/license-Apache--2.0-brightgreen)](https://github.com/molihuan/mlhfileselectorlib/blob/master/LICENSE)[![Star](https://img.shields.io/github/stars/molihuan/mlhfileselectorlib.svg)](https://github.com/molihuan/mlhfileselectorlib)[![bilibili: 玲莫利 (shields.io)](https://img.shields.io/badge/bilibili-玲莫利-orange)](https://space.bilibili.com/454222981)[![CSDN: molihuan (shields.io)](https://img.shields.io/badge/CSDN-molihuan-blue)](https://blog.csdn.net/molihuan)

<h3 align="center">Andriod上提供文件或路径选择的第三方库</h3>
<p align="center">自动申请存储权限，支持安卓4.4 ~ 13，支持Android/data和Android/obb目录访问，</p>
<p align="center">支持自定义UI，支持SD卡。</p>
<p align="center">(Library that provides file or path selection on Android, automatically apply for storage permission, support Android 4.4 to 13, support Android/data and Android/obb directory access, support custom UI,support SD card.The Keyword:file selector operator android/data android 11 android 13)</p>



## 语言(Language)

#### **[中文](./README.md)** | [English](./README_EN.md)

## 为什么选择我

自动申请存储权限，支持 Android4.4 ~ 13，再也不用为了适配各种版本而苦恼了，快速集成，一句代码搞定，完善的文档，支持无root权限访问和操作Android/data和Android/obb目录(适配Android 13)，支持SD卡，高度自定义UI满足你的所有需求，使用非常灵活，支持国际化，对于Android文件选择你只需要关注你的业务代码即可其他的都交给它。

## 特性

- [x] 自动申请存储权限(可以控制)
- [x] 安卓 4.4 ~ 13
- [x] Android/data和Android/obb目录访问和操作
- [x] SD卡
- [x] 高度自定义UI
- [x] 国际化
- [ ] 搜索功能
- [x] 自定义图标
- [ ] 显示隐藏文件

## 前言

#### 在开始之前可以给项目一个Star吗？非常感谢，你的支持是我唯一的动力。欢迎Star和Issues!

#### 欢迎Pr，请Pr提交到dev分支

#### 项目地址：
##### [Github地址](https://github.com/molihuan/mlhfileselectorlib)
##### [Gitee地址](https://gitee.com/molihuan/mlhfileselectorlib)

## demo演示：

#### 系统版本：Android 13 

#### 下载链接：[体验APP](https://github.com/molihuan/mlhfileselectorlib/tree/master/app/release)

![pathSelectorDemo.gif](https://s2.loli.net/2022/12/14/QSGrIvwzYKhZuMe.gif)

## 一、快速开始

#### 第1步：添加仓库:

- ##### 如果你的项目 Gradle 配置是在 `7.0 以下`，需要在 `build.gradle` 文件中加入

```java
allprojects {
    repositories {
        ...
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

- ##### 如果你的 Gradle 配置是 `7.0 及以上`，则需要在 `settings.gradle` 文件中加入

```java
dependencyResolutionManagement {
    repositories {
    	...
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

#### 第2步：添加远程依赖:

- ##### 配置完远程仓库后，在项目 app 模块下的 `build.gradle` 文件中加入远程依赖

- ##### 最新发布版:[![Maven Central](https://img.shields.io/maven-central/v/io.github.molihuan/pathselector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.molihuan%22%20AND%20a:%22pathselector%22)

```java
dependencies {
    ...
    // 请将"版本"替换成具体的版本号，如 1.1.11
    implementation 'io.github.molihuan:pathselector:版本'
}
```

#### 第3步：基本用法示范:

```java
//如果没有权限会自动申请权限
PathSelector.build(this, MConstants.BUILD_DIALOG)//Dialog构建方式
        .setMorePopupItemListeners(
                new CommonItemListener("OK") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        
                        /**取消dialog弹窗
 * pathSelectFragment.dismiss();
 */

                        StringBuilder builder = new StringBuilder();
                        builder.append("you selected:\n");
                        for (FileBean fileBean : selectedFiles) {
                            builder.append(fileBean.getPath() + "\n");
                        }
                        Mtools.toast(builder.toString());

                        return false;
                    }
                }
        )
        .show();//开始构建
```



## 二、基本设置

#### 打开调试模式

```java
//开启调试模式，生产环境请关闭
PathSelectorConfig.setDebug(true);
//或者PathSelector.setDebug(true);
```

#### 1、Activity构建模式：

```java
//Activity构建方式
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_ACTIVITY)
        .setRequestCode(635)
        .setMorePopupItemListeners(
                new CommonItemListener("OK") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {

                        StringBuilder builder = new StringBuilder();
                        builder.append("you selected:\n");
                        for (FileBean fileBean : selectedFiles) {
                            builder.append(fileBean.getPath() + "\n");
                        }
                        Mtools.toast(builder.toString());

                        return false;
                    }
                }
        )
        .show();
```

#### 2、Fragment构建模式：

##### 第1步：在你需要显示的布局文件xml中使用FrameLayout占位

```xml
<FrameLayout
    android:id="@+id/fragment_select_show_area"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

##### 第2步：编写代码

```java
//获取PathSelectFragment实例然后在onBackPressed中处理返回按钮点击事件
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_FRAGMENT)
        .setFrameLayoutId(R.id.fragment_select_show_area)//加载位置,FrameLayout的ID
        .setMorePopupItemListeners(
                new CommonItemListener("OK") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {

                        StringBuilder builder = new StringBuilder();
                        builder.append("you selected:\n");
                        for (FileBean fileBean : selectedFiles) {
                            builder.append(fileBean.getPath() + "\n");
                        }
                        
                        Mtools.toast(builder.toString());
                        return false;
                    }
                }
        )
        .show();//开始构建
```

##### 第3步：重写onBackPressed()方法让路径选择器优先处理返回按钮点击事件

<h5 style="color:red"> 非常重要!!!</h5>

<h5 style="color:red"> 非常重要!!!</h5>

<h5 style="color:red"> 非常重要!!!</h5>

##### 重要的事情说三遍

```java
@Override
public void onBackPressed() {

    //让PathSelectFragment先处理返回按钮点击事件
    if (selector != null && selector.onBackPressed()) {
        return;
    }
    ......
    super.onBackPressed();
}
```

#### 3、Dialog构建模式 & 常用设置：

```java
//获取PathSelectFragment实例然后在onBackPressed中处理返回按钮点击事件
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
        //.setBuildType(MConstants.BUILD_DIALOG)//已经在build中已经设置了
        //.setContext(this)//已经在build中已经设置了
        .setRootPath("/storage/emulated/0/")//初始路径
        .setShowSelectStorageBtn(true)//是否显示内部存储选择按钮
        .setShowTitlebarFragment(true)//是否显示标题栏
        .setShowTabbarFragment(true)//是否显示面包屑
        .setAlwaysShowHandleFragment(true)//是否总是显示长按弹出选项
        .setShowFileTypes("", "mp3", "mp4")//只显示(没有后缀)或(后缀为mp3)或(后缀为mp4)的文件
        .setSelectFileTypes("", "mp3")//只能选择(没有后缀)或(后缀为mp3)的文件
        .setMaxCount(3)//最多可以选择3个文件,默认是-1不限制
        .setRadio()//单选(如果需要单选文件夹请使用setMaxCount(0)来替换)
        .setSortType(MConstants.SORT_NAME_ASC)//按名称排序
        .setTitlebarMainTitle(new FontBean("My Selector"))//设置标题栏主标题,还可以设置字体大小,颜色等
        .setTitlebarBG(Color.GREEN)//设置标题栏颜色
        .setFileItemListener(//设置文件item点击回调(点击是文件才会回调,如果点击是文件夹则不会)
                new FileItemListener() {
                    @Override
                    public boolean onClick(View v, FileBean file, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        Mtools.toast("you clicked path:\n" + file.getPath());
                        return false;
                    }
                }
        )
        .setMorePopupItemListeners(//设置右上角选项回调
                new CommonItemListener("SelectAll") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        pathSelectFragment.selectAllFile(true);
                        return false;
                    }
                },
                new CommonItemListener("DeselectAll") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        pathSelectFragment.selectAllFile(false);
                        return false;
                    }
                }
        )
        .setHandleItemListeners(//设置长按弹出选项回调
                new CommonItemListener("OK") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("you selected:\n");
                        for (FileBean fileBean : selectedFiles) {
                            builder.append(fileBean.getPath() + "\n");
                        }
                        Mtools.toast(builder.toString());
                        return false;
                    }
                },
                new CommonItemListener("cancel") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        pathSelectFragment.openCloseMultipleMode(false);
                        return false;
                    }
                }
        )
        .show();
```

## 三、高级设置(自定义UI)

#### UI布局：

![UiLayout.png](https://s2.loli.net/2022/12/14/Yw8bamgrtNC9yiW.png)

#### 1、自定义选项样式(以HandleItem为例子)

##### 方式1:通过FontBean来设置样式

```java
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
        .setHandleItemListeners(//设置长按弹出选项回调
                //FontBean可以设置文本、字的大小、字的颜色、字左边的图标
    			//R.drawable.ic_test_mlh是你自己的图片资源id
                new CommonItemListener(new FontBean("OK", 18, Color.RED, R.drawable.ic_test_mlh)) {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        Mtools.toast("You Click");
                        return false;
                    }
                }
        )
        .show();
```

<h5 style="color:red">  什么？这种方式还不能满足你，那么试试方式2</h5>

##### 方式2:重写CommonItemListener的setViewStyle方法来自定义样式

```java
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
        .setHandleItemListeners(
                //重写CommonItemListener的setViewStyle方法来自定义样式
                new CommonItemListener("OK") {
                    @Override
                    public boolean setViewStyle(RelativeLayout container, ImageView leftImg, TextView textView) {
                        textView.setTextSize(18);
                        textView.setTextColor(Color.RED);
                        //默认是不显示图标的
                        leftImg.setVisibility(View.VISIBLE);
                        leftImg.setImageResource(R.drawable.ic_test_mlh);
                        leftImg.getLayoutParams().width = 90;
                        leftImg.getLayoutParams().height = 90;
                        return true;
                    }

                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        Mtools.toast("You Click");
                        return false;
                    }
                }
        )
        .show();
```
<h4 style="color:red"> 什么？什么？这种方式还不能满足你，那么你来写UI它来帮你添加，试试高度自定义UI</h4>

#### 2、高度自定义UI(以Titlebar为例子)：

##### 第1步：新建一个布局文件，如:fragment_custom_titlebar.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:orientation="horizontal">

    <Button
        android:id="@+id/my_btn1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="btn1" />

    <Button
        android:id="@+id/my_btn2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="selectAll" />
    
</LinearLayout>
```

##### 第2步：新建一个类，如:CustomTitlebarFragment.class使其继承AbstractTitlebarFragment并关联第1步中的布局文件

```java
public class CustomTitlebarFragment extends AbstractTitlebarFragment {
    private Button btn1;
    private Button btn2;
    
    @Override
    public int setFragmentViewId() {
        return R.layout.fragment_custom_titlebar;
    }

    @Override
    public void getComponents(View view) {
        btn1 = view.findViewById(R.id.my_btn1);
        btn2 = view.findViewById(R.id.my_btn2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mtools.toast("The current path is:\n" + psf.getCurrentPath());
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psf.selectAllFile(true);
            }
        });
    }
}
```

##### 第3步：编写代码

```java
//获取PathSelectFragment实例然后在onBackPressed中处理返回按钮点击事件
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
        .setTitlebarFragment(new CustomTitlebarFragment())
        .show();
```

#### 3、自定义列表item图标

```java
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
        .setFileBeanController(new AbstractFileBeanController() {
            @Override
            public int getFileBeanImageResource(boolean isDir, String extension, FileBean fileBean) {
                int resourceId;
                switch (extension) {
                    case "jpg":
                    case "jpeg":
                    case "png":
                        //开发者自己的图片资源id
                        resourceId = R.drawable.ic_launcher_foreground;
                        break;
                    case "mp3":
                        resourceId = R.drawable.ic_launcher_foreground;
                        break;
                    case "mp4":
                        //也可以使用默认的图片资源id
                        resourceId = com.molihuan.pathselector.R.mipmap.movie;
                        break;
                    default:
                        if (isDir) {
                            //开发者自己的图片资源id
                            resourceId = R.drawable.ml192;
                        } else {
                            resourceId = R.drawable.ic_launcher_background;
                        }
                        break;
                }
                return resourceId;
            }
        })
        .show();
```



## 四、接口与方法（尽量看源码，都写了注释，懒得写文档）

##### IConfigDataBuilder


| 方法                                                         | 作用                                                   | 备注                                            |
| ------------------------------------------------------------ | ------------------------------------------------------ | ----------------------------------------------- |
| setFrameLayoutId(int id)                                     | 设置加载位置FrameLayoutID                              | 当构建模式为MConstants.BUILD_FRAGMENT时必须设置 |
| setRequestCode(int code)                                     | 设置请求码                                             | 当构建模式为MConstants.BUILD_ACTIVITY时必须设置 |
| setRootPath(String path)                                     | 设置开始默认路径                                       | 默认为内部存储根路径                            |
| setMaxCount(int maxCount)                                    | 设置最大选择数量                                       | 不设置默认为-1 即无限                           |
| setShowFileTypes(String... fileTypes)                        | 设置显示文件类型                                       | 没有后缀请用""                                  |
| setSelectFileTypes(String... fileTypes)                      | 设置选择文件类型                                       | 没有后缀请用""                                  |
| setSortType(int sortType)                                    | 设置排序规则                                           | 类型请看MConstants                              |
| setRadio()                                                   | 设置单选(如果需要单选文件夹请使用setMaxCount(0)来替换) | 默认多选                                        |
| setShowSelectStorageBtn(boolean var)                         | 设置是否显示内部存储选择按钮                           | 默认true                                        |
| setShowTitlebarFragment(boolean var)                         | 是否显示标题栏                                         | 默认true                                        |
| setShowTabbarFragment(boolean var)                           | 是否显示面包屑                                         | 默认true                                        |
| setAlwaysShowHandleFragment(boolean var)                     | 是否总是显示长按弹出选项                               | 默认false                                       |
| setTitlebarMainTitle(FontBean titlebarMainTitle)             | 设置标题栏主标题                                       | 还可以设置字体大小,颜色等                       |
| setTitlebarBG(Integer titlebarBG)                            | 设置标题栏背景颜色                                     |                                                 |
| setFileItemListener(FileItemListener fileItemListener)       | 设置文件item点击回调                                   | 点击是文件才会回调,如果点击是文件夹则不会       |
| setMorePopupItemListeners(CommonItemListener... morePopupItemListener) | 设置右上角选项回调                                     |                                                 |
| setHandleItemListeners(CommonItemListener... handleItemListener) | 设置长按弹出选项回调                                   |                                                 |
| setTitlebarFragment(AbstractTitlebarFragment titlebarFragment) | 设置自定义标题栏UI                                     | 自己的Fragment必须继承AbstractTitlebarFragment  |
| setHandleFragment(AbstractHandleFragment handleFragment)     | 设置长按弹出自定义UI                                   | 自己的Fragment必须继承AbstractHandleFragment    |
| start()                                                      | 开始构建                                               | 必须调用                                        |
| ......                                                       | ......                                                 |                                                 |

## 五、！！！特别注意 ！！！

#### 分区存储

##### 该库以及适配了分区存储，不需要额外适配，你只需要写你的业务代码即可，其他的交给它。

- 注意该库已经在库的`AndroidManifest.xml`中添加了：

  ```xml
  <!-- 外部存储的写权限 -->
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
  <!-- 安卓11额外权限 -->
  <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"
      tools:ignore="ScopedStorage" />
  <!-- 已经适配了分区存储特性 -->
  <application
          android:preserveLegacyExternalStorage="true"
          android:requestLegacyExternalStorage="true"
          >
  ```

- 可能会报错：

  ```java
  Execution failed for task ':app:processDebugMainManifest'.
  > Manifest merger failed with multiple errors, see logs
  ```

  请在你的项目中的`AndroidManifest.xml`设置一致

#### 包含Android Support库

- 如果你的项目使用Androidx库，因为此库引用了[getActivity/XXPermissions](https://github.com/getActivity/XXPermissions)库，其包含旧版Support库，会导致冲突而报错。解决办法为在project主目录下的gradle.properties中添加

  ```
  android.enableJetifier=true
  ```

#### 版本升级

- 新版本往往解决了旧版本的一些问题、增加了性能、可扩展性......建议升级新版本
- 请注意因为重构了项目导致了旧版本与新版本不兼容。1.0.x升级1.1.x为非兼容升级，请注意学习新的API


#### 体积过大

- 已经集成了[Blankj/AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

  如果项目对大小有严格要求请自行下载源码并精简AndroidUtilCode模块

#### 代码混淆

- 一般来说无需配置，会自动导入混淆规则

# 特别鸣谢

- [getActivity/XXPermissions](https://github.com/getActivity/XXPermissions)
- [CymChad/BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
- [Blankj/AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)
- [xuexiangjys/XTask](https://github.com/xuexiangjys/XTask)
- [ZLYang110/FileSelector](https://github.com/ZLYang110/FileSelector)
- [zzy0516alex/FileSelectorRelease](https://github.com/zzy0516alex/FileSelectorRelease)
- [folderv/androidDataWithoutRootAPI33](https://github.com/folderv/androidDataWithoutRootAPI33)

开源项目以及其依赖项目。

### LICENSE 

```
   Copyright [2020] molihuan

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

