# mlhfileselectorlib

[![Maven Central](https://img.shields.io/maven-central/v/io.github.molihuan/mlhfileselectorlib.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.molihuan%22%20AND%20a:%22mlhfileselectorlib%22)

- [中文文档](./README_CN.md)

Provide file path selection, support automatic application read permission support Android 11, support automatic access to Android/data directory (file browsing is still adapted), has very high scalability, support custom UI.

## Thanks

[ZLYang110/FileSelector: Android 文件选择器，指定选择文件夹还是文件，根据后缀名过滤，支持多选 (github.com)](https://github.com/ZLYang110/FileSelector)

[zzy0516alex/FileSelectorRelease: lib (github.com)](https://github.com/zzy0516alex/FileSelectorRelease)

[getActivity/XXPermissions: Android 权限请求框架，已适配 Android 12 (github.com)](https://github.com/getActivity/XXPermissions)

Open source projects and their dependencies.

## 1、How to use

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

```java
dependencies {
    ...
    // 版本请自行选择
    //Please choose your own version
    implementation 'io.github.molihuan:mlhfileselectorlib:1.0.3'
}
```

#### Step 3: Basic Java usage demonstration:

```java
//如果没有权限会自动申请权限
FileSelector.from(this)
    		.requestCode(100) //requestCode
    		.start();
```

#### Step 4: Get the returned data:

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 100) {//requestCode
        if(data!=null){
            ArrayList<String> essFileList = data.getStringArrayListExtra(Const.EXTRA_RESULT_SELECTION);
            StringBuilder builder = new StringBuilder();
            for (String file : essFileList) {
                builder.append(file).append("");
            }
            ToastUtils.make().show(builder.toString());
        }
    }
}
```

## 2、Classes and methods (try to see the source code, have written comments, too lazy to write documentation)

##### SelectCreator

| 方法                                                         | 注释                                                         | 备注 |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ---- |
| setMoreOPtions(String[] optionsName , boolean [] optionsNeedCallBack ,SelectOptions.IToolbarOptionsListener ...onOptionClicks) | Set more options for the Toolbar and listen (overloaded)     |      |
| setFileItemDispose(SelectOptions.IOnFileItemListener onFileItem) | Set FileItem click/long click callback                       |      |
| setMaxCount(int maxCount)                                    | Set the maximum number of selections                         |      |
| setToolbar(Fragment toolbarFragment)                         | Set custom title bar (recommend inheriting BaseFragment under package) |      |
| setTargetPath(String path)                                   | Set the default start path                                   |      |
| setFileTypes(String... fileTypes)                            | Set select file type                                         |      |
| setSortType(int sortType)                                    | Setting collation rules                                      |      |
| isSingle()                                                   | Set the radio                                                |      |
| onlyShowFolder()                                             | Set only folders to be displayed                             |      |
| onlySelectFolder()                                           | Set only folders to be selected                              |      |
| requestCode(int requestCode)                                 | Setting the request code                                     |      |
| ......                                                       | ......                                                       |      |

#### The advanced：

```java
FileSelector.from(this)
    .onlyShowFolder()  //只显示文件夹
    .onlySelectFolder()  //只能选择文件夹
    .isSingle() // 只能选择一个
    .setMaxCount(5) //设置最大选择数
    .setFileTypes("png", "doc","apk", "mp3", "gif", "txt", "mp4") //设置文件类型
    .setSortType(FileSelector.BY_NAME_ASC) //设置名字排序
    //.setSortType(FileSelector.BY_TIME_ASC) //设置时间排序
    //.setSortType(FileSelector.BY_SIZE_DESC) //设置大小排序
    .requestCode(100) //设置返回码
    .setTargetPath("/storage/emulated/0/") //设置默认目录
    .setToolbar(new EmptyFragment())//自定义Toolbar建议继承库中的BaseFragment
    //自定义Toolbar更多按钮以及监听事件
    .setMoreOPtions(new String[]{"选择","删除"},
                    new boolean[]{true,false},
                    new SelectOptions.IToolbarOptionsListener() {
                        @Override
                        public void onOptionClick(Context context, int position, String currentPath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter) {
                            ToastUtils.make().show("选择"+currentPath);
                        }
                    },
                    new SelectOptions.IToolbarOptionsListener() {
                        @Override
                        public void onOptionClick(Context context, int position, String currentPath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter) {
                            ToastUtils.make().show("删除"+currentPath);
                        }
                    }
                   )
    //item点击or长按回调
    .setFileItemDispose(new SelectOptions.IOnFileItemListener() {
        @Override
        public void onFileItemClick(Context context, int position, String fileAbsolutePath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter) {
            ToastUtils.make().show("点击"+fileAbsolutePath);
        }
        @Override
        public void onLongFileItemClick(Context context, int position, String fileAbsolutePath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter) {
            ToastUtils.make().show("长按"+fileAbsolutePath);
        }
    })
    .start();
```



## 3、！！！Pay special attention to！！！

#### Partition storage

##### The library also ADAPTS partitioned storage, no additional adaptation is required, you just need to write your business code, leave the rest to it.

- Note that the library has been added to the library's `Androidmanifest.xml`：

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

- Errors may be reported：

  ```java
  Execution failed for task ':app:processDebugMainManifest'.
  > Manifest merger failed with multiple errors, see logs
  ```

  Please be consistent in setting `Androidmanifest.xml` in your project
