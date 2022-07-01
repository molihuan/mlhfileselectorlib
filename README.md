# mlhfileselectorlib

[![Maven Central](https://img.shields.io/maven-central/v/io.github.molihuan/pathselector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.molihuan%22%20AND%20a:%22pathselector%22) ![API](https://img.shields.io/badge/API-19%2B-green) ![license: Apache-2.0 (shields.io)](https://img.shields.io/badge/license-Apache--2.0-brightgreen) [![bilibili: 玲莫利 (shields.io)](https://img.shields.io/badge/bilibili-玲莫利-orange)](https://space.bilibili.com/454222981) [![CSDN: molihuan (shields.io)](https://img.shields.io/badge/CSDN-molihuan-blue)](https://blog.csdn.net/molihuan)

提供文件或路径选择，自动申请存储权限，支持安卓4.4 ~ 12，支持Android/data目录访问，支持自定义UI，支持SD卡。(Provide file or path selection, automatically apply for storage permission, support Android 4.4 to 12, support Android/data directory access, support custom UI,Support SD card.The Keyword:file selector operator android/data android 11)

## 前言

#### 在开始之前可以给我一个小星星吗？

#### 非常感谢，你的支持是我唯一动力。

#### 欢迎Star和Issues!



## 语言(Language)

### [中文](./README.md) | [English](./README_EN.md)

## 一、如何使用

#### 第1步：添加仓库:

- ##### 如果你的项目 Gradle 配置是在 `7.0 以下`，需要在 `build.gradle` 文件中加入

```java
allprojects {
    repositories {
        ...
        mavenCentral()
    }
}
```

- ##### 如果你的 Gradle 配置是 `7.0 及以上`，则需要在 `settings.gradle` 文件中加入

```
dependencyResolutionManagement {
    repositories {
    	...
        mavenCentral()
    }
}
```

#### 第2步：添加远程依赖:

- ##### 配置完远程仓库后，在项目 app 模块下的 `build.gradle` 文件中加入远程依赖

- ##### 最新发布版:[![Maven Central](https://img.shields.io/maven-central/v/io.github.molihuan/pathselector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.molihuan%22%20AND%20a:%22pathselector%22)

```java
dependencies {
    ...
    // 版本请自行选择建议使用最新的
    implementation 'io.github.molihuan:pathselector:版本'
}
```

#### 第3步：基本Java用法示范:

```java
//如果没有权限会自动申请权限
PathSelector.build(MainActivity.this, Constants.BUILD_ACTIVITY)//跳转Activity方式
                .requestCode(10011)//请求码
    			.start();//开始构建
```

#### 第4步：获取返回的数据(也可以在点击回调中获取数据):

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 10011) {
        if(data!=null){
            List<String> pathData = data.getStringArrayListExtra(Constants.CALLBACK_DATA_ARRAYLIST_STRING);//获取数据
            StringBuilder builder = new StringBuilder();
            for (String path : pathData) {
                builder.append(path).append("");
            }
            Mtools.toast(MainActivity.this,builder.toString());
        }
    }
}
```

## demo演示：

![pathSelectorDemo1](https://github.com/molihuan/mlhfileselectorlib/blob/master/MarkDownAssets/pathSelectorDemo1.gif)

## 二、更多设置

#### 1、Activity&所有设置模式：

```java
//Constants.BUILD_ACTIVITY为ACTIVITY模式
PathSelector.build(MainActivity.this, Constants.BUILD_ACTIVITY)
    .requestCode(10011)//请求码
    .setRootPath("/storage/emulated/0")//设置根目录(注意最后没有/)
    .setMaxCount(3)//设置最大选择数量，默认是-1不限制
    //.setToolbarFragment(new TestFragment())//加载自定义ToolbarFragment
    //.setMoreChooseFragment(new TestFragment())//加载自定义MoreChooseFragment
    //.setShowFileTypes("mp4","")//设置显示文件类型如果无后缀请使用""
    //.setSelectFileTypes("mp3","mp4","ppt","")//设置选择文件类型如果无后缀请使用""
    .setSortType(Constants.SORT_NAME_ASC)//设置排序类型
    //.isSingle()//单选模式不能多选
    .showToolBarFragment(true)//是否显示ToolbarFragment
    .setToolbarMainTitle("路径选择器")//设置ToolbarFragment主标题
    .setToolbarSubtitleTitle("MLH")//设置ToolbarFragment副标题
    .setToolbarSubtitleColor(Color.BLACK)//ToolbarFragment副标题颜色
    //设置多选item和其回调
    .setMoreChooseItems(
    new String[]{"全选", "删除"},
    //匿名对象
    new SelectOptions.onMoreChooseItemsListener() {
        @Override
        public void onItemsClick(View view, String currentPath, List<FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, FileListAdapter fileAdapter, List<FileBean> callBackFileBeanList) {
            Mtools.toast(getBaseContext(),"点击了全选");
        }
    },
    //lambda表达式 为了简洁下面都使用lambda表达式
    (view,currentPath, fileBeanList,callBackData,tabbarAdapter,fileAdapter,callBackFileBeanList) -> {
        Mtools.toast(getBaseContext(),"点击了删除");
    }
)
    //设置toolbarFragment更多选项item和其回调
    .setMoreOPtions(
    new String[]{"选择"},
    new boolean[]{false},//选择结束后是否需要销毁当前Activity
    (view,currentPath, fileBeanList,callBackData,tabbarAdapter,fileAdapter,callBackFileBeanList) -> {
        Mtools.toast(getBaseContext(),"点击了选择"+callBackData.get(0));
    }
)
    //设置文件列表中FileItem和其回调
    .setFileItemListener(new com.molihuan.pathselector.dao.SelectOptions.onFileItemListener() {
        @Override
        public boolean onFileItemClick(View view, String currentPath, List<com.molihuan.pathselector.entities.FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, com.molihuan.pathselector.adapters.FileListAdapter fileAdapter,FileBean fileBean) {
            Mtools.toast(getBaseContext(),currentPath);
            return false;
        }
        @Override
        public boolean onLongFileItemClick(View view, String currentPath, List<com.molihuan.pathselector.entities.FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, com.molihuan.pathselector.adapters.FileListAdapter fileAdapter,FileBean fileBean) {
            return false;
        }
    })
    .start();//开始构建
```

#### 2、Fragment模式：

```java
//获取PathSelectFragment实例onBackPressed中处理返回按钮点击事件
mPathSelectFragment = PathSelector.build(MainActivity.this, Constants.BUILD_FRAGMENT)
    .frameLayoutID(R.id.fragment_select_show_area)//加载位置,FrameLayout的ID
    .requestCode(10011)//请求码
    .showToolBarFragment(false)//是否显示ToolbarFragment
    //设置多选item和其回调
    .setMoreChooseItems(
    new String[]{"全选", "删除"},
    (view,currentPath, fileBeanList,callBackData,tabbarAdapter,fileAdapter,callBackFileBeanList) -> {
        Mtools.toast(getBaseContext(),"点击了全选");
    },
    (view,currentPath, fileBeanList,callBackData,tabbarAdapter,fileAdapter,callBackFileBeanList) -> {
        Mtools.toast(getBaseContext(),"点击了删除"+callBackData.get(0));
    }
)
    .start();//开始构建
```

##### onBackPressed()中

```java
@Override
public void onBackPressed() {
    //让PathSelectFragment先处理返回按钮点击事件
    if (mPathSelectFragment!=null&&mPathSelectFragment.onBackPressed()){
        return;
    }
    super.onBackPressed();
}
```

#### 3、Dialog模式：

```java
//获取PathSelectFragment实例onBackPressed中处理返回按钮点击事件
mPathSelectFragment = PathSelector.build(MainActivity.this, Constants.BUILD_DIALOG)
    .frameLayoutID(R.id.fragment_select_show_area)//加载位置FrameLayout的ID
    .requestCode(10011)//请求码
    .showToolBarFragment(true)//是否显示ToolbarFragment
    //设置toolbarFragment更多选项item和其回调
    .setMoreOPtions(
    new String[]{"选择"},
    new boolean[]{true},
    (view,currentPath, fileBeanList,callBackData,tabbarAdapter,fileAdapter,callBackFileBeanList) -> {
        Mtools.toast(getBaseContext(),"点击了选择"+currentPath);
    }
)
    .start();//开始构建
```

#### 4、自定义Toolbar：

```java
//获取PathSelectFragment实例onBackPressed中处理返回按钮点击事件
mPathSelectFragment = PathSelector.build(MainActivity.this, Constants.BUILD_ACTIVITY)
    .requestCode(10011)//请求码
    .showToolBarFragment(true)//是否显示ToolbarFragment 如果自定义必须为true 默认为true
    .setToolbarFragment(new CustomToolbarFragment())
    .setToolbarViewClickers(
    new SelectOptions.onToolbarListener() {
        @Override
        public void onClick(View view, String currentPath, List<FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, FileListAdapter fileAdapter,List<FileBean> callBackFileBeanList) {
            Mtools.toast(getBaseContext(),"点击了按钮1");
        }
    }
)
    .start();//开始构建
```



## 三、类与方法（尽量看源码，都写了注释，懒得写文档）

##### SelectManager

| 方法                                                         | 注释                                | 备注                            |
| ------------------------------------------------------------ | ----------------------------------- | ------------------------------- |
| frameLayoutID(int id)                                        | 设置加载位置FrameLayoutID           |                                 |
| requestCode(int code)                                        | 设置请求码                          |                                 |
| setRootPath(String path)                                     | 设置开始默认路径                    |                                 |
| setMaxCount(int maxCount)                                    | 设置最大选择数量                    | 不设置默认为-1 即无限           |
| setShowFileTypes(String... fileTypes)                        | 设置显示文件类型                    |                                 |
| setSelectFileTypes(String... fileTypes)                      | 设置选择文件类型                    |                                 |
| setSortType(int sortType)                                    | 设置排序规则                        |                                 |
| isSingle()                                                   | 设置单选                            |                                 |
| showToolBarFragment(boolean var)                             | 设置ToolBarFragment是否显示         |                                 |
| setMoreOPtions(String[] optionsName ,SelectOptions.onToolbarOptionsListener ...optionListeners) | 设置一些Toolbar选项(有重载)         |                                 |
| setToolbarViewClickers(SelectOptions.onToolbarListener ...listeners) | 设置一些Toolbar点击                 |                                 |
| SelectManager setMoreChooseItems(String[] ItemsName ,SelectOptions.onMoreChooseItemsListener ...itemListeners) | 设置一些MoreChooseItems选项(有重载) |                                 |
| setFileItemListener(SelectOptions.onFileItemListener onFileItem) | FileItem点击/长按回调               |                                 |
| setToolbarMainTitle(String title)                            | 设置Toolbar主标题                   | 标题相关选项一系列不一一列举    |
| setToolbarFragment(Fragment fragment)                        | 设置自定义标题栏UI                  | 建议继承AbstractToolbarFragment |
| setMoreChooseFragment(Fragment fragment)                     | 设置自定义多选UI                    |                                 |
| start()                                                      | 开始构建                            | 必须调用                        |
| ......                                                       | ......                              |                                 |

## 四、！！！特别注意！！！

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



# 非常感谢

[ZLYang110/FileSelector: Android 文件选择器，指定选择文件夹还是文件，根据后缀名过滤，支持多选 (github.com)](https://github.com/ZLYang110/FileSelector)

[zzy0516alex/FileSelectorRelease: lib (github.com)](https://github.com/zzy0516alex/FileSelectorRelease)

[getActivity/XXPermissions: Android 权限请求框架，已适配 Android 12 (github.com)](https://github.com/getActivity/XXPermissions)

[CymChad/BaseRecyclerViewAdapterHelper: BRVAH:Powerful and flexible RecyclerAdapter (github.com)](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

[Blankj/AndroidUtilCode: Android developers should collect the following utils(updating). (github.com)](https://github.com/Blankj/AndroidUtilCode)

开源项目以及其依赖项目。

### [LICENSE ](https://github.com/molihuan/mlhfileselectorlib/blob/master/LICENSE)
