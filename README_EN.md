# mlhfileselectorlib

[![Maven Central](https://img.shields.io/maven-central/v/io.github.molihuan/pathselector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.molihuan%22%20AND%20a:%22pathselector%22) ![API](https://img.shields.io/badge/API-19%2B-green) ![license: Apache-2.0 (shields.io)](https://img.shields.io/badge/license-Apache--2.0-brightgreen) [![bilibili: 玲莫利 (shields.io)](https://img.shields.io/badge/bilibili-玲莫利-orange)](https://space.bilibili.com/454222981) [![CSDN: molihuan (shields.io)](https://img.shields.io/badge/CSDN-molihuan-blue)](https://blog.csdn.net/molihuan)

Provide file or path selection, automatically apply for storage permission, support Android 4.4 to 12, support Android/data directory access, support custom UI,Support SD card.The Keyword:file selector operator android/data android 11

## Preface

#### may I have a little star before we begin?

#### thank you very much, your support is my only motivation.

#### Welcome to Star and Issues!



## 语言(Language)

### [中文](./README.md) | [English](./README_EN.md)

## 一、how to use

#### Step 1: Add repository:

- ##### If your project Gradle configuration is below '7.0', you need to add it in the 'build. Gradle' file

```java
allprojects {
    repositories {
        ...
        mavenCentral()
    }
}
```

- ##### If your Gradle configuration is' 7.0 'or above, you need to add it in the' settings. Gradle 'file

```
dependencyResolutionManagement {
    repositories {
    	...
        mavenCentral()
    }
}
```

#### Step 2: Add remote dependencies:

- ##### After configuring the remote repository, add the remote dependencies to the 'build.gradle' file in the project app module

- ##### Latest Release:[![Maven Central](https://img.shields.io/maven-central/v/io.github.molihuan/pathselector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.molihuan%22%20AND%20a:%22pathselector%22)

```java
dependencies {
    ...
    // 版本请自行选择建议使用最新的
    //The latest version is recommended
    implementation 'io.github.molihuan:pathselector:版本'
}
```

#### Step 3: Basic Usage demonstration:

```java
//如果没有权限会自动申请权限
// If there is no permission, the system automatically applies for permission
PathSelector.build(MainActivity.this, Constants.BUILD_ACTIVITY)//跳转Activity方式
                .requestCode(10011)//requestCode
    			.start();//Begin to build
```

#### Step 4: Get the returned data (you can also get the data in the clickback):

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 10011) {
        if(data!=null){
            List<String> pathData = data.getStringArrayListExtra(Constants.CALLBACK_DATA_ARRAYLIST_STRING);// Get data
            StringBuilder builder = new StringBuilder();
            for (String path : pathData) {
                builder.append(path).append("");
            }
            Mtools.toast(MainActivity.this,builder.toString());
        }
    }
}
```

## The demo presentation:

![pathSelectorDemo1](https://github.com/molihuan/mlhfileselectorlib/blob/master/MarkDownAssets/pathSelectorDemo1.gif)

## 二、More Settings

#### 1、Activity& All Settings mode:

```java
//Constants.BUILD_ACTIVITY为ACTIVITY模式
PathSelector.build(MainActivity.this, Constants.BUILD_ACTIVITY)
    .requestCode(10011)//requestCode
    .setRootPath("/storage/emulated/0")//Set the root directory (note no/at the end)
    .setMaxCount(3)// Set the maximum number of selections, default is -1 unlimited
    //.setToolbarFragment(new TestFragment())// Load a custom ToolbarFragment
    //.setMoreChooseFragment(new TestFragment())// Load a custom MoreChooseFragment
    //.setShowFileTypes("mp4","")// Set the display file type if no suffix please use ""
    //.setSelectFileTypes("mp3","mp4","ppt","")// Select file type if no suffix please use ""
    .setSortType(Constants.SORT_NAME_ASC)// Set the sort type
    //.isSingle()// Single mode cannot be multiple
    .showToolBarFragment(true)// Whether to display ToolbarFragment
    .setToolbarMainTitle("路径选择器")// Set the ToolbarFragment main header
    .setToolbarSubtitleTitle("MLH")// Set the ToolbarFragment subtitle
    .setToolbarSubtitleColor(Color.BLACK)//ToolbarFragment subtitle color
    // Set multiple items and their callback
    .setMoreChooseItems(
    new String[]{"全选", "删除"},
    // Anonymous objects
    new SelectOptions.onMoreChooseItemsListener() {
        @Override
        public void onItemsClick(View view, String currentPath, List<FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, FileListAdapter fileAdapter, List<FileBean> callBackFileBeanList) {
            Mtools.toast(getBaseContext(),"点击了全选");
        }
    },
    //Lambda expressions are used below for brevity
    (view,currentPath, fileBeanList,callBackData,tabbarAdapter,fileAdapter,callBackFileBeanList) -> {
        Mtools.toast(getBaseContext(),"点击了删除");
    }
)
    // Set the toolbarFragment option item and its callback
    .setMoreOPtions(
    new String[]{"选择"},
    new boolean[]{false},// Select whether to destroy the current Activity after it finishes
    (view,currentPath, fileBeanList,callBackData,tabbarAdapter,fileAdapter,callBackFileBeanList) -> {
        Mtools.toast(getBaseContext(),"点击了选择"+callBackData.get(0));
    }
)
    // Set FileItem and its callback in the file list
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
    .start();// Start building
```

#### 2、Fragments mode:

```java
// Get the return button click event in the PathSelectFragment instance onBackPressed
mPathSelectFragment = PathSelector.build(MainActivity.this, Constants.BUILD_FRAGMENT)
    .frameLayoutID(R.id.fragment_select_show_area)//The ID of the loading location,FrameLayout
    .requestCode(10011)//requestCode
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

#### 3、 Dialog mode:

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

#### 4、Custom toolbars:

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



## 三、Classes and methods (try to see the source code, have written comments, too lazy to write documentation)

##### SelectManager

| methods                                                      | annotation                                        | note                                                         |
| ------------------------------------------------------------ | ------------------------------------------------- | ------------------------------------------------------------ |
| frameLayoutID(int id)                                        | Set the loading location FrameLayoutID            |                                                              |
| requestCode(int code)                                        | Setting the request code                          |                                                              |
| setRootPath(String path)                                     | Set the default start path                        |                                                              |
| setMaxCount(int maxCount)                                    | Set the maximum number of selections              | Default to -1 means unlimited                                |
| setShowFileTypes(String... fileTypes)                        | Set the display file type                         |                                                              |
| setSelectFileTypes(String... fileTypes)                      | Set select file type                              |                                                              |
| setSortType(int sortType)                                    | 设置排序规则                                      |                                                              |
| isSingle()                                                   | Setting collation rules                           |                                                              |
| showToolBarFragment(boolean var)                             | Sets whether ToolBarFragment is displayed         |                                                              |
| setMoreOPtions(String[] optionsName ,SelectOptions.onToolbarOptionsListener ...optionListeners) | Set some Toolbar options (with overloads)         |                                                              |
| setToolbarViewClickers(SelectOptions.onToolbarListener ...listeners) | Set up some Toolbar clicks                        |                                                              |
| SelectManager setMoreChooseItems(String[] ItemsName ,SelectOptions.onMoreChooseItemsListener ...itemListeners) | Set some MoreChooseItems options (with overloads) |                                                              |
| setFileItemListener(SelectOptions.onFileItemListener onFileItem) | FileItem Click/long press callback                |                                                              |
| setToolbarMainTitle(String title)                            | Set the Toolbar main title                        | Title related options are listed in a series of different ones |
| setToolbarFragment(Fragment fragment)                        | Set up a custom title bar UI                      | AbstractToolbarFragment is recommended                       |
| setMoreChooseFragment(Fragment fragment)                     | Set up a custom multi-choice UI                   |                                                              |
| start()                                                      | Begin to build                                    | You must call                                                |
| ......                                                       | ......                                            |                                                              |

## 四、!!!Special attention!!

#### Partition storage

##### The library also ADAPTS partitioned storage, no additional adaptation is required, you just need to write your business code, leave the rest to it.

- Note that the library has been added to the library's 'androidmanifest.xml'：

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

- Error may occur:

  ```java
  Execution failed for task ':app:processDebugMainManifest'.
  > Manifest merger failed with multiple errors, see logs
  ```

  Please be consistent in setting 'androidmanifest.xml' in your project



# Thank

[ZLYang110/FileSelector: Android 文件选择器，指定选择文件夹还是文件，根据后缀名过滤，支持多选 (github.com)](https://github.com/ZLYang110/FileSelector)

[zzy0516alex/FileSelectorRelease: lib (github.com)](https://github.com/zzy0516alex/FileSelectorRelease)

[getActivity/XXPermissions: Android 权限请求框架，已适配 Android 12 (github.com)](https://github.com/getActivity/XXPermissions)

[CymChad/BaseRecyclerViewAdapterHelper: BRVAH:Powerful and flexible RecyclerAdapter (github.com)](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

[Blankj/AndroidUtilCode: Android developers should collect the following utils(updating). (github.com)](https://github.com/Blankj/AndroidUtilCode)

Open source projects and their dependencies

### [LICENSE ](https://github.com/molihuan/mlhfileselectorlib/blob/master/LICENSE)
